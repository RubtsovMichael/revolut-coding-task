package rubtsov.revolut.logics;

import rubtsov.revolut.model.Account;

import java.util.Optional;

public interface AccountsRepository {

    Optional<Account> get(String number);

    boolean create(Account account);

}
