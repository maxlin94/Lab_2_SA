package org.example.exceptionmapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {
    private static final Logger logger = LoggerFactory.getLogger(ConstraintViolationExceptionHandler.class);

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        logger.error("ConstraintViolationException occurred: {}", exception.getMessage());

        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        String errors = violations.stream()
                .map(violation -> {
                    String propertyPath = violation.getPropertyPath().toString().replaceFirst("^.*\\.", "");
                    return propertyPath + ": " + violation.getMessage();
                })
                .collect(Collectors.joining(", "));

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation error");
        response.put("status", Response.Status.BAD_REQUEST.getStatusCode());
        response.put("message", errors);
        response.put("timestamp", LocalDateTime.now().toString());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(response)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
