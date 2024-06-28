package io.github.plantaest.composite;

import io.github.plantaest.composite.helper.WikiSite;

import java.util.List;

public class CompositeMain {
    public static void main(String[] args) {
        // Example 1
        WikiConfig wikiConfig = WikiConfig.builder()
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:99.0) Gecko/20100101 Firefox/99.0")
                .wikiId("enwiki")
                .serverName("en.wikipedia.org")
                .build();

        Wiki wiki = Wiki.init(wikiConfig);

        Page page = wiki.page("Pet door");

        System.out.println(page.html());

        // Example 2
        WikisConfig wikisConfig = WikisConfig.builder()
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:99.0) Gecko/20100101 Firefox/99.0")
                .wikis(List.of(
                        new WikiSite("enwiki", "en.wikipedia.org"),
                        new WikiSite("viwiki", "vi.wikipedia.org")
                ))
                .build();

        Wikis wikis = Wikis.init(wikisConfig);

        Page page2 = wikis.getWiki("viwiki").page("A");
        Page page3 = wikis.getWiki("enwiki").page("A");

        System.out.println(page2.html());
        System.out.println(page3.html());
    }
}
