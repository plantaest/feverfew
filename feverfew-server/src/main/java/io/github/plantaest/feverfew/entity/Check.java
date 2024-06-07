package io.github.plantaest.feverfew.entity;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import io.github.plantaest.feverfew.enumeration.CheckStatus;
import jakarta.annotation.Nullable;

import java.time.Instant;

@Builder
public record Check(
        Long id,
        Instant createdAt,
        Instant updatedAt,
        @Nullable
        Long createdBy,
        @Nullable
        Long updatedBy,
        String wikiId,
        String pageTitle,
        Long pageRevisionId
) {}
