package rubtsov.revolut.application;

import rubtsov.revolut.logics.InMemoryAccountsRepository;
import rubtsov.revolut.model.Account;

import java.math.BigDecimal;

public class InitialAccounts {

    public static void persist(InMemoryAccountsRepository repository) {
        createAccount(repository, "1111222233334444", "123.45");
        createAccount(repository, "2222222233334444", "0");
        createAccount(repository, "3333222233334444", "1000000");
        createAccount(repository, "4444222233334444", "656565.45");
        createAccount(repository, "5555222233334444", "6454666.12");
        createAccount(repository, "6666222233334444", "11132.23");
        createAccount(repository, "123456789", "123.45");
        createAccount(repository, "987654321", "1");
    }

    private static boolean createAccount(InMemoryAccountsRepository repository, String number, String amount) {
        return repository.create(new Account(number, new BigDecimal(amount)));
    }

}
