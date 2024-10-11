package org.example.exceptionmapper;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ProcessingExceptionHandler implements ExceptionMapper<ProcessingException> {
    private static final Logger logger = LoggerFactory.getLogger(ProcessingExceptionHandler.class);

    @Override
    public Response toResponse(ProcessingException exception) {
        logger.error("ProcessingException occurred: ", exception);

        String clientMessage = "An error occurred while processing the request.";
        Throwable cause = exception.getCause();
        if (cause != null && cause.getMessage().contains("No enum constant")) {
            clientMessage = "Invalid enum value provided.";
        }

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Bad request");
        response.put("status", Response.Status.BAD_REQUEST.getStatusCode());
        response.put("message", clientMessage);
        response.put("timestamp", LocalDateTime.now().toString());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
