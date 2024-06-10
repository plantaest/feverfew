package io.github.plantaest.feverfew.dto.response;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;

import java.time.Instant;

@Builder
public record GetListCheckResponse(
        String id,
        Instant createdAt,
        long createdBy,
        String wikiId,
        String wikiServerName,
        String pageTitle,
        long pageRevisionId,
        double durationInMillis,
        int totalLinks,
        int totalIgnoredLinks,
        int totalSuccessLinks,
        int totalErrorLinks,
        int totalWorkingLinks,
        int totalBrokenLinks
) {}
