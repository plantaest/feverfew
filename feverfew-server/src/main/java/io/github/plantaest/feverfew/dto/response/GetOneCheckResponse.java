package io.github.plantaest.feverfew.dto.response;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import io.github.plantaest.feverfew.helper.EvaluationResult;

import java.time.Instant;
import java.util.List;

@Builder
public record GetOneCheckResponse(
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
        int totalBrokenLinks,
        List<EvaluationResult> results
) {}
