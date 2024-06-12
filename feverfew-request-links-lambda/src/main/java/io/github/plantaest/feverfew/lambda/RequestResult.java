package io.github.plantaest.feverfew.lambda;

import jakarta.annotation.Nullable;

import java.util.List;

@Builder
public record RequestResult(
        Type type,
        double requestDuration,
        int responseStatus,
        @Nullable
        String contentType,
        int contentLength,
        boolean containsPageNotFoundWords,
        boolean containsPaywallWords,
        boolean containsDomainExpiredWords,
        List<Redirect> redirects,
        boolean redirectToHomepage
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
