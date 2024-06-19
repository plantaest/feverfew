package io.github.plantaest.feverfew.resource;

import io.github.plantaest.feverfew.config.bucket4j.RateLimit;
import io.github.plantaest.feverfew.config.exception.AppError;
import io.github.plantaest.feverfew.dto.common.AppResponse;
import io.github.plantaest.feverfew.dto.common.ListResponse;
import io.github.plantaest.feverfew.dto.request.CreateCheckRequest;
import io.github.plantaest.feverfew.dto.request.ExportFeaturesAsCsvRequest;
import io.github.plantaest.feverfew.dto.response.CreateCheckResponse;
import io.github.plantaest.feverfew.dto.response.GetListCheckResponse;
import io.github.plantaest.feverfew.dto.response.GetOneCheckResponse;
import io.github.plantaest.feverfew.service.CheckService;
import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.ResponseStatus;

@Path("/api/v1/check")
@Tag(name = "Check")
public class CheckResource {

    @Inject
    CheckService checkService;

    @POST
    @Path(("/create"))
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
    @RateLimit(bucket = "createCheckBucket")
    public AppResponse<CreateCheckResponse> createCheck(@Valid CreateCheckRequest request) {
        return checkService.createCheck(request);
    }

    @POST
    @Path("/export/features/csv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @ResponseStatus(200)
    @Operation(summary = "Export link features in CSV format (private API)")
    @RateLimit(bucket = "defaultBucket")
    public String exportFeaturesAsCsv(@Valid ExportFeaturesAsCsvRequest request) {
        if (ConfigUtils.isProfileActive("prod")) {
            throw new ForbiddenException("You are not allowed to export features in prod profile");
        }

        return checkService.exportFeaturesAsCsv(request);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseStatus(200)
    @Operation(summary = "Get a check")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(
                            implementation = CheckResourceSchema.AppResponse$GetOneCheckResponse.class))),
            @APIResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    @RateLimit(bucket = "defaultBucket")
    public AppResponse<GetOneCheckResponse> getOneCheck(@PathParam("id") Long id) {
        return checkService.getOneCheck(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseStatus(200)
    @Operation(summary = "Get a list of checks")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(
                            implementation = CheckResourceSchema.AppResponse$ListResponse$GetListCheckResponse.class))),
            @APIResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    @RateLimit(bucket = "defaultBucket")
    public AppResponse<ListResponse<GetListCheckResponse>> getListCheck(
            @QueryParam("page") @DefaultValue("1") Integer page,
            @QueryParam("size") @DefaultValue("5") Integer size
    ) {
        return checkService.getListCheck(page, size);
    }

}
