package io.github.plantaest.feverfew.helper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import jakarta.annotation.Nullable;

import java.util.List;

public record EvaluationResultSchemaV1(
        @JsonProperty("i")
        int index,
        @JsonProperty("l")
        ExternalLink link,
        @JsonProperty("r")
        RequestResult requestResult,
        @JsonProperty("c")
        ClassificationResult classificationResult
) {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ExternalLink(
            @JsonProperty("i")
            String id,
            @JsonProperty("f")
            String href,
            @Nullable
            @JsonProperty("s")
            String scheme,
            @JsonProperty("h")
            String host,
            @Nullable
            @JsonProperty("p")
            Integer port,
            @Nullable
            @JsonProperty("a")
            String path,
            @Nullable
            @JsonProperty("q")
            String query,
            @Nullable
            @JsonProperty("r")
            String fragment,
            @JsonProperty("4")
            boolean isIPv4,
            @JsonProperty("6")
            boolean isIPv6,
            @Nullable
            @JsonProperty("l")
            String tld,
            @Nullable
            @JsonProperty("t")
            String text,
            @Nullable
            @JsonProperty("y")
            String fileType,
            @Nullable
            @JsonProperty("x")
            Integer refIndex,
            @Nullable
            @JsonProperty("e")
            String refName
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record RequestResult(
            @JsonProperty("t")
            Type type,
            @JsonProperty("d")
            double requestDuration,
            @JsonProperty("s")
            int responseStatus,
            @Nullable
            @JsonProperty("c")
            String contentType,
            @JsonProperty("l")
            int contentLength,
            @JsonProperty("f")
            int containsPageNotFoundWords,
            @JsonProperty("p")
            int containsPaywallWords,
            @JsonProperty("e")
            int containsDomainExpiredWords,
            @JsonProperty("r")
            List<Redirect> redirects,
            @JsonProperty("h")
            boolean redirectToHomepage,
            @JsonProperty("i")
            boolean simpleRedirect,
            @JsonProperty("o")
            boolean timeout,
            @JsonProperty("y")
            FileType fileType
    ) {

        public enum Type {
            @JsonProperty("S")
            SUCCESS,
            @JsonProperty("E")
            ERROR,
            @JsonProperty("I")
            IGNORED
        }

        public enum FileType {
            @JsonProperty("H")
            HTML,
            @JsonProperty("X")
            XML,
            @JsonProperty("P")
            PDF,
            @JsonProperty("U")
            UNKNOWN,
            @JsonProperty("N")
            NONE
        }

        @Builder
        public record Redirect(
                @JsonProperty("r")
                String requestUrl,
                @JsonProperty("l")
                String location,
                @JsonProperty("s")
                int responseStatus
        ) {}

    }

    public record ClassificationResult(
            @JsonProperty("l")
            long label,
            @JsonProperty("p")
            float probability
    ) {}

}
