package io.github.plantaest.feverfew.config;

import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class Start {

    @Inject
    AppConfig appConfig;

    public void onStart(@Observes StartupEvent ev) {
        // For testing on startup
        Log.debugf("Application config: %s", appConfig);
    }

}
