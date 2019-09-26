package rubtsov.revolut.application;

import org.glassfish.jersey.server.ResourceConfig;
import rubtsov.revolut.controller.AccountsController;

public class AppConfig extends ResourceConfig {

    public AppConfig() {
        register(AccountsController.class);
    }

}
