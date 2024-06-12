package io.github.plantaest.feverfew.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import kong.unirest.core.RawResponse;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestInstance;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class RequestLinksLambda implements RequestHandler<RequestLinksRequest, RequestLinksResponse> {

    @Inject
    AppConfig appConfig;

    @Override
    public RequestLinksResponse handleRequest(RequestLinksRequest request, Context context) {
        RequestLinksResponse.Debug debug;

        if (request.debug()) {
            try (var httpClient = CloseableUnirest.spawnInstance()) {
                String serverIp = httpClient.get(appConfig.checkIpServer()).asString().getBody();
                debug = RequestLinksResponseDebugBuilder.builder()
                        .serverIp(serverIp)
                        .build();
            }
        } else {
            debug = null;
        }

        return RequestLinksResponseBuilder.builder()
                .requestResults(requestLinks(request.links()))
                .debug(debug)
                .build();
    }

    private List<RequestResult> requestLinks(List<String> links) {
        List<RequestResult> results = new ArrayList<>();

        if (links.isEmpty()) {
            return results;
        }

        try (var httpClient = CloseableUnirest.spawnInstance();
             var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            httpClient.config()
                    .connectTimeout(appConfig.connectTimeout())
                    .requestTimeout(appConfig.requestTimeout())
                    .connectionTTL(appConfig.connectionTtl(), TimeUnit.MILLISECONDS)
                    .followRedirects(false)
                    .verifySsl(false);

            var tasks = links.stream().<Callable<RequestResult>>map(link -> () -> executeLink(httpClient, link)).toList();
            long startTime = System.nanoTime();
            List<Future<RequestResult>> futures = executor.invokeAll(tasks, 25000, TimeUnit.MILLISECONDS);

            for (int i = 0; i < futures.size(); i++) {
                var future = futures.get(i);
                var link = links.get(i);
                AtomicReference<RequestResult> result = new AtomicReference<>();

                try {
                    var r = future.get();
                    result.set(r);
                } catch (Exception e) {
                    Log.errorf("Unable to request link: %s", link);
                    var r = RequestResultBuilder.builder()
                            .type(RequestResult.Type.ERROR)
                            .requestDuration(TimeHelper.durationInMillis(startTime))
                            .responseStatus(0)
                            .contentType(null)
                            .contentLength(0)
                            .containsPageNotFoundWords(false)
                            .containsPaywallWords(false)
                            .containsDomainExpiredWords(false)
                            .redirects(List.of())
                            .redirectToHomepage(false)
                            .build();
                    result.set(r);
                }

                results.add(result.get());
                Log.debugf("Added request result of link [%s]: %s", link, result.get());
            }
        } catch (InterruptedException e) {
            Log.errorf("Cannot invoke all links: %s", e.getMessage());
        }

        return results;
    }

    private RequestResult executeLink(UnirestInstance httpClient, String link) {
        long startTime = System.nanoTime();

        try {
            var uri = new URI(link);
            var scheme = uri.getScheme();
            var host = uri.getHost();
            var path = uri.getPath();

            if (!List.of("http", "https").contains(scheme) ||
                    appConfig.ignoredHosts().stream().anyMatch(host::contains)) {
                return RequestResultBuilder.builder()
                        .type(RequestResult.Type.IGNORED)
                        .requestDuration(0.0)
                        .responseStatus(0)
                        .contentType(null)
                        .contentLength(0)
                        .containsPageNotFoundWords(false)
                        .containsPaywallWords(false)
                        .containsDomainExpiredWords(false)
                        .redirects(List.of())
                        .redirectToHomepage(false)
                        .build();
            }

            List<RawResponse> responses = new ArrayList<>();
            Random random = new Random();
            AtomicInteger nextStatus = new AtomicInteger();
            AtomicReference<String> nextLink = new AtomicReference<>(link);
            Set<String> locations = new HashSet<>();
            AtomicBoolean stop = new AtomicBoolean(false);

            do {
                RawResponse rawResponse = httpClient
                        .get(nextLink.get())
                        .cookie("session_id", UUID.randomUUID().toString())
                        .header("User-Agent", appConfig.userAgents()
                                .get(random.nextInt(appConfig.userAgents().size())))
                        .asObject(Function.identity())
                        .getBody();

                nextStatus.set(rawResponse.getStatus());

                var location = rawResponse.getHeaders().getFirst("Location");
                var improvedLocation = improveLocation(location, scheme, host);
                nextLink.set(improvedLocation);

                responses.add(rawResponse);

                if (locations.contains(improvedLocation)) {
                    stop.set(true);
                }
                locations.add(improvedLocation);
            } while (List.of(301, 302, 303, 307, 308).contains(nextStatus.get())
                    // Infinite redirect: http://csus-dspace.calstate.edu/handle/10211.3/124990
                    && !stop.get());

            var contentType = responses.getLast().getHeaders().getFirst("Content-Type");
            var contentLength = responses.getLast().getHeaders().getFirst("Content-Length");

            // Get text inside body tag
            int htmlLength;
            String bodyText;

            if (contentType.contains("text/html")) {
                String html = responses.getLast().getContentAsString();
                htmlLength = html.length();
                Document document = Jsoup.parse(html);
                bodyText = document.body().text().toLowerCase();
            } else {
                htmlLength = 0;
                bodyText = "";
            }

            long endTime = System.nanoTime();
            var requestDurationInMillis = TimeHelper.durationInMillis(startTime, endTime);
            var redirects = responses.stream()
                    .limit(responses.size() - 1)
                    .map((response) -> RequestResultRedirectBuilder.builder()
                            .requestUrl(response.getRequestSummary().getUrl())
                            .location(improveLocation(
                                    response.getHeaders().getFirst("Location"),
                                    scheme,
                                    host
                            ))
                            .responseStatus(response.getStatus())
                            .build())
                    .toList();

            return RequestResultBuilder.builder()
                    .type(RequestResult.Type.SUCCESS)
                    .requestDuration(requestDurationInMillis)
                    .responseStatus(responses.getLast().getStatus())
                    .contentType(contentType.isBlank() ? null : contentType)
                    .contentLength(htmlLength > 0
                            ? htmlLength
                            : contentLength.isBlank() ? 0 : Integer.parseInt(contentLength))
                    .containsPageNotFoundWords(appConfig.pageNotFoundWords().stream().anyMatch(bodyText::contains))
                    .containsPaywallWords(appConfig.paywallWords().stream().anyMatch(bodyText::contains))
                    .containsDomainExpiredWords(appConfig.domainExpiredWords().stream().anyMatch(bodyText::contains))
                    .redirects(redirects)
                    .redirectToHomepage(isRedirectToHomepage(
                            !redirects.isEmpty(),
                            path != null,
                            responses.getLast().getRequestSummary().getUrl(),
                            host
                    ))
                    .build();
        } catch (Exception e) {
            Log.errorf("Unable to execute link [%s]: %s", link, e.getMessage());

            long endTime = System.nanoTime();
            var requestDurationInMillis = TimeHelper.durationInMillis(startTime, endTime);

            return RequestResultBuilder.builder()
                    .type(RequestResult.Type.ERROR)
                    .requestDuration(requestDurationInMillis)
                    .responseStatus(0)
                    .contentType(null)
                    .contentLength(0)
                    .containsPageNotFoundWords(false)
                    .containsPaywallWords(false)
                    .containsDomainExpiredWords(false)
                    .redirects(List.of())
                    .redirectToHomepage(false)
                    .build();
        }
    }

    private String improveLocation(String location, String scheme, String host) {
        // Some locations are weird
        // For example:
        // * https://archive.org/details/sim_plant-physiology_2001-01_125_1/page/50
        // * http://www.pnas.org/content/95/13/7805.full
        if (location.startsWith("https://") || location.startsWith("http://")) {
            return location;
        } else {
            return scheme + "://" + host + (location.startsWith("/") ? location : "/" + location);
        }
    }

    private boolean isRedirectToHomepage(boolean hasRedirects, boolean hasPath, String lastRequestUrl, String host) {
        return hasRedirects
                && hasPath
                && lastRequestUrl
                .replaceAll("http://|https://", "")
                .replaceAll("/", "")
                .equalsIgnoreCase(host);
    }

}
