package io.github.plantaest.composite;

import io.github.plantaest.composite.helper.WikiHelper;
import io.github.plantaest.composite.helper.WikiSite;
import io.github.plantaest.composite.helper.WikiSites;
import kong.unirest.core.UnirestInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Wikis {
    private final WikisConfig config;
    private final Map<String, Wiki> wikis;
    private final WikiSites wikiSites;
    private final UnirestInstance httpClient;

    public Wikis(WikisConfig config) {
        this.config = config;
        this.wikis = new ConcurrentHashMap<>();
        this.wikiSites = new WikiSites(config.wikis());
        this.httpClient = WikiHelper.createHttpClient(config.userAgent());
    }

    public static Wikis init(WikisConfig config) {
        return new Wikis(config);
    }

    public WikiSites getWikiSites() {
        return wikiSites;
    }

    public Wiki getWiki(String wikiId) {
        if (!wikiSites.getWikiIds().contains(wikiId)) {
            throw new CompositeException("Wiki " + wikiId + " is not supported");
        }

        if (wikis.containsKey(wikiId)) {
            return wikis.get(wikiId);
        }

        WikiConfig wikiConfig = WikiConfig.builder()
                .userAgent(config.userAgent())
                .wikiId(wikiId)
                .serverName(wikiSites.getWikis().get(wikiId).serverName())
                .build();

        Wiki wiki = Wiki.init(wikiConfig, httpClient);
        wikis.put(wikiId, wiki);

        return wiki;
    }
}
