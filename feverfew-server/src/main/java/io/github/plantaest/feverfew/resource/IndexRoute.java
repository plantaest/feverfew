package io.github.plantaest.feverfew.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/")
@Tag(name = "Index")
public class IndexRoute {

    @GET
    @Path("/favicon.ico")
    public Response favicon() {
        return Response.noContent().build();
    }

}
