package com.caixaverso.model;

import java.util.concurrent.atomic.AtomicLong;

public class Vehicle {

    private static final AtomicLong ATOMIC_LONG = new AtomicLong(1);
    private Long Id;
    private String model;
    private VehicleStatus status;
    private int year;
    private String engine;


    public Vehicle(String model, VehicleStatus status, int year, String engine) {
        this.Id = ATOMIC_LONG.getAndIncrement();
        this.model = model;
        this.status = VehicleStatus.AVAIABLE;
        this.year = year;
        this.engine = engine;
    }

    public Long getId() {
        return Id;
    }

    public String getModel() {
        return model;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public int getYear() {
        return year;
    }

    public String getEngine() {
        return engine;
    }

    public boolean isRented() {
        return this.getStatus().equals(VehicleStatus.RENTED);
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}
