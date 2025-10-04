package com.example.service;

import com.example.entity.*;
import com.example.exception.NotFoundException;
import com.example.exception.ValidationException;
import com.example.repository.MaintenanceJdbcRepository;
import com.example.repository.MaintenanceRepository;
import com.example.repository.VehicleJdbcRepository;
import com.example.repository.VehicleRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class MaintenanceService {
    private final MaintenanceRepository repo = new MaintenanceJdbcRepository();
    private final VehicleRepository vehicleRepo = new VehicleJdbcRepository();

    public Maintenance createMaintenance(Maintenance m) throws Exception {
        // validations
        Vehicle v = vehicleRepo.findById(m.getVehicleId()).orElseThrow(() -> new NotFoundException("Vehicle not found"));
        if (v.getStatus() == VehicleStatus.DECOMMISSIONED) throw new ValidationException("Vehicle is decommissioned");
        if (m.getMileageAtService() != null && m.getMileageAtService() < 0) throw new ValidationException("mileageAtService must be >= 0");
        if (m instanceof PerformedMaintenance && m.getPerformedAt() != null && m.getPerformedAt().isAfter(Instant.now())) throw new ValidationException("performedAt cannot be in the future");
        m.touchCreated();
        m.setCreatedAt(Instant.now());
        m = repo.save(m);
        // Apply business rule: update vehicle mileage if performed maintenance increased it
        if (m instanceof PerformedMaintenance) {
            m.applyToVehicle(v);
            vehicleRepo.update(v);
        }
        return m;
    }

    public Maintenance updateMaintenance(Maintenance m) throws Exception {
        Optional<Maintenance> existing = repo.findById(m.getId());
        if (!existing.isPresent()) throw new NotFoundException("Maintenance not found");
        // similar validations (simplified)
        return repo.update(m);
    }

    public void deleteMaintenance(Long id) throws Exception {
        repo.delete(id);
    }

    public List<Maintenance> getHistory(Long vehicleId) throws Exception {
        vehicleRepo.findById(vehicleId).orElseThrow(() -> new NotFoundException("Vehicle not found"));
        return repo.findByVehicleId(vehicleId);
    }

    public Maintenance getById(Long id) throws Exception {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Maintenance not found"));
    }
}
