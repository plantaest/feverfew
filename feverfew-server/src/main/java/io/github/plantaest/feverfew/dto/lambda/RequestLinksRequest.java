package io.github.plantaest.feverfew.dto.lambda;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;

import java.util.List;

@Builder
public record RequestLinksRequest(
        List<String> links,
        boolean debug
) {}
