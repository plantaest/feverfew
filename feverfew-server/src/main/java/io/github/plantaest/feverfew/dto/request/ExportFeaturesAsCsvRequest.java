package io.github.plantaest.feverfew.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ExportFeaturesAsCsvRequest(
        @NotNull
        List<String> links
) {}
