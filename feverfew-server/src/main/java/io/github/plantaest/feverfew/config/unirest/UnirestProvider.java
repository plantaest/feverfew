package io.github.plantaest.feverfew.config.unirest;

import jakarta.inject.Singleton;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestInstance;

public class UnirestProvider {

    @Singleton
    public UnirestInstance unirestInstance() {
        UnirestInstance unirestInstance = Unirest.spawnInstance();
        Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(unirestInstance::close));
        return unirestInstance;
    }

}
