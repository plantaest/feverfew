package io.github.plantaest.feverfew.helper;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;

@Builder
public record ClassificationResult(
        long label,
        float probability
) {}
