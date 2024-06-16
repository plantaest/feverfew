package io.github.plantaest.feverfew.lambda;

import com.fasterxml.jackson.annotation.JsonInclude;
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
