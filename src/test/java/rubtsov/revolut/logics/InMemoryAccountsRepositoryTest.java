package rubtsov.revolut.logics;

import org.junit.jupiter.api.Test;
import rubtsov.revolut.model.Account;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryAccountsRepositoryTest {

    private final AccountsRepository repository = new InMemoryAccountsRepository();

    @Test
    void accountNotFound() {
        assertThat(repository.get("qqq")).isEmpty();
    }

    @Test
    void accountCreatedAndFound() {
        Account account = new Account("qqq", BigDecimal.ONE);
        assertThat(repository.create(account)).isTrue();
        assertThat(repository.get("qqq")).contains(account);
    }

    @Test
    void failsToCreateAccountTwice() {
        assertThat(repository.create(new Account("qqq", BigDecimal.ONE))).isTrue();
        assertThat(repository.create(new Account("qqq", BigDecimal.TEN))).isFalse();
    }

    @Test
    void listsAccounts() {
        repository.create(new Account("111", BigDecimal.ONE));
        repository.create(new Account("222", BigDecimal.TEN));
        repository.create(new Account("333", BigDecimal.TEN));

        assertThat(repository.listAccounts()).containsExactlyInAnyOrder("111", "222", "333");
    }

    @Test
    void listsAccountsAsEmpty() {
        assertThat(repository.listAccounts()).isEmpty();
    }

}