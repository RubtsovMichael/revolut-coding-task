package rubtsov.revolut.application;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import rubtsov.revolut.controller.AccountsController;
import rubtsov.revolut.logics.AccountsService;
import rubtsov.revolut.logics.InMemoryAccountsRepository;

public class AppConfig extends ResourceConfig {

    public AppConfig() {
        this(new AccountsService(new InMemoryAccountsRepository()));
    }

    public AppConfig(AccountsService accountsService) {
        register(AccountsController.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(accountsService);
            }
        });
    }

}
