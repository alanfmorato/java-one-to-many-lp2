package com.example.repository;

import com.example.entity.Vehicle;
import java.util.Optional;

public interface VehicleRepository extends Repository<Vehicle> {
    Optional<Vehicle> findByVin(String vin) throws Exception;
    Optional<Vehicle> findByPlate(String plate) throws Exception;
}
