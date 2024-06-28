package io.github.plantaest.feverfew.resource;

import io.github.plantaest.feverfew.config.exception.AppError;
import io.github.plantaest.feverfew.dto.common.AppResponse;
import io.github.plantaest.feverfew.dto.response.GetRevisionWikitextResponse;
import io.github.plantaest.feverfew.service.WikiService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
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

@Path("/api/v1/wiki")
@Tag(name = "Wiki")
public class WikiResource {

    @Inject
    WikiService wikiService;

    @GET
    @Path("/revision/wikitext")
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseStatus(200)
    @Operation(summary = "Get wikitext of a revision")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(
                            implementation = WikiResourceSchema.AppResponse$GetRevisionWikitextResponse.class))),
            @APIResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = AppError.class))),
            @APIResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = AppError.class)))
    })
    public AppResponse<GetRevisionWikitextResponse> getRevisionWikitext(
            @QueryParam("wikiId") String wikiId,
            @QueryParam("revisionId") Long revisionId
    ) {
        return wikiService.getRevisionWikitext(wikiId, revisionId);
    }

}
