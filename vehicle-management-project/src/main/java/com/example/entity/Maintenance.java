package com.example.entity;

import java.math.BigDecimal;
import java.time.Instant;

public abstract class Maintenance extends BaseEntity {
    protected Long vehicleId;
    protected MaintenanceType type;
    protected Instant performedAt;
    protected Long mileageAtService;
    protected String notes;
    protected BigDecimal cost;
    protected String createdBy;

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public MaintenanceType getType() { return type; }
    public void setType(MaintenanceType type) { this.type = type; }

    public Instant getPerformedAt() { return performedAt; }
    public void setPerformedAt(Instant performedAt) { this.performedAt = performedAt; }

    public Long getMileageAtService() { return mileageAtService; }
    public void setMileageAtService(Long mileageAtService) { this.mileageAtService = mileageAtService; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public abstract boolean isFinalized();
    public abstract void applyToVehicle(Vehicle v);
}
