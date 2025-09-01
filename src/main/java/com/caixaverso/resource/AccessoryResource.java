package com.caixaverso.resource;

import com.caixaverso.model.Accessory;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("api/v1/accessories")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccessoryResource {

    public Response all(){

        return Response.ok(Accessory.listAll()).build();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") Long id) {

        Accessory accessory = Accessory.findById(id);

        if (accessory == null) {
            return Response.status(404).build();
        }

        return Response.ok(accessory).build();
    }
}