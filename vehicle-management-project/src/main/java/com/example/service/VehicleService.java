package com.example.service;

import com.example.entity.Vehicle;
import com.example.entity.VehicleStatus;
import com.example.exception.NotFoundException;
import com.example.exception.ValidationException;
import com.example.repository.VehicleRepository;
import com.example.repository.VehicleJdbcRepository;

import java.time.Year;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class VehicleService {
    private final VehicleRepository repo = new VehicleJdbcRepository();

    public Vehicle createVehicle(Vehicle v) throws Exception {
        validate(v, true);
        v.touchCreated();
        v.touchUpdated();
        return repo.save(v);
    }

    public Vehicle updateVehicle(Long id, Vehicle v) throws Exception {
        Optional<Vehicle> existing = repo.findById(id);
        if (!existing.isPresent()) throw new NotFoundException("Vehicle not found");
        v.setId(id);
        validate(v, false);
        v.touchUpdated();
        return repo.update(v);
    }

    public void deleteVehicle(Long id) throws Exception {
        repo.delete(id);
    }

    public Vehicle getById(Long id) throws Exception {
        return repo.findById(id).orElseThrow(() -> new NotFoundException("Vehicle not found"));
    }

    public List<Vehicle> listAll() throws Exception {
        return repo.findAll();
    }

    public List<Vehicle> listByStatus(VehicleStatus status) throws Exception {
        List<Vehicle> all = repo.findAll();
        return all.stream().filter(v -> v.getStatus()==status).toList();
    }

    private void validate(Vehicle v, boolean creating) throws Exception {
        if (v.getVin() == null || v.getVin().length() != 17) throw new ValidationException("VIN must be 17 chars");
        if (v.getPlate() == null || v.getPlate().isBlank()) throw new ValidationException("Plate required");
        if (v.getYear() == null || v.getYear() < 1900 || v.getYear() > Year.now().getValue()) throw new ValidationException("Invalid year");
        // Unique checks
        Optional<Vehicle> byVin = repo.findByVin(v.getVin());
        if (byVin.isPresent() && (creating || !byVin.get().getId().equals(v.getId()))) throw new ValidationException("VIN already exists");
        Optional<Vehicle> byPlate = repo.findByPlate(v.getPlate());
        if (byPlate.isPresent() && (creating || !byPlate.get().getId().equals(v.getId()))) throw new ValidationException("Plate already exists");
        if (v.getCurrentMileage() != null && v.getCurrentMileage() < 0) throw new ValidationException("currentMileage must be >= 0");
        if (v.getStatus() == null) v.setStatus(VehicleStatus.ACTIVE);
    }
}
