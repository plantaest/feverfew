package io.github.plantaest.feverfew.resource;

import io.github.plantaest.feverfew.config.exception.AppError;
import io.github.plantaest.feverfew.dto.common.AppResponse;
import io.github.plantaest.feverfew.dto.request.CreateCheckRequest;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponse;
import io.github.plantaest.feverfew.service.CheckService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.ResponseStatus;

@Path("/api/v1/checks")
@Tag(name = "Check")
public class CheckResource {

    @Inject
    CheckService checkService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseStatus(201)
    @Operation(summary = "Create a check")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Created",
                    content = @Content(schema = @Schema(
                            implementation = CheckResourceSchema.AppResponse$CreateCheckResponse.class))),
            @APIResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    public AppResponse<CreateCheckResponse> createCheck(@Valid CreateCheckRequest request) {
        return checkService.createCheck(request);
    }

}
