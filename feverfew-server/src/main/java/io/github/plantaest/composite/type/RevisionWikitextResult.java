package io.github.plantaest.composite.type;

public record RevisionWikitextResult(
        String title,
        long pageId,
        long revisionId,
        String wikitext
) {}
