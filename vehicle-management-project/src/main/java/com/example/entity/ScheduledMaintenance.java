package com.example.entity;

public class ScheduledMaintenance extends Maintenance {
    @Override
    public boolean isFinalized() {
        return false;
    }

    @Override
    public void applyToVehicle(Vehicle v) {
        // Scheduled maintenance does not apply changes until performed.
    }
}
