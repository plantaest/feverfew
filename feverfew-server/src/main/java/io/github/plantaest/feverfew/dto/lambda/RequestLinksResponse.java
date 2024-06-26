package io.github.plantaest.feverfew.dto.lambda;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.plantaest.feverfew.config.recordbuilder.Builder;
import io.github.plantaest.feverfew.helper.RequestResult;
import jakarta.annotation.Nullable;

import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RequestLinksResponse(
        Map<Integer, RequestResult> requestResults,
        @Nullable
        Debug debug
) {

    @Builder
    public record Debug(
            String serverIp
    ) {}

}
