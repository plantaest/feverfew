package io.github.plantaest.feverfew.config.ml;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;

import java.io.IOException;

@Startup
@Singleton
public class FeverfewNextModel {

    private byte[] instance;

    @PostConstruct
    public void init() throws IOException {
        try (var inputStream = getClass().getClassLoader()
                .getResourceAsStream("model/feverfew_next_model.onnx")) {
            if (inputStream != null) {
                instance = inputStream.readAllBytes();
            }
        }
    }

    public byte[] getInstance() {
        return instance;
    }

}
