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
        int containsPageNotFoundWords,
        int containsPaywallWords,
        int containsDomainExpiredWords,
        List<Redirect> redirects,
        boolean redirectToHomepage,
        boolean simpleRedirect,
        boolean timeout,
        FileType fileType
) {

    public enum Type {
        SUCCESS,
        ERROR,
        IGNORED
    }

    public enum FileType {
        HTML,
        XML,
        PDF,
        UNKNOWN,
        NONE
    }

    @Builder
    public record Redirect(
            String requestUrl,
            String location,
            int responseStatus
    ) {}

}
