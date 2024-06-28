package io.github.plantaest.composite;

import io.github.plantaest.composite.type.RevisionWikitextResult;

public class Revision {

    private final Wiki wiki;
    private final long id;

    public Revision(Wiki wiki, long id) {
        this.wiki = wiki;
        this.id = id;
    }

    public long id() {
        return id;
    }

    public RevisionWikitextResult wikitext() {
        var response = wiki.httpClient()
                .get(wiki.actionApiUri())
                .queryString("action", "parse")
                .queryString("format", "json")
                .queryString("formatversion", 2)
                .queryString("oldid", id)
                .queryString("prop", "wikitext")
                .asJson()
                .getBody()
                .getObject()
                .getJSONObject("parse");

        return new RevisionWikitextResult(
                response.getString("title"),
                response.getLong("pageid"),
                response.getLong("revid"),
                response.getString("wikitext")
        );
    }

}
