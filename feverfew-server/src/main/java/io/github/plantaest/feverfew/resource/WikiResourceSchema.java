package io.github.plantaest.feverfew.resource;

import io.github.plantaest.feverfew.dto.common.AppResponseSchema;
import io.github.plantaest.feverfew.dto.response.GetRevisionWikitextResponse;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public interface WikiResourceSchema {

    @Schema(name = "AppResponse<GetRevisionWikitextResponse>")
    class AppResponse$GetRevisionWikitextResponse extends AppResponseSchema<GetRevisionWikitextResponse> {}

}
