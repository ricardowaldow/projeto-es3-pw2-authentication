package dev.users.presentation;

import dev.users.application.requests.AuthenticateRequest;
import dev.users.application.requests.CreateUserRequest;
import dev.users.application.usecase.AuthenticateUseCase;
import dev.users.application.usecase.CreateUserUseCase;
import dev.users.exceptions.ServiceException;
import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final AuthenticateUseCase authenticateUseCase;

    @Inject
    public UserController(
            CreateUserUseCase createUserUseCase,
            AuthenticateUseCase authenticateUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.authenticateUseCase = authenticateUseCase;
    }

    @POST
    @Path("/user/create")
    @WithSession
    public Uni<Response> createUser(CreateUserRequest request) {
        try {
            return createUserUseCase.execute(request)
                    .map(response -> Response.status(Status.CREATED).entity(response).build())
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
    @WithSession
    public Uni<Response> authenticate(AuthenticateRequest request) {
        // try {
            return authenticateUseCase.execute(request)
                    .map(response -> Response.status(Status.ACCEPTED).entity(response).build())
                    .log();
                    // .onFailure().transform(e -> {
                    //     String message = e.getMessage();
                    //     throw new ServiceException(
                    //             message,
                    //             Response.Status.BAD_REQUEST);
                    // });
        // } catch (Exception e) {
        //     String message = e.getMessage();
        //     throw new ServiceException(
        //             message,
        //             Response.Status.BAD_REQUEST);
        // }
    }
}
