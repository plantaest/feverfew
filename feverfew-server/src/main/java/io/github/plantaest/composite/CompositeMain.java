package io.github.plantaest.composite;

public class CompositeMain {
    public static void main(String[] args) {
        // Example 1
        WikiConfig wikiConfig = WikiConfig.builder()
                .serverName("en.wikipedia.org")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:99.0) Gecko/20100101 Firefox/99.0")
                .build();

        Wiki wiki = Wiki.init(wikiConfig);

        Page page = wiki.page("Pet door");

        System.out.println(page.html());

        // Example 2
        WikisConfig wikisConfig = WikisConfig.builder()
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:99.0) Gecko/20100101 Firefox/99.0")
                .build();

        Wikis wikis = Wikis.init(wikisConfig);

        Page page2 = wikis.getWiki("viwiki").page("A");
        Page page3 = wikis.getWiki("enwiki").page("A");

        System.out.println(page2.text());
        System.out.println(page3.text());
    }
}
