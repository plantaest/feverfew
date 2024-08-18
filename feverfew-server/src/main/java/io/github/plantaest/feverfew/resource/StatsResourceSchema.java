package io.github.plantaest.feverfew.resource;

import io.github.plantaest.feverfew.dto.common.AppResponseSchema;
import io.github.plantaest.feverfew.dto.response.GetAllStatsResponse;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public interface StatsResourceSchema {

    @Schema(name = "AppResponse<GetAllStatsResponse>")
    class AppResponse$GetAllStatsResponse extends AppResponseSchema<GetAllStatsResponse> {}

}
