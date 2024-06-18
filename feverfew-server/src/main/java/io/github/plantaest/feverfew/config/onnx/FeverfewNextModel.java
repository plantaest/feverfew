package io.github.plantaest.feverfew.config.onnx;

import io.github.plantaest.feverfew.config.AppConfig;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.IOException;

@Startup
@Singleton
public class FeverfewNextModel {

    private byte[] instance;

    @Inject
    AppConfig appConfig;

    @PostConstruct
    public void init() throws IOException {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream(appConfig.modelFilePath())) {
            if (inputStream != null) {
                instance = inputStream.readAllBytes();
            }
        }
    }

    public byte[] getInstance() {
        return instance;
    }

}
