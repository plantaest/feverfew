package io.github.plantaest.feverfew.config.wikis;

import io.github.plantaest.composite.Wikis;
import io.github.plantaest.composite.WikisConfig;
import io.github.plantaest.composite.helper.WikiSite;
import jakarta.inject.Singleton;

import java.util.List;

public class WikisInstance {

    @Singleton
    public Wikis wikis() {
        List<WikiSite> wikiSites = List.of(
                new WikiSite("enwiki", "en.wikipedia.org"),
                new WikiSite("eswiki", "es.wikipedia.org"),
                new WikiSite("viwiki", "vi.wikipedia.org")
        );

        WikisConfig wikisConfig = WikisConfig.builder()
                // TODO: Change to a proper User-Agent
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:99.0) Gecko/20100101 Firefox/99.0")
                .wikis(wikiSites)
                .build();

        return Wikis.init(wikisConfig);
    }

}
