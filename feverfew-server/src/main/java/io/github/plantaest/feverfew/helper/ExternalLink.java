package io.github.plantaest.feverfew.helper;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import jakarta.annotation.Nullable;

@Builder
public record ExternalLink(
        String anchorHref,
        String anchorId,
        @Nullable
        String uriScheme,
        String uriHost,
        @Nullable
        Integer uriPort,
        @Nullable
        String uriPath,
        @Nullable
        String uriQuery,
        @Nullable
        String uriFragment,
        Boolean isIPv4Host,
        Boolean isIPv6Host,
        @Nullable
        String tld,
        @Nullable
        String text,
        @Nullable
        String fileType
) {}
