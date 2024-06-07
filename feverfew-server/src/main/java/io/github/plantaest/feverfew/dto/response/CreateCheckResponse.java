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
        Instant updatedAt,
        String wikiId,
        String pageTitle,
        @Nullable
        Long pageRevisionId,
        int totalLinks,
        List<EvaluationResult> results
) {}
