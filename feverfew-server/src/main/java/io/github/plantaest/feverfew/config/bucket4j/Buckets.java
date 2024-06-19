package io.github.plantaest.feverfew.config.bucket4j;

import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.caffeine.CaffeineProxyManager;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.distributed.remote.RemoteBucketState;
import io.github.plantaest.feverfew.config.AppConfig;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.Optional;

@Singleton
public class Buckets {

    private ProxyManager<String> proxyManager;

    @Inject
    AppConfig appConfig;

    @Inject
    IpHashResolver ipHashResolver;

    @Inject
    Map<String, BucketConfiguration> bucketConfigurations;

    @PostConstruct
    public void init() {
        this.proxyManager = this.proxyManager();
    }

    public Bucket getBucket(String bucketName) {
        BucketConfiguration configuration = Optional.ofNullable(bucketConfigurations.get(bucketName))
                .orElseThrow(() -> new IllegalStateException("Unable to retrieve bucket configuration for name '%s'"
                        .formatted(bucketName)));
        return proxyManager.builder()
                .build(bucketName + "_" + ipHashResolver.getIdentityKey(), () -> configuration);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private ProxyManager<String> proxyManager() {
        Caffeine<String, RemoteBucketState> builder = (Caffeine) Caffeine.newBuilder()
                .maximumSize(appConfig.rateLimiter().maxSize());
        return new CaffeineProxyManager<>(builder, appConfig.rateLimiter().keepAfterRefill());
    }

}
