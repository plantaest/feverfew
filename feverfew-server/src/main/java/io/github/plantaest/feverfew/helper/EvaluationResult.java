package io.github.plantaest.feverfew.helper;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;

@Builder
public record EvaluationResult(
        ExternalLink link,
        RequestResult requestResult,
        ClassificationResult classificationResult
) {}
