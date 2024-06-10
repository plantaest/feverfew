package io.github.plantaest.feverfew.entity;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;

import java.time.Instant;

@Builder
public record Check(
        Long id,
        Instant createdAt,
        String wikiId,
        String pageTitle,
        Long pageRevisionId
) {}
