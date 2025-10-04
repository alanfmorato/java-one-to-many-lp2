package com.example.dto;

import com.example.entity.MaintenanceType;
import java.math.BigDecimal;
import java.time.Instant;

public class MaintenanceDTO {
    public Long vehicleId;
    public MaintenanceType type;
    public Instant performedAt;
    public Long mileageAtService;
    public String notes;
    public BigDecimal cost;
    public String createdBy;
    public String subtype; // PERFORMED or SCHEDULED
}
