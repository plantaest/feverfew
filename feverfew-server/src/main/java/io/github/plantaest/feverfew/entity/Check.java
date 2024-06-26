package io.github.plantaest.feverfew.entity;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import jakarta.annotation.Nullable;

import java.time.Instant;

@Builder
public record Check(
        long id,
        Instant createdAt,
        long createdBy,
        String wikiId,
        String pageTitle,
        long pageRevisionId,
        double durationInMillis,
        int totalLinks,
        int totalIgnoredLinks,
        int totalSuccessLinks,
        int totalErrorLinks,
        int totalWorkingLinks,
        int totalBrokenLinks,
        int resultSchemaVersion,
        @Nullable
        byte[] results
) {}
