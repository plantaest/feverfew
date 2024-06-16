package io.github.plantaest.composite.helper;

import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestInstance;

import java.text.MessageFormat;

public class WikiHelper {

    public static UnirestInstance createHttpClient(String userAgent) {
        UnirestInstance httpClient = Unirest.spawnInstance();

        httpClient.config()
                .addDefaultHeader("User-Agent", userAgent);

        Runtime.getRuntime().addShutdownHook(Thread.ofVirtual().unstarted(httpClient::close));

        return httpClient;
    }

    public static String createActionApiUri(String serverName) {
        // Example: https://en.wikipedia.org/w/api.php
        return MessageFormat.format("https://{0}/w/api.php", serverName);
    }

}
