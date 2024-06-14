package io.github.plantaest.feverfew.lambda;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestInstance;

import java.util.concurrent.TimeUnit;

public class UnirestProvider {

    @Inject
    AppConfig appConfig;

    @Singleton
    public UnirestInstance unirestInstance() {
        UnirestInstance unirest = Unirest.spawnInstance();
        unirest.config()
                .connectTimeout(appConfig.connectTimeout())
                .requestTimeout(appConfig.requestTimeout())
                .connectionTTL(appConfig.connectionTtl(), TimeUnit.MILLISECONDS)
                .followRedirects(false)
                .verifySsl(false);
        return unirest;
    }

}
