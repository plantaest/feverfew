package io.github.plantaest.feverfew.config;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class Start {

    public void onStart(@Observes StartupEvent ev) {
        // For testing on startup
    }

}
