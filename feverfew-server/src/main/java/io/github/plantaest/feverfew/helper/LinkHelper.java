package io.github.plantaest.feverfew.helper;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import kong.unirest.core.RawResponse;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestInstance;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class LinkHelper {

    public List<ExternalLink> extractExternalLinks(String pageHtmlContent) {
        try {
            List<ExternalLink> externalLinks = new ArrayList<>();

            // Parsing HTML
            Document document = Jsoup.parse(pageHtmlContent);

            // Remove authority boxes
            Elements authorityBoxElements = document.select("div.navbox.authority-control");
            for (Element authorityBoxElement : authorityBoxElements) {
                authorityBoxElement.remove();
            }

            // Select external anchors
            Elements anchorElements = document.select("a[rel='mw:ExtLink nofollow']");

            for (Element anchor : anchorElements) {
                String anchorId = anchor.id();
                String anchorHref = anchor.attr("href");
                URI uri = new URI(anchorHref);

                if (anchorHref.startsWith("//")) {
                    anchorHref = "https:" + anchorHref;
                }

                var externalLink = ExternalLinkBuilder.builder()
                        .id(anchorId)
                        .href(anchorHref)
                        .scheme(uri.getScheme())
                        .host(uri.getHost())
                        .port(uri.getPort() == -1 ? null : uri.getPort())
                        .path(uri.getPath())
                        .query(uri.getQuery())
                        .fragment(uri.getFragment())
                        .isIPv4(isValidIPv4(uri.getHost()))
                        .isIPv6(isValidIPv6(uri.getHost()))
                        .tld(getTLD(uri.getHost()))
                        .text(anchor.text().isBlank() ? null : anchor.text())
                        .fileType(getExtension(uri.getPath()))
                        .build();

                externalLinks.add(externalLink);
            }

            return externalLinks;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValidIPv4(String host) {
        InetAddressValidator validator = InetAddressValidator.getInstance();
        return validator.isValidInet4Address(host);
    }

    private boolean isValidIPv6(String host) {
        InetAddressValidator validator = InetAddressValidator.getInstance();
        return validator.isValidInet6Address(host);
    }

    private String getTLD(String domain) {
        DomainValidator validator = DomainValidator.getInstance();

        if (validator.isValid(domain)) {
            String[] domainParts = domain.split("\\.");
            if (domainParts.length > 1) {
                return domainParts[domainParts.length - 1];
            }
        }

        return null;
    }

    private String getExtension(String path) {
        var extension = FilenameUtils.getExtension(path);
        return extension.isBlank() ? null : extension;
    }

    private static final List<String> PAGE_NOT_FOUND_WORDS = List.of(
            "not found",
            "not be found",
            "404",
            "did you mean",
            "return to",
            "oops",
            "not exist",
            "try again"
    );

    private static final List<String> PAYWALL_WORDS = List.of(
            "paywall",
            "unlimited",
            "miss out",
            "anytime",
            "subscription",
            "best value",
            "join today",
            "try free",
            "for free"
    );

    private static final List<String> DOMAIN_EXPIRED_WORDS = List.of(
            "expired",
            "renew",
            "reactivate",
            "new domain"
    );

    private static final List<String> USER_AGENTS = List.of(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36",

            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.75.14 (KHTML, like Gecko) Version/7.0.3 Safari/7046A194A",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 13_1) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.1 Safari/605.1.15",

            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:101.0) Gecko/20100101 Firefox/101.0"
    );

    private static final List<String> IGNORED_HOSTS = List.of(
            "web.archive.org",
            "archive.today",
            "archive.fo",
            "archive.is",
            "archive.li",
            "archive.md",
            "archive.ph",
            "archive.vn",
            "worldcat.org",
            "doi.org",
            "ncbi.nlm.nih.gov", // PMC
            "pubmed.ncbi.nlm.nih.gov", // PMID
            "ui.adsabs.harvard.edu" // Bibcode
    );

    public List<RequestResult> requestLinks(List<ExternalLink> links) {
        List<RequestResult> results = new ArrayList<>();

        UnirestInstance httpClient = Unirest.spawnInstance();
        httpClient.config()
                .connectTimeout(5000)
                .requestTimeout(8000)
                .connectionTTL(8000, TimeUnit.MICROSECONDS)
                .followRedirects(false)
                .verifySsl(false);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<RequestResult>> futures = new ArrayList<>();
            for (var link : links) {
                Future<RequestResult> future = executor.submit(() -> executeLink(httpClient, link));
                futures.add(future);
            }
            for (var future : futures) {
                results.add(future.get());
                Log.debugf("Added request result of link: %s", future.get().requestUrl());
            }
        } catch (InterruptedException | ExecutionException e) {
            Log.errorf("Unable to request links: %s", e.getMessage());
        }

        httpClient.close();

        return results;
    }

    private RequestResult executeLink(UnirestInstance httpClient, ExternalLink link) {
        var requestUrl = link.href();
        var scheme = link.scheme();
        var host = link.host();
        long startTime = System.nanoTime();

        if (IGNORED_HOSTS.stream().anyMatch(host::contains)) {
            return RequestResultBuilder.builder()
                    .type(RequestResult.Type.IGNORED)
                    .requestUrl(requestUrl)
                    .requestDuration(0)
                    .responseStatus(0)
                    .contentType(null)
                    .contentLength(0)
                    .containsPageNotFoundWords(false)
                    .containsPaywallWords(false)
                    .containsDomainExpiredWords(false)
                    .redirects(List.of())
                    .build();
        }

        try {
            List<RawResponse> responses = new ArrayList<>();
            Random random = new Random();
            AtomicInteger nextStatus = new AtomicInteger();
            AtomicReference<String> nextLink = new AtomicReference<>(requestUrl);
            Set<String> locations = new HashSet<>();
            AtomicBoolean stop = new AtomicBoolean(false);

            // Call all redirects?
            do {
                httpClient
                        .get(nextLink.get())
                        .cookie("session_id", UUID.randomUUID().toString())
                        .header("User-Agent", USER_AGENTS.get(random.nextInt(USER_AGENTS.size())))
                        .thenConsume(rawResponse -> {
                            nextStatus.set(rawResponse.getStatus());

                            var location = rawResponse.getHeaders().getFirst("Location");
                            var improvedLocation = improveLocation(location, scheme, host);
                            nextLink.set(improvedLocation);

                            responses.add(rawResponse);

                            if (locations.contains(improvedLocation)) {
                                stop.set(true);
                            }
                            locations.add(improvedLocation);
                        });
            } while (List.of(301, 302).contains(nextStatus.get())
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

            return RequestResultBuilder.builder()
                    .type(RequestResult.Type.SUCCESS)
                    .requestUrl(requestUrl)
                    .requestDuration(requestDurationInMillis)
                    .responseStatus(responses.getLast().getStatus())
                    .contentType(contentType.isBlank() ? null : contentType)
                    .contentLength(htmlLength > 0
                            ? htmlLength
                            : contentLength.isBlank() ? 0 : Integer.parseInt(contentLength))
                    .containsPageNotFoundWords(PAGE_NOT_FOUND_WORDS.stream().anyMatch(bodyText::contains))
                    .containsPaywallWords(PAYWALL_WORDS.stream().anyMatch(bodyText::contains))
                    .containsDomainExpiredWords(DOMAIN_EXPIRED_WORDS.stream().anyMatch(bodyText::contains))
                    .redirects(responses.stream()
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
                            .toList())
                    .build();
        } catch (Exception e) {
            Log.errorf("Unable to execute link [%s]: %s", requestUrl, e.getMessage());

            long endTime = System.nanoTime();
            var requestDurationInMillis = TimeHelper.durationInMillis(startTime, endTime);

            return RequestResultBuilder.builder()
                    .type(RequestResult.Type.ERROR)
                    .requestUrl(requestUrl)
                    .requestDuration(requestDurationInMillis)
                    .responseStatus(0)
                    .contentType(null)
                    .contentLength(0)
                    .containsPageNotFoundWords(false)
                    .containsPaywallWords(false)
                    .containsDomainExpiredWords(false)
                    .redirects(List.of())
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

}
