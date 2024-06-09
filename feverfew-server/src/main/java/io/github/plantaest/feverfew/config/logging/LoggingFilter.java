package io.github.plantaest.feverfew.config.logging;

import io.quarkus.logging.Log;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;

import java.util.Optional;

@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    @Context
    UriInfo uriInfo;

    @Context
    HttpServerRequest httpServerRequest;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        final String method = requestContext.getMethod();
        final String path = uriInfo.getPath();
        final int length = requestContext.getLength();

        Log.infof("Request %s %s (%s bytes)", method, path, length);
        Log.debugf("Request Headers: %s", requestContext.getHeaders());
        Optional.ofNullable(requestContext.getMediaType())
                .map(MediaType::toString)
                .ifPresent(contentType -> {
                    if (contentType.contains(MediaType.APPLICATION_JSON)) {
                        httpServerRequest.body()
                                .onComplete(event -> Log.debugf("Request Body: %s", event.result().toString()));
                    }
                });
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        final int status = responseContext.getStatus();
        final String method = requestContext.getMethod();
        final String path = uriInfo.getPath();

        Log.infof("Response %s %s %s", status, method, path);
        Log.debugf("Response Headers: %s", responseContext.getHeaders());
        Optional.ofNullable(responseContext.getMediaType())
                .map(MediaType::toString)
                .ifPresent(contentType -> {
                    if (contentType.contains(MediaType.APPLICATION_JSON)) {
                        Log.debugf("Response Body: %s", responseContext.getEntity());
                    }
                });
    }

}
