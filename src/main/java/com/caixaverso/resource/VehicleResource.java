package com.caixaverso.resource;

import com.caixaverso.dto.UpdateVehicleStatus;
import com.caixaverso.model.Vehicle;
import com.caixaverso.dto.VehicleRequest;
import com.caixaverso.dto.VehicleResponse;
import com.caixaverso.model.VehicleStatus;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Path("api/v1/vehicles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VehicleResource {

    private static Map<Long, Vehicle> VEHICLE = new ConcurrentHashMap<>();

    @POST()
    public Response create(
            VehicleRequest body
    ) {
        Vehicle vehicle = new Vehicle(
                body.getModel(),
                body.getStatus(),
                body.getYear(),
                body.getEngine()
        );

        VEHICLE.put(vehicle.getId(), vehicle);

        return Response.created(URI.create("api/v1/vehicles" + vehicle.getId())).build();
    }

    @GET
    public List<VehicleResponse> findAll() {
        return VEHICLE
                .values()
                .stream()
                .map(VehicleResponse::new)
                .toList();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") Long id) {

        Vehicle vehicle = VEHICLE.get(id);

        if (vehicle == null) {
            return Response.status(404).build();
        }

        VehicleResponse vehicleResponse = new VehicleResponse(vehicle);

        return Response.ok(
                        vehicleResponse
                )
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {

        Vehicle vehicle = VEHICLE.get(id);

        if (vehicle == null) {
            return Response.status(404).build();
        }

        if (vehicle.isRented()) {

            return Response.status(Response.Status.CONFLICT).build();
        }

        VEHICLE.remove(id);

        return Response.noContent().build();

    }

    @PATCH
    @Path("{id}")
    public Response changeVehicle(@PathParam("id") Long id, UpdateVehicleStatus body) {

        Vehicle vehicle = VEHICLE.get(id);

        if (vehicle == null) {
            return Response.status(404).build();
        }

        vehicle.setStatus(body.status());
        VEHICLE.put(id, vehicle);

        return Response.noContent().build();

    }

}
