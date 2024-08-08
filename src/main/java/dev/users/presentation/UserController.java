package dev.users.presentation;

import dev.users.exceptions.ServiceException;
import dev.users.usecase.UserUseCase;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/api/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private final UserUseCase uc;

    @Inject
    public UserController(
            UserUseCase uc) {
        this.uc = uc;
    }

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @WithSession
    public Uni<Response> createUser(
        @FormParam("username") final String username,
        @FormParam("email") final String email,
        @FormParam("password") final String password
    ) {
        try {
            return uc.createUser(username, email, password)
                    .map(response -> Response.status(Status.OK).entity(response).build())
                    .log()
                    .onFailure().transform(e -> {
                        String message = e.getMessage();
                        throw new ServiceException(
                                message,
                                Response.Status.BAD_REQUEST);
                    });
        } catch (Exception e) {
            String message = e.getMessage();
            throw new ServiceException(
                    message,
                    Response.Status.BAD_REQUEST);
        }
    }

    @POST
    @Path("/authenticate")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    @WithSession
    public Uni<Response> authenticate(
        @FormParam("email") final String email,
        @FormParam("password") final String password
    ) {
        try {
            return uc.authenticateUser(email, password)
                    .map(response -> Response.status(Status.OK).entity(response).build())
                    .log()
                    .onFailure().transform(e -> {
                        String message = e.getMessage();
                        throw new ServiceException(
                                message,
                                Response.Status.BAD_REQUEST);
                    });
        } catch (Exception e) {
            String message = e.getMessage();
            throw new ServiceException(
                    message,
                    Response.Status.BAD_REQUEST);
        }
    }

}
