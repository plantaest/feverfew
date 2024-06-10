package io.github.plantaest.feverfew.resource;

import io.github.plantaest.feverfew.dto.common.AppResponseSchema;
import io.github.plantaest.feverfew.dto.common.ListResponse;
import io.github.plantaest.feverfew.dto.common.ListResponseSchema;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponse;
import io.github.plantaest.feverfew.dto.response.GetListCheckResponse;
import io.github.plantaest.feverfew.dto.response.GetOneCheckResponse;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public interface CheckResourceSchema {

    @Schema(name = "AppResponse<CreateCheckResponse>")
    class AppResponse$CreateCheckResponse extends AppResponseSchema<CreateCheckResponse> {}

    @Schema(name = "AppResponse<GetOneCheckResponse>")
    class AppResponse$GetOneCheckResponse extends AppResponseSchema<GetOneCheckResponse> {}

    @Schema(name = "ListResponse<GetListCheckResponse>")
    class ListResponse$GetListCheckResponse extends ListResponseSchema<GetListCheckResponse> {}

    @Schema(name = "AppResponse<ListResponse<GetListCheckResponse>>")
    class AppResponse$ListResponse$GetListCheckResponse extends AppResponseSchema<ListResponse$GetListCheckResponse> {}

}
