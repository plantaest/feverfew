package io.github.plantaest.feverfew.config.lambda;

import io.github.plantaest.feverfew.config.AppConfig;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;

public class LambdaClientProvider {

    @Inject
    AppConfig appConfig;

    @Produces
    @Named("lambdaClientUsWest2")
    public LambdaClient lambdaClientUsWest2() {
        AwsCredentials credentials = AwsBasicCredentials
                .create(appConfig.aws().accessKeyId(), appConfig.aws().secretAccessKey());
        return LambdaClient
                .builder()
                .region(Region.US_WEST_2)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

}
