package io.github.plantaest.feverfew.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import kong.unirest.core.RawResponse;
import kong.unirest.core.UnirestInstance;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class RequestLinksLambda implements RequestHandler<RequestLinksRequest, RequestLinksResponse> {

    @Inject
    AppConfig appConfig;

    @Inject
    UnirestInstance httpClient;

    @Override
    public RequestLinksResponse handleRequest(RequestLinksRequest request, Context context) {
        Log.info("Starting lambda function...");
        RequestLinksResponse.Debug debug;

        if (request.debug()) {
            String serverIp = httpClient.get(appConfig.checkIpServer()).asString().getBody();
            debug = RequestLinksResponseDebugBuilder.builder()
                    .serverIp(serverIp)
                    .build();
        } else {
            debug = null;
        }

        Map<Integer, RequestResult> requestResults = requestLinks(request.links());

        return RequestLinksResponseBuilder.builder()
                .requestResults(requestResults)
                .debug(debug)
                .build();
    }

    private Map<Integer, RequestResult> requestLinks(Map<Integer, String> links) {
        Map<Integer, RequestResult> results = new HashMap<>();
        List<Map.Entry<Integer, String>> linkList = new ArrayList<>(links.entrySet());
        Function<Map.Entry<Integer, String>, Callable<RequestResult>> executeLinkFunction = (link)
                -> () -> executeLink(link.getValue());

        if (links.isEmpty()) {
            return results;
        }

        ThreadFactory factory = Thread.ofVirtual().name("execute-link-", 0L).factory();
        try (var executor = Executors.newThreadPerTaskExecutor(factory)) {
            var tasks = linkList.stream().map(executeLinkFunction).toList();
            long startTime = System.nanoTime();
            var futures = executor.invokeAll(tasks, 25000, TimeUnit.MILLISECONDS);

            for (int i = 0; i < futures.size(); i++) {
                var future = futures.get(i);
                var link = linkList.get(i);
                RequestResult result;

                try {
                    result = future.get();
                } catch (Exception e) {
                    Log.errorf("Unable to request link: %s", link.getValue());
                    result = RequestResultBuilder.builder()
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
                } finally {
                    future.cancel(true);
                }

                results.put(link.getKey(), result);
                Log.debugf("Added request result of link (%s): %s", link.getValue(), result);
            }
        } catch (InterruptedException e) {
            Log.errorf("Cannot invoke all links: %s", e.getMessage());
        }

        return results;
    }

    private RequestResult executeLink(String link) {
        long startTime = System.nanoTime();

        try {
            var uri = new URI(link);
            var scheme = uri.getScheme();
            var host = uri.getHost();
            var path = uri.getPath();

            List<RawResponse> responses = new ArrayList<>();
            Random random = new Random();
            int nextStatus;
            String nextLink = link;
            Set<String> locations = new HashSet<>();
            boolean stop = false;

            do {
                RawResponse rawResponse = httpClient
                        .get(nextLink)
                        .cookie("session_id", UUID.randomUUID().toString())
                        .header("User-Agent", appConfig.userAgents()
                                .get(random.nextInt(appConfig.userAgents().size())))
                        .asObject(Function.identity())
                        .getBody();

                nextStatus = rawResponse.getStatus();

                var location = rawResponse.getHeaders().getFirst("Location");
                var improvedLocation = improveLocation(location, scheme, host);
                nextLink = improvedLocation;

                responses.add(rawResponse);

                if (locations.contains(improvedLocation)) {
                    stop = true;
                }
                locations.add(improvedLocation);
            } while (List.of(301, 302, 303, 307, 308).contains(nextStatus)
                    // Infinite redirect: http://csus-dspace.calstate.edu/handle/10211.3/124990
                    && !stop);

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
            Log.errorf("Unable to execute link (%s): %s", link, e.getMessage());

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
