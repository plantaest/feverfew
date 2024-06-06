package io.github.plantaest.feverfew.helper;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import jakarta.annotation.Nullable;

import java.util.List;

@Builder
public record RequestResult(
        Type type,
        String requestUrl,
        Double requestDuration,
        Integer responseStatus,
        @Nullable
        String contentType,
        Integer contentLength,
        Boolean containsPageNotFoundWords,
        Boolean containsPaywallWords,
        Boolean containsDomainExpiredWords,
        List<Redirect> redirects,
        Boolean redirectToHomepage
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
            Integer responseStatus
    ) {}

}
