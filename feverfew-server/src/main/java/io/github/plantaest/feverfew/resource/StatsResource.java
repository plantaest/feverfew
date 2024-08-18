package io.github.plantaest.feverfew.resource;

import io.github.plantaest.feverfew.config.exception.AppError;
import io.github.plantaest.feverfew.dto.common.AppResponse;
import io.github.plantaest.feverfew.dto.response.GetAllStatsResponse;
import io.github.plantaest.feverfew.service.StatsService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
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

@Path("/api/v1/stats")
@Tag(name = "Stats")
public class StatsResource {

    @Inject
    StatsService statsService;

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseStatus(200)
    @Operation(summary = "Get all stats")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(
                            implementation = StatsResourceSchema.AppResponse$GetAllStatsResponse.class))),
            @APIResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    public AppResponse<GetAllStatsResponse> getAllStats() {
        return statsService.getAllStats();
    }

}
