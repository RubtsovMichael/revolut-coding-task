package rubtsov.revolut.logics;

import org.junit.jupiter.api.Test;
import rubtsov.revolut.model.Account;
import rubtsov.revolut.model.TransferOrder;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountsServiceTest {

    private final InMemoryAccountsRepository repository = new InMemoryAccountsRepository();
    private final AccountsService service = new AccountsService(repository);

    @Test
    void findsAccount() {
        Account account = new Account("123", BigDecimal.ONE);
        repository.create(account);

        assertThat(service.get("123")).contains(account);
    }

    @Test
    void transferFailsIfSourceNotFound() {
        assertThatThrownBy(() -> service.makeTransfer(TransferOrder.builder().from("qqq").build()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Account qqq not found");
    }

    @Test
    void transferFailsIfSameAccount() {
        assertThatThrownBy(() -> service.makeTransfer(TransferOrder.builder().from("qqq").to("qqq").build()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Source and target account numbers must be different");
    }

    @Test
    void transferFailsIfTargetNotFound() {
        repository.create(new Account("123", BigDecimal.ONE));
        TransferOrder order = TransferOrder.builder().from("123").to("qqq").build();
        assertThatThrownBy(() -> service.makeTransfer(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Account qqq not found");
    }

    @Test
    void transferFailsIfNotEnoughSourceFunds() {
        repository.create(new Account("123", BigDecimal.ONE));
        repository.create(new Account("456", BigDecimal.ONE));

        TransferOrder order = TransferOrder.builder().from("123").to("456").amount(new BigDecimal("5")).build();
        assertThatThrownBy(() -> service.makeTransfer(order))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Not enough funds for transfer");
    }

    @Test
    void transferWorks() {
        repository.create(new Account("123", BigDecimal.TEN));
        repository.create(new Account("456", BigDecimal.ZERO));

        TransferOrder order = TransferOrder.builder().from("123").to("456").amount(new BigDecimal("5")).build();
        service.makeTransfer(order);

        assertThat(service.get("123").get().getAmount()).isEqualTo(new BigDecimal("5.00"));
        assertThat(service.get("456").get().getAmount()).isEqualTo(new BigDecimal("5.00"));
    }

}