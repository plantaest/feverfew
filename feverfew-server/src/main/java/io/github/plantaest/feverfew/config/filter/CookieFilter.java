package io.github.plantaest.feverfew.config.filter;

import io.github.plantaest.feverfew.helper.HashingHelper;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.MDC;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class CookieFilter implements ContainerRequestFilter, ContainerResponseFilter {

    public static final String ACTOR_ID = "feverfew_actor_id";
    private static final String ACTOR_ID_STATUS = "feverfew_actor_id_status";

    private enum ActorIdStatus {
        NEW,
        CURRENT
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        final Cookie actorIdCookie = requestContext.getCookies().get(ACTOR_ID);

        if (actorIdCookie == null) {
            String actorId = String.valueOf(HashingHelper.crc32Hash(UUID.randomUUID().toString()));
            MDC.put(ACTOR_ID, actorId);
            MDC.put(ACTOR_ID_STATUS, ActorIdStatus.NEW.name());
        } else {
            MDC.put(ACTOR_ID, actorIdCookie.getValue());
            MDC.put(ACTOR_ID_STATUS, ActorIdStatus.CURRENT.name());
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String actorId = MDC.get(ACTOR_ID);
        String actorIdStatus = MDC.get(ACTOR_ID_STATUS);

        if (ActorIdStatus.NEW.name().equals(actorIdStatus)) {
            NewCookie actorIdCookie = new NewCookie.Builder(ACTOR_ID)
                    .value(actorId)
                    .path("/")
                    .expiry(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)))
                    .maxAge((int) Duration.ofDays(30).getSeconds())
                    .secure(true)
                    .httpOnly(true)
                    .sameSite(NewCookie.SameSite.STRICT)
                    .build();
            responseContext.getHeaders().add(HttpHeaders.SET_COOKIE, actorIdCookie);
        }

        MDC.remove(ACTOR_ID);
        MDC.remove(ACTOR_ID_STATUS);
    }

}
