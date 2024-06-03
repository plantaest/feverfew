package io.github.plantaest.composite;

import io.github.plantaest.composite.helper.WikiHelper;
import io.github.plantaest.composite.helper.WikiSite;
import kong.unirest.core.UnirestInstance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Wikis {

    private final WikisConfig config;

    private final UnirestInstance httpClient;

    private final Map<String, Wiki> wikis;

    public Wikis(WikisConfig config) {
        this.config = config;
        this.httpClient = WikiHelper.createHttpClient(config.userAgent());
        this.wikis = new ConcurrentHashMap<>();
    }

    public static Wikis init(WikisConfig config) {
        return new Wikis(config);
    }

    public Wiki getWiki(String wikiId) {
        if (!WikiSite.getWikiIds().contains(wikiId)) {
            throw new CompositeException("Wiki " + wikiId + " is not supported");
        }

        if (wikis.containsKey(wikiId)) {
            return wikis.get(wikiId);
        }

        WikiConfig wikiConfig = WikiConfig.builder()
                .serverName(WikiSite.getWikis().get(wikiId).serverName())
                .userAgent(config.userAgent())
                .build();

        Wiki wiki = Wiki.init(wikiConfig, httpClient);
        wikis.put(wikiId, wiki);

        return wiki;
    }

}
