package rubtsov.revolut.logics;

import rubtsov.revolut.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountsRepository implements AccountsRepository {

    private final Map<String, Account> repo = new ConcurrentHashMap<>();

    @Override
    public Optional<Account> get(String number) {
        return Optional.ofNullable(repo.get(number));
    }

    @Override
    public boolean create(Account account) {
        return repo.putIfAbsent(account.getNumber(), account) == null;
    }

    @Override
    public List<String> listAccounts() {
        return new ArrayList<>(repo.keySet());
    }

}
