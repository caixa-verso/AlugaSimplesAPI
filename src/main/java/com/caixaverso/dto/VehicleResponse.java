package com.caixaverso.dto;

import com.caixaverso.model.Vehicle;
import com.caixaverso.model.VehicleStatus;

public record VehicleResponse(
        Long Id,
        String model,
        VehicleStatus status,
        int year,
        String engine,
        String carTitle
) {
    public VehicleResponse(Vehicle vehicle) {
        this(
                vehicle.getId(),
                vehicle.getModel(),
                vehicle.getStatus(),
                vehicle.getYear(),
                vehicle.getEngine(),
                "%s %s %s".formatted(vehicle.getModel(), vehicle.getStatus(), vehicle.getYear())
        );
    }


}
