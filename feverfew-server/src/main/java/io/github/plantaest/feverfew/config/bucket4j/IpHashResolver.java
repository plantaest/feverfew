package io.github.plantaest.feverfew.config.bucket4j;

import io.github.plantaest.feverfew.helper.HashingHelper;
import io.vertx.ext.web.RoutingContext;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@RequestScoped
public class IpHashResolver {

    @Inject
    RoutingContext context;

    public String getIdentityKey() {
        return String.valueOf(HashingHelper.hashIP(context.request().remoteAddress().host()));
    }

}
