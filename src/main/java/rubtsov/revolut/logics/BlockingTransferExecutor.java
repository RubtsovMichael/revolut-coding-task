package rubtsov.revolut.logics;

import rubtsov.revolut.model.Account;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BlockingTransferExecutor {

    private final Account first;
    private final Account second;

    public BlockingTransferExecutor(Account account1, Account account2) {
        List<Account> sortedAccounts = Stream.of(account1, account2)
                .sorted(Comparator.comparing(Account::getNumber))
                .collect(Collectors.toList());

        first = sortedAccounts.get(0);
        second = sortedAccounts.get(1);
    }

    public void execute(Transaction transaction) {
        first.lock();
        try {
            second.lock();
            try {
                transaction.execute();
            } finally {
                second.unlock();
            }
        } finally {
            first.unlock();
        }
    }

    @FunctionalInterface
    public interface Transaction {
        void execute();
    }
}
