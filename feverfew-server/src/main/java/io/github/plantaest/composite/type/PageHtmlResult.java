package io.github.plantaest.composite.type;

public record PageHtmlResult(
        String title,
        long pageId,
        long revisionId,
        String html
) {}
