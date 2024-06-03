package io.github.plantaest.composite;

public class Page {

    private final Wiki wiki;

    private final String title;

    public Page(Wiki wiki, String title) {
        this.wiki = wiki;
        this.title = title;
    }

    public String title() {
        return title;
    }

    public String text() {
        return wiki.httpClient()
                .get(wiki.actionApiUri())
                .queryString("action", "parse")
                .queryString("format", "json")
                .queryString("formatversion", 2)
                .queryString("page", this.title)
                .queryString("prop", "wikitext")
                .asJson()
                .getBody()
                .getObject()
                .getJSONObject("parse")
                .getString("wikitext");
    }

    public String html() {
        return wiki.httpClient()
                .get(wiki.actionApiUri())
                .queryString("action", "parse")
                .queryString("format", "json")
                .queryString("formatversion", 2)
                .queryString("page", this.title)
                .queryString("prop", "text")
                .queryString("parsoid", true)
                .asJson()
                .getBody()
                .getObject()
                .getJSONObject("parse")
                .getString("text");
    }

}
