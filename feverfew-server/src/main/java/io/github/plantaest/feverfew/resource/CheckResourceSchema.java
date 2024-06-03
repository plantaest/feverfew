package io.github.plantaest.feverfew.resource;

import io.github.plantaest.feverfew.dto.common.AppResponseSchema;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponse;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public interface CheckResourceSchema {

    @Schema(name = "AppResponse<CreateCheckResponse>")
    class AppResponse$CreateCheckResponse extends AppResponseSchema<CreateCheckResponse> {}

}
