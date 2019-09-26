package rubtsov.revolut.controller;

import rubtsov.revolut.logics.AccountsService;
import rubtsov.revolut.model.Account;
import rubtsov.revolut.model.TransferOrder;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Optional;

@Path("accounts")
public class AccountsController {

    private final AccountsService accountsService;

    @Inject
    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GET
    @Path("/{number}")
    public Response getAccount(@PathParam("number") String number) {
        Optional<Account> account = accountsService.get(number);
        if (account.isPresent()) {
            return Response.ok(account.get()).build();
        } else {
            return Response.status(Status.BAD_REQUEST).entity("Account not found").build();
        }
    }

    @POST
    @Path("/makeTransfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response makeTransfer(TransferOrder transferOrder) {
        try {
            accountsService.makeTransfer(transferOrder);
            return Response.ok().build();
        } catch (IllegalArgumentException e) {
            return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

}
