package rubtsov.revolut.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("accounts")
public class AccountsController {

    @GET
    @Path("/sanity")
    public String sanityCheck() {
        return "hello";
    }

}
