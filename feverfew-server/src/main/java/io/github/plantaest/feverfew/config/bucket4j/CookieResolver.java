package io.github.plantaest.feverfew.config.bucket4j;

import io.github.plantaest.feverfew.config.filter.CookieFilter;
import jakarta.enterprise.context.RequestScoped;
import org.slf4j.MDC;

@RequestScoped
public class CookieResolver {

    public String getIdentityKey() {
        return MDC.get(CookieFilter.ACTOR_ID);
    }

}
