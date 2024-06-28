package io.github.plantaest.feverfew.dto.response;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;

@Builder
public record GetRevisionWikitextResponse(
        String title,
        long pageId,
        long revisionId,
        String wikitext
) {}
