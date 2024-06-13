package io.github.plantaest.feverfew.config;

import io.smallrye.config.ConfigMapping;
import software.amazon.awssdk.regions.Region;

import java.util.List;

@ConfigMapping(prefix = "app")
public interface AppConfig {
    int currentResultSchemaVersion();

    Aws aws();

    Lambda lambda();

    interface Aws {
        String accessKeyId();

        String secretAccessKey();
    }

    interface Lambda {
        List<Region> supportedRegions();

        int maxFunctionIndex();
    }
}
