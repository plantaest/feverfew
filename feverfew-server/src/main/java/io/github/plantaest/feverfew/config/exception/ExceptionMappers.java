package io.github.plantaest.feverfew.config.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.quarkus.logging.Log;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ExceptionMappers {

    @ServerExceptionMapper
    public Response throwable(Throwable e, UriInfo uriInfo) {
        String errorId = UUID.randomUUID().toString();
        Log.errorf("ErrorID[%s]: %s", errorId, e.getMessage());
        return Response.serverError()
                .entity(AppError.builder()
                        .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                        .type(URI.create("https://feverfew.toolforge.org/problems/internal-server-error"))
                        .title("Internal Server Error")
                        .detail("An unexpected error has occurred. Please contact support.")
                        .instance(URI.create(uriInfo.getPath()))
                        .code("INTERNAL_SERVER_ERROR")
                        .errorId(errorId)
                        .build())
                .build();
    }

    @ServerExceptionMapper(value = {MismatchedInputException.class})
    public Response jsonMappingException(JsonMappingException e, UriInfo uriInfo) {
        String errorId = UUID.randomUUID().toString();
        Log.errorf("ErrorID[%s]: %s", errorId, e.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(AppError.builder()
                        .status(Response.Status.BAD_REQUEST.getStatusCode())
                        .type(URI.create("https://feverfew.toolforge.org/problems/json-mapping-error"))
                        .title("JSON Mapping Error")
                        .detail("Cannot map JSON. Please check the request again.")
                        .instance(URI.create(uriInfo.getPath()))
                        .code("JSON_MAPPING_ERROR")
                        .errorId(errorId)
                        .build())
                .build();
    }

    @ServerExceptionMapper
    public Response constraintViolationException(ConstraintViolationException e, UriInfo uriInfo) {
        String errorId = UUID.randomUUID().toString();
        Log.errorf("ErrorID[%s]: %s", errorId, e.getMessage());

        Map<String, AppError.Violation> violations = new HashMap<>();

        for (var violation : e.getConstraintViolations()) {
            if (violations.containsKey(violation.getPropertyPath().toString())) {
                violations.get(violation.getPropertyPath().toString()).messages().add(violation.getMessage());
            } else {
                violations.put(
                        violation.getPropertyPath().toString(),
                        new AppError.Violation(violation.getPropertyPath().toString(), new ArrayList<>(List.of(
                                Optional.ofNullable(violation.getMessage())
                                        .orElse("This field is not valid"))))
                );
            }
        }

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(AppError.builder()
                        .status(Response.Status.BAD_REQUEST.getStatusCode())
                        .type(URI.create("https://feverfew.toolforge.org/problems/validation-error"))
                        .title("Validation Error")
                        .detail("Validation error on request body")
                        .instance(URI.create(uriInfo.getPath()))
                        .code("VALIDATION_ERROR")
                        .violations(violations.values().stream().toList())
                        .errorId(errorId)
                        .build())
                .build();
    }

}
