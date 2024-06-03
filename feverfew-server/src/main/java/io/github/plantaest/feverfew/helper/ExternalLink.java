package io.github.plantaest.feverfew.helper;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import jakarta.annotation.Nullable;

@Builder
public record ExternalLink(
        String anchorHref,
        String anchorId,
        UriSpec uriSpec,
        Boolean isIPv4Host,
        Boolean isIPv6Host,
        @Nullable
        String tld,
        @Nullable
        String cleanedText,
        @Nullable
        String fileType,
        @Nullable
        String sectionTitle,
        @Nullable
        String sectionTitleId,
        @Nullable
        String nearestLevelTwoTitle,
        @Nullable
        String nearestLevelTwoTitleId
) {

    @Builder
    record UriSpec(
            @Nullable
            String scheme,
            @Nullable
            String userinfo,
            String host,
            @Nullable
            String port,
            String authority,
            @Nullable
            String path,
            @Nullable
            String query,
            @Nullable
            String fragment
    ) {}

}
