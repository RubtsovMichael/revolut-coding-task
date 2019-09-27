package rubtsov.revolut.application;

import rubtsov.revolut.logics.InMemoryAccountsRepository;
import rubtsov.revolut.model.Account;

import java.math.BigDecimal;

public class InitialAccounts {

    public static void persist(InMemoryAccountsRepository repository) {
        createAccount(repository, "123456789", "123.45");
        createAccount(repository, "987654321", "1");
    }

    private static boolean createAccount(InMemoryAccountsRepository repository, String number, String amount) {
        return repository.create(new Account(number, new BigDecimal(amount)));
    }

}
