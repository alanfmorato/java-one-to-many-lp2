package com.example.entity;

import java.time.Instant;

public class Vehicle extends BaseEntity {
    private String vin;
    private String plate;
    private String make;
    private String model;
    private Integer manuf_year;
    private Long currentMileage;
    private VehicleStatus status;

    public Vehicle() {}

    public String getVin() { return vin; }
    public void setVin(String vin) { this.vin = vin; }

    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public Integer getYear() { return manuf_year; }
    public void setYear(Integer manuf_year) { this.manuf_year = manuf_year; }

    public Long getCurrentMileage() { return currentMileage; }
    public void setCurrentMileage(Long currentMileage) { this.currentMileage = currentMileage; }

    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }
}
