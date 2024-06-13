package io.github.plantaest.feverfew.config.lambda;

import io.github.plantaest.feverfew.config.AppConfig;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;

import java.time.Duration;

public class LambdaClientProvider {

    @Inject
    AppConfig appConfig;

    @Produces
    @Named("lambdaClientUsWest2")
    public LambdaClient lambdaClientUsWest2() {
        return LambdaClient
                .builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCredentials()))
                .httpClientBuilder(apacheHttpClientBuilder())
                .overrideConfiguration(clientOverrideConfiguration())
                .build();
    }

    private AwsCredentials awsCredentials() {
        return AwsBasicCredentials
                .create(appConfig.aws().accessKeyId(), appConfig.aws().secretAccessKey());
    }

    private ApacheHttpClient.Builder apacheHttpClientBuilder() {
        return ApacheHttpClient.builder()
                .maxConnections(200)
                .connectionTimeout(Duration.ofSeconds(10))
                .socketTimeout(Duration.ofSeconds(60));
    }

    private ClientOverrideConfiguration clientOverrideConfiguration() {
        return ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofMinutes(2))
                .apiCallAttemptTimeout(Duration.ofMinutes(1))
                .build();
    }

}
