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

    public static final RequestResult IGNORED = RequestResultBuilder.builder()
            .type(RequestResult.Type.IGNORED)
            .requestDuration(-1)
            .responseStatus(-1)
            .contentType(null)
            .contentLength(-1)
            .containsPageNotFoundWords(-1)
            .containsPaywallWords(-1)
            .containsDomainExpiredWords(-1)
            .redirects(List.of())
            .redirectToHomepage(false)
            .simpleRedirect(false)
            .timeout(false)
            .fileType(RequestResult.FileType.NONE)
            .build();

}
