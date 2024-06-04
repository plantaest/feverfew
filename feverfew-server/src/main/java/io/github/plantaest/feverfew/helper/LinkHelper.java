package io.github.plantaest.feverfew.helper;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LinkHelper {

    public List<ExternalLink> extractExternalLinks(String pageHtmlContent) {
        try {
            List<ExternalLink> externalLinks = new ArrayList<>();

            Document document = Jsoup.parse(pageHtmlContent);
            Elements anchorElements = document.select("a[rel='mw:ExtLink nofollow']");

            for (Element anchor : anchorElements) {
                String anchorId = anchor.id();
                String anchorHref = anchor.attr("href");
                URI uri = new URI(anchorHref);

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

    public boolean isValidIPv4(String host) {
        InetAddressValidator validator = InetAddressValidator.getInstance();
        return validator.isValidInet4Address(host);
    }

    public boolean isValidIPv6(String host) {
        InetAddressValidator validator = InetAddressValidator.getInstance();
        return validator.isValidInet6Address(host);
    }

    public String getTLD(String domain) {
        DomainValidator validator = DomainValidator.getInstance();

        if (validator.isValid(domain)) {
            String[] domainParts = domain.split("\\.");
            if (domainParts.length > 1) {
                return domainParts[domainParts.length - 1];
            }
        }

        return null;
    }

    public String getExtension(String path) {
        var extension = FilenameUtils.getExtension(path);
        return extension.isBlank() ? null : extension;
    }

}
