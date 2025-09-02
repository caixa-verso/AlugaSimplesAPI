package com.caixaverso.resource;

import com.caixaverso.dto.*;
import com.caixaverso.model.Accessory;
import com.caixaverso.model.Maintenance;
import com.caixaverso.model.Vehicle;
import io.quarkus.panache.common.Parameters;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("api/v1/vehicles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VehicleResource {


    @POST
    @Transactional
    @RolesAllowed({"admin", "user"})
    public Response create(CreateVehicleRequest request) {

        Vehicle vehicle = new Vehicle(request.model(), request.year(), request.engine(), request.brand());

        vehicle.persist();

        VehicleResponse response = new VehicleResponse(vehicle);

        return Response.created(URI.create("api/v1/vehicles/" + vehicle.getId())).entity(response).build();
    }

    @GET
    public List<VehicleResponse> findAll() {

        List<Vehicle> vehicles = Vehicle.listAll();

        return vehicles.stream().map(VehicleResponse::new).toList();
    }

    @GET
    @Path("{id}")
    public Response findById(@PathParam("id") Long id) {

        Optional<Vehicle> vehicle = Vehicle.findByIdOptional(id);
        if (vehicle.isPresent()) {

            return Response.ok(new VehicleResponse(vehicle.get())).build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(Map.of("message", "Vehicle with ID not found"))
                    .build();
        }
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    public Response deleteById(@PathParam("id") Long id) {
        Vehicle.deleteById(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("{id}")
    @Transactional
    public Response updateStatusPartially(@PathParam("id") Long id, UpdateVehicleStatus request) {

        Vehicle vehicle = Vehicle.findById(id);
        if (vehicle != null) {

            try {
                vehicle.changeStatus(request.status());
            } catch (IllegalStateException e) {
                return Response.status(Response.Status.CONFLICT).build();
            }
            return Response.ok(new VehicleResponse(vehicle)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Path("/{vehicleId}/maintenances")
    @Transactional
    public Response addMaintenance(@PathParam("vehicleId") Long vehicleId, CreateMaintenanceRequest request) {

        Optional<Vehicle> possibleVehicle = Vehicle.findByIdOptional(vehicleId);

        if (possibleVehicle.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Vehicle vehicle = possibleVehicle.get();
        Maintenance maintenance = new Maintenance(
                request.problem(),
                vehicleId
        );

        maintenance.persist();

        vehicle.moveForMaintenance(maintenance);

        return Response.created(URI.create("/api/v1/vehicles/%d/maintenances/%d".formatted(
                vehicleId, maintenance.getId()
        ))).build();
    }

    @GET
    @Path("/{vehicleId}/maintenances/{maintenanceId}")
    public Response getMaintenanceById(@PathParam("vehicleId") Long vehicleId, @PathParam("maintenanceId") Long maintenanceId) {

        Maintenance maintenance = Maintenance.find("vehicleId = :vehicleId AND id = :maintenanceId", Parameters.with(
                "vehicleId", vehicleId).and("maintenanceId", maintenanceId
        )).firstResult();

        if (maintenance == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(maintenance).build();
        }
    }

    @PUT
    @Transactional
    @Path("/{id}/accessories")
    public Response addAccessory(@PathParam("id") Long id, AddAccessoryRequest request) {

        Vehicle vehicle = Vehicle.findById(id);

        if (vehicle == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Accessory accessory = new Accessory(request.name());

        accessory.persist();

        vehicle.addAccessory(accessory);

        return Response.noContent().build();
    }
}
