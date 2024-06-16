package io.github.plantaest.feverfew.config;

import io.smallrye.config.ConfigMapping;
import software.amazon.awssdk.regions.Region;

import java.util.List;
import java.util.Map;

@ConfigMapping(prefix = "app")
public interface AppConfig {
    int currentResultSchemaVersion();

    List<String> ignoredHosts();

    Aws aws();

    Lambda lambda();

    String toString();

    interface Aws {
        String accessKeyId();

        String secretAccessKey();
    }

    interface Lambda {
        String mockServer();

        int maxConsecutiveInvocations();

        int batchSize();

        List<Region> supportedRegions();

        Map<Region, List<String>> functionNames();

        String toString();
    }
}
