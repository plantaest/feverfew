package io.github.plantaest.feverfew.helper;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import jakarta.annotation.Nullable;

import java.util.List;

@Builder
public record RequestResult(
        Type type,
        String requestUrl,
        double requestDuration,
        int responseStatus,
        @Nullable
        String contentType,
        int contentLength,
        boolean containsPageNotFoundWords,
        boolean containsPaywallWords,
        boolean containsDomainExpiredWords,
        List<Redirect> redirects
) {

    public enum Type {
        SUCCESS,
        ERROR,
        IGNORED
    }

    @Builder
    public record Redirect(
            String requestUrl,
            String location,
            int responseStatus
    ) {}

}
