package io.github.plantaest.feverfew.helper;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LinkHelper {

    public List<ExternalLink> extractExternalLinks(String pageHtmlContent) {
        List<ExternalLink> externalLinks = new ArrayList<>();

        // TODO: Implement this function

        return externalLinks;
    }

}
