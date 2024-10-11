package org.example.exceptionmapper;

import jakarta.ws.rs.BadRequestException;
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
public class BadRequestExceptionHandler implements ExceptionMapper<BadRequestException> {
    private static final Logger logger = LoggerFactory.getLogger(BadRequestExceptionHandler.class);

    @Override
    public Response toResponse(BadRequestException exception) {
        logger.error("Bad request occurred: ", exception);

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Bad request");
        response.put("status", Response.Status.BAD_REQUEST.getStatusCode());
        response.put("timestamp", LocalDateTime.now().toString());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
