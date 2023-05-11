package dev.users.exceptions;

import java.util.Map;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 * Service exception.
 */
public class ServiceException extends WebApplicationException {

    /**
     * Service Exception constructor.
     *
     * @param message : The message of the exception
     * @param status  : The HTTP error code
     */
    public ServiceException(final String message, final Status status) {
        super(init(message, status));
    }

    /**
     * A static method to init the message.
     *
     * @param message : An error message
     * @param status  : A HTTP error code
     *
     * @return A Response object
     */
    private static Response init(final String message, final Status status) {
        return Response
        .status(status)
        .entity(Map.of("message", message))
        .build();
    }

}
