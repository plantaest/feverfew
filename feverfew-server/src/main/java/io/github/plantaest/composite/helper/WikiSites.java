package io.github.plantaest.composite.helper;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class WikiSites {
    private final List<String> wikiIds;
    private final Map<String, WikiSite> wikis;

    public WikiSites(List<WikiSite> wikis) {
        this.wikiIds = wikis.stream().map(WikiSite::wikiId).toList();
        this.wikis = wikis.stream().collect(Collectors.toConcurrentMap(WikiSite::wikiId, Function.identity()));
    }

    public List<String> getWikiIds() {
        return wikiIds;
    }

    public Map<String, WikiSite> getWikis() {
        return wikis;
    }
}
