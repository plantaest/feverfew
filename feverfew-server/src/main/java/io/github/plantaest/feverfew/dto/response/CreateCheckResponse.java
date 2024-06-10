package io.github.plantaest.feverfew.dto.response;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import io.github.plantaest.feverfew.helper.EvaluationResult;
import jakarta.annotation.Nullable;

import java.time.Instant;
import java.util.List;

@Builder
public record CreateCheckResponse(
        String id,
        Instant createdAt,
        String wikiId,
        String wikiServerName,
        String pageTitle,
        @Nullable
        Long pageRevisionId,
        double durationInMillis,
        int totalLinks,
        int totalIgnoredLinks,
        int totalSuccessLinks,
        int totalErrorLinks,
        int totalWorkingLinks,
        int totalBrokenLinks,
        List<EvaluationResult> results
) {}
