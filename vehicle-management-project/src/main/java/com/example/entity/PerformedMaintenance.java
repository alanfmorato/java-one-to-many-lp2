package com.example.entity;

import java.time.Instant;

public class PerformedMaintenance extends Maintenance {

    @Override
    public boolean isFinalized() {
        return true;
    }

    @Override
    public void applyToVehicle(Vehicle v) {
        if (this.mileageAtService != null && v.getCurrentMileage() != null) {
            if (this.mileageAtService > v.getCurrentMileage()) {
                v.setCurrentMileage(this.mileageAtService);
            }
        } else if (this.mileageAtService != null) {
            v.setCurrentMileage(this.mileageAtService);
        }
        if (v.getStatus() == com.example.entity.VehicleStatus.IN_REPAIR) {
            v.setStatus(com.example.entity.VehicleStatus.ACTIVE);
        }
        v.touchUpdated();
    }
}
