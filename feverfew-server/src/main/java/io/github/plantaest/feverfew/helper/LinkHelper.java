package io.github.plantaest.feverfew.helper;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import kong.unirest.core.RawResponse;
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
import java.util.Optional;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            // For extracting refIndex, refName
            String regex = "cite_note-((?<refName>.+)-|)(?<refIndex>\\d+)";
            Pattern pattern = Pattern.compile(regex);

            for (Element anchor : anchorElements) {
                String anchorId = anchor.id();
                String rawAnchorHref = anchor.attr("href");
                String anchorHref = rawAnchorHref.startsWith("//") ? "https:" + rawAnchorHref : rawAnchorHref;
                URI uri = new URI(anchorHref);

                if (!List.of("http", "https").contains(uri.getScheme())) {
                    continue;
                }

                Element li = findNearestCiteNoteLi(anchor);
                Integer refIndex = null;
                String refName = null;

                if (li != null) {
                    Matcher matcher = pattern.matcher(li.id());
                    if (matcher.matches()) {
                        refIndex = Optional.ofNullable(matcher.group("refIndex"))
                                .map(Integer::parseInt)
                                .orElse(null);
                        refName = Optional.ofNullable(matcher.group("refName"))
                                .map(rn -> rn.replaceAll("_", " "))
                                .orElse(null);
                    }
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
                        .refIndex(refIndex)
                        .refName(refName)
                        .build();

                externalLinks.add(externalLink);
            }

            return externalLinks;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Element findNearestCiteNoteLi(Element anchor) {
        Element parent = anchor.parent();
        while (parent != null && !parent.tagName().equals("section")) {
            if (parent.tagName().equals("li") && parent.id().startsWith("cite_note")) {
                return parent;
            }
            parent = parent.parent();
        }
        return null;
    }

    private boolean isValidIPv4(String host) {
        InetAddressValidator validator = InetAddressValidator.getInstance();
        return validator.isValidInet4Address(host);
    }

    private boolean isValidIPv6(String host) {
        InetAddressValidator validator = InetAddressValidator.getInstance();
        return validator.isValidInet6Address(host);
    }

    private String getTLD(String host) {
        DomainValidator validator = DomainValidator.getInstance();

        if (validator.isValid(host)) {
            String[] domainParts = host.split("\\.");
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
            "try again",
            "something went wrong"
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
            "for free",
            "become a subscriber",
            "already a subscriber",
            "subscribers only",
            "subscribe now"
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

    public List<RequestResult> requestLinks(List<String> links) {
        List<RequestResult> results = new ArrayList<>();

        try (var httpClient = CloseableUnirest.spawnInstance();
             var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            httpClient.config()
                    .connectTimeout(12000)
                    .requestTimeout(25000)
                    .connectionTTL(25000, TimeUnit.MILLISECONDS)
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

    public List<RequestResult> requestExternalLinks(List<ExternalLink> links) {
        return requestLinks(links.stream().map(ExternalLink::href).toList());
    }

    private RequestResult executeLink(UnirestInstance httpClient, String link) {
        long startTime = System.nanoTime();

        try {
            var uri = new URI(link);
            var scheme = uri.getScheme();
            var host = uri.getHost();
            var path = uri.getPath();

            if (!List.of("http", "https").contains(scheme) || IGNORED_HOSTS.stream().anyMatch(host::contains)) {
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
                    .containsPageNotFoundWords(PAGE_NOT_FOUND_WORDS.stream().anyMatch(bodyText::contains))
                    .containsPaywallWords(PAYWALL_WORDS.stream().anyMatch(bodyText::contains))
                    .containsDomainExpiredWords(DOMAIN_EXPIRED_WORDS.stream().anyMatch(bodyText::contains))
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
