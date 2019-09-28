package rubtsov.revolut.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest {

    @Test
    void accountCouldBeLockedAndThenUnlocked() {
        Account account = new Account("111", BigDecimal.ONE);
        account.lock();
        account.unlock();
    }

    @Test
    void unlockFailsIfWasNotLocked() {
        Account account = new Account("111", BigDecimal.ONE);
        assertThatThrownBy(account::unlock)
                .isInstanceOf(IllegalMonitorStateException.class);
    }

    @Test
    void payInAddsMoney() {
        Account account = new Account("111", BigDecimal.ONE);

        account.payIn(new BigDecimal("187.65"));

        assertThat(account.getAmount()).isEqualTo(new BigDecimal("188.65"));
    }

    @Test
    void payOutReducesMoney() {
        Account account = new Account("111", BigDecimal.ONE);

        account.payOut(new BigDecimal("187.65"));

        assertThat(account.getAmount()).isEqualTo(new BigDecimal("-186.65"));
    }
}