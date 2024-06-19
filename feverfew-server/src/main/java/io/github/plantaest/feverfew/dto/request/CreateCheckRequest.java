package io.github.plantaest.feverfew.dto.request;

import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Builder
public record CreateCheckRequest(
        @NotBlank(message = "{wikiId.required}")
        @Size(min = 1, max = 20)
        String wikiId,
        @NotBlank(message = "{pageTitle.required}")
        @Size(min = 1, max = 255)
        String pageTitle,
        @Nullable
        @Schema(nullable = true)
        Long pageRevisionId
) {}
