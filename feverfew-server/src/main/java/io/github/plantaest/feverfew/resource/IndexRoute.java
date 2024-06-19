package io.github.plantaest.feverfew.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/")
public class IndexRoute {

    @GET
    @Path("/favicon.ico")
    public Response favicon() {
        return Response.noContent().build();
    }

}
