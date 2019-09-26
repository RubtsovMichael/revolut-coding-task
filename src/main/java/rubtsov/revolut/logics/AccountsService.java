package rubtsov.revolut.logics;

import rubtsov.revolut.model.Account;
import rubtsov.revolut.model.TransferOrder;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountsService {

    private final AccountsRepository repository;

    public AccountsService(AccountsRepository accountsRepository) {
        this.repository = accountsRepository;
    }

    public Optional<Account> get(String number) {
        return repository.get(number);
    }

    public void makeTransfer(TransferOrder order) {
        if (order.getFrom().equals(order.getTo())) {
            throw new IllegalArgumentException("Source and target account numbers must be different");
        }

        Account sourceAccount = get(order.getFrom()).orElseThrow(() -> new IllegalArgumentException("Account " + order.getFrom() + " not found"));
        Account targetAccount = get(order.getTo()).orElseThrow(() -> new IllegalArgumentException("Account " + order.getTo() + " not found"));

        BigDecimal transferAmount = order.getAmount();
        verifyFundsAvailability(sourceAccount, transferAmount);
        targetAccount.payIn(transferAmount);
        sourceAccount.payOut(transferAmount);
    }

    private void verifyFundsAvailability(Account sourceAccount, BigDecimal transferAmount) {
        if (transferAmount.compareTo(sourceAccount.getAmount()) > 0) {
            throw new IllegalArgumentException("Not enough funds for transfer");
        }
    }

}
