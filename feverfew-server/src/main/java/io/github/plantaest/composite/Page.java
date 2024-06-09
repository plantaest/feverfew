package io.github.plantaest.composite;

import io.github.plantaest.composite.type.PageHtmlResult;

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

    public PageHtmlResult html() {
        var response = wiki.httpClient()
                .get(wiki.actionApiUri())
                .queryString("action", "parse")
                .queryString("format", "json")
                .queryString("formatversion", 2)
                .queryString("page", this.title)
                .queryString("parsoid", true)
                .asJson()
                .getBody()
                .getObject()
                .getJSONObject("parse");

        return new PageHtmlResult(
                response.getString("title"),
                response.getLong("pageid"),
                response.getLong("revid"),
                response.getString("text")
        );
    }

}
