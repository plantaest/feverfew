package io.github.plantaest.feverfew.dto.lambda;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;

import java.util.Map;

@Builder
public record RequestLinksRequest(
        Map<Integer, String> links,
        boolean debug
) {}
