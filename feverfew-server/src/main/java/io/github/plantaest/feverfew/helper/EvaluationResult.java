package io.github.plantaest.feverfew.helper;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;

@Builder
public record EvaluationResult(
        int index,
        ExternalLink link,
        RequestResult requestResult,
        ClassificationResult classificationResult
) {}
