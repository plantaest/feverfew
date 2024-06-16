package io.github.plantaest.feverfew.helper;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
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

    public static final RequestResult IGNORED = RequestResultBuilder.builder()
            .type(RequestResult.Type.IGNORED)
            .requestDuration(0.0)
            .responseStatus(0)
            .contentType(null)
            .contentLength(0)
            .containsPageNotFoundWords(false)
            .containsPaywallWords(false)
            .containsDomainExpiredWords(false)
            .redirects(List.of())
            .redirectToHomepage(false)
            .build();

}
