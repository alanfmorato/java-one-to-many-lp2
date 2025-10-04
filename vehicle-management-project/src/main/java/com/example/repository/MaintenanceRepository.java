package com.example.repository;

import com.example.entity.Maintenance;
import java.util.List;

public interface MaintenanceRepository extends Repository<Maintenance> {
    java.util.List<Maintenance> findByVehicleId(Long vehicleId) throws Exception;
}
