package io.github.plantaest.feverfew.lambda;

import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigMapping(prefix = "app")
public interface AppConfig {
    int connectTimeout();

    int requestTimeout();

    int connectionTtl();

    String checkIpServer();

    List<String> pageNotFoundWords();

    List<String> paywallWords();

    List<String> domainExpiredWords();

    List<String> userAgents();

    List<String> ignoredHosts();
}
