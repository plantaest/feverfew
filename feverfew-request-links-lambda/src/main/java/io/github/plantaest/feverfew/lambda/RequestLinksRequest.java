package io.github.plantaest.feverfew.lambda;

import java.util.Map;

public record RequestLinksRequest(
        Map<Integer, String> links,
        boolean debug
) {}
