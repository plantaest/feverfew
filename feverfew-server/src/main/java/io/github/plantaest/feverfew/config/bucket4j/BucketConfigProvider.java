package io.github.plantaest.feverfew.config.bucket4j;

import io.github.bucket4j.BucketConfiguration;
import jakarta.inject.Singleton;

import java.time.Duration;
import java.util.Map;

public class BucketConfigProvider {

    @Singleton
    public Map<String, BucketConfiguration> bucketConfigurations() {
        return Map.of(
                "defaultBucket", BucketConfiguration.builder()
                        .addLimit(limit -> limit.capacity(20).refillIntervally(20, Duration.ofSeconds(1)))
                        .addLimit(limit -> limit.capacity(300).refillIntervally(300, Duration.ofMinutes(1)))
                        .build(),
                "createCheckBucket", BucketConfiguration.builder()
                        .addLimit(limit -> limit.capacity(5).refillIntervally(5, Duration.ofSeconds(1)))
                        .addLimit(limit -> limit.capacity(10).refillIntervally(10, Duration.ofMinutes(1)))
                        .addLimit(limit -> limit.capacity(100).refillIntervally(100, Duration.ofDays(1)))
                        .build()
        );
    }

}
