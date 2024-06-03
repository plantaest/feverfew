package io.github.plantaest.feverfew.dto.response;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;

import java.time.Instant;

@Builder
public record CreateCheckResponse(
        String id,
        Instant createdAt,
        Instant updatedAt,
        Integer totalLinks
) {}
