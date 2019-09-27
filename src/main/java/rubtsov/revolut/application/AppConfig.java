package rubtsov.revolut.application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import rubtsov.revolut.controller.AccountsController;
import rubtsov.revolut.logics.AccountsService;
import rubtsov.revolut.logics.InMemoryAccountsRepository;

public class AppConfig extends ResourceConfig {

    public AppConfig() {
        this(new AccountsService(prepopulatedRepository()));
    }

    private static InMemoryAccountsRepository prepopulatedRepository() {
        InMemoryAccountsRepository inMemoryAccountsRepository = new InMemoryAccountsRepository();
        InitialAccounts.persist(inMemoryAccountsRepository);
        return inMemoryAccountsRepository;
    }

    public AppConfig(AccountsService accountsService) {
        register(AccountsController.class);
        register(JacksonFeature.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(accountsService);
            }
        });
    }

}
