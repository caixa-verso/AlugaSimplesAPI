package com.caixaverso.dto;

import com.caixaverso.model.Accessory;
import com.caixaverso.model.Vehicle;
import com.caixaverso.model.VehicleStatus;

import java.util.Set;
import java.util.stream.Collectors;

public record VehicleResponse(
        Long Id,
        String brand,
        String model,
        int year,
        String engine,
        VehicleStatus status,
        String carTitle,
        Set<String> accessories
) {

    public VehicleResponse(Vehicle vehicle) {
        this(
                vehicle.getId(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getEngine(),
                vehicle.getStatus(),
                "%s %s %s".formatted(vehicle.getModel(), vehicle.getStatus(), vehicle.getYear()),
                vehicle.getAccessories().stream().map(Accessory::getName).collect(Collectors.toSet())
        );
    }
}
