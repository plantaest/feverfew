package io.github.plantaest.feverfew.helper;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import jakarta.annotation.Nullable;

@Builder
public record ExternalLink(
        String id,
        String href,
        @Nullable
        String scheme,
        String host,
        @Nullable
        Integer port,
        @Nullable
        String path,
        @Nullable
        String query,
        @Nullable
        String fragment,
        Boolean isIPv4,
        Boolean isIPv6,
        @Nullable
        String tld,
        @Nullable
        String text,
        @Nullable
        String fileType
) {}
