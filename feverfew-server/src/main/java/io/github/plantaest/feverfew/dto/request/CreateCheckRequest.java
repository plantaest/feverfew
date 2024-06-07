package io.github.plantaest.feverfew.dto.request;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Builder
public record CreateCheckRequest(
        @NotBlank(message = "{wikiId.required}")
        String wikiId,
        @NotBlank(message = "{pageTitle.required}")
        String pageTitle,
        @Nullable
        @Schema(nullable = true)
        Long pageRevisionId
) {}
