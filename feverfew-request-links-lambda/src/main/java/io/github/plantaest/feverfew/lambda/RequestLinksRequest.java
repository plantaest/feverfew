package io.github.plantaest.feverfew.lambda;

import java.util.List;

public record RequestLinksRequest(
        List<String> links,
        boolean debug
) {}
