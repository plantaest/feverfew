package io.github.plantaest.feverfew.config;

import io.quarkus.runtime.configuration.DurationConverter;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithConverter;
import io.smallrye.config.WithDefault;
import software.amazon.awssdk.regions.Region;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@ConfigMapping(prefix = "app")
public interface AppConfig {
    int currentResultSchemaVersion();

    List<String> ignoredHosts();

    int maxNonIgnoredLinks();

    String modelFilePath();

    Aws aws();

    Lambda lambda();

    RateLimiter rateLimiter();

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

    interface RateLimiter {
        @WithDefault("1000")
        int maxSize();

        @WithDefault("1H")
        @WithConverter(DurationConverter.class)
        Duration keepAfterRefill();
    }
}
