package io.github.plantaest.feverfew.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.plantaest.feverfew.config.AppConfig;
import io.github.plantaest.feverfew.dto.lambda.RequestLinksRequestBuilder;
import io.github.plantaest.feverfew.dto.lambda.RequestLinksResponse;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class LinkHelper {

    @Inject
    AppConfig appConfig;

    @Inject
    @Named("lambdaClientUsWest2")
    LambdaClient lambdaClientUsWest2;

    @Inject
    ObjectMapper objectMapper;

    public List<ExternalLink> extractExternalLinks(String pageHtmlContent) {
        try {
            List<ExternalLink> externalLinks = new ArrayList<>();

            // Parsing HTML
            Document document = Jsoup.parse(pageHtmlContent);

            // Remove authority boxes
            Elements navigationDivs = document.select("div[role='navigation']");
            for (Element navigationDiv : navigationDivs) {
                navigationDiv.remove();
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

    public List<RequestResult> requestLinks(List<String> links) {
        if (links.isEmpty()) {
            return List.of();
        }

        try {
            Random random = new Random();
            Region selectedRegion = appConfig.lambda().supportedRegions()
                    .get(random.nextInt(appConfig.lambda().supportedRegions().size()));
            int selectedFunctionIndex = random.nextInt(appConfig.lambda().maxFunctionIndex()) + 1;

            String functionName = "FeverfewRequestLinksLambda_Native_%s_%s"
                    .formatted(selectedRegion.id(), selectedFunctionIndex);
            var requestLinksRequest = RequestLinksRequestBuilder.builder()
                    .links(links)
                    .debug(false)
                    .build();
            String requestPayload = objectMapper.writeValueAsString(requestLinksRequest);

            InvokeRequest invokeRequest = InvokeRequest.builder()
                    .functionName(functionName)
                    .payload(SdkBytes.fromUtf8String(requestPayload))
                    .build();
            LambdaClient lambdaClient = getLambdaClientFromRegion(selectedRegion);

            Log.infof("Lambda function '%s' invoked for checking %s link(s)", functionName, links.size());
            InvokeResponse invokeResponse = lambdaClient.invoke(invokeRequest);
            String responsePayload = invokeResponse.payload().asUtf8String();

            if (invokeResponse.functionError() != null) {
                throw new RuntimeException("Lambda function '%s' returned an error response: %s"
                        .formatted(functionName, responsePayload));
            }

            var requestLinksResponse = objectMapper.readValue(responsePayload, RequestLinksResponse.class);

            return requestLinksResponse.requestResults();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<RequestResult> requestLinksMocking(List<String> links) {
        if (links.isEmpty()) {
            return List.of();
        }

        try (var httpClient = CloseableUnirest.spawnInstance()) {
            var requestLinksRequest = RequestLinksRequestBuilder.builder()
                    .links(links)
                    .debug(false)
                    .build();
            String response = httpClient.post(appConfig.lambda().mockServer())
                    .body(requestLinksRequest)
                    .asString()
                    .getBody();
            var requestLinksResponse = objectMapper.readValue(response, RequestLinksResponse.class);
            return requestLinksResponse.requestResults();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<RequestResult> requestExternalLinks(List<ExternalLink> links) {
        return requestLinks(links.stream().map(ExternalLink::href).toList());
    }

    public List<RequestResult> requestExternalLinksMocking(List<ExternalLink> links) {
        return requestLinksMocking(links.stream().map(ExternalLink::href).toList());
    }

    private LambdaClient getLambdaClientFromRegion(Region region) {
        if (Region.US_WEST_2.id().equals(region.id())) {
            return lambdaClientUsWest2;
        } else {
            throw new IllegalStateException("Unsupported AWS region: " + region.id());
        }
    }

}
