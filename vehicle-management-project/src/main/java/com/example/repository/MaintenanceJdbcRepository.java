package com.example.repository;

import com.example.entity.*;
import com.example.util.Database;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaintenanceJdbcRepository implements MaintenanceRepository {

    @Override
    public Maintenance save(Maintenance m) throws Exception {
        String sql = "INSERT INTO maintenance(vehicle_id, type, performed_at, mileage_at_service, notes, cost, created_by, created_at, subtype) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setObject(1, m.getVehicleId());
            p.setString(2, m.getType() != null ? m.getType().name() : null);
            p.setTimestamp(3, m.getPerformedAt() != null ? Timestamp.from(m.getPerformedAt()) : null);
            p.setObject(4, m.getMileageAtService());
            p.setString(5, m.getNotes());
            p.setObject(6, m.getCost());
            p.setString(7, m.getCreatedBy());
            p.setTimestamp(8, m.getCreatedAt() != null ? Timestamp.from(m.getCreatedAt()) : Timestamp.from(Instant.now()));
            p.setString(9, m instanceof PerformedMaintenance ? "PERFORMED" : "SCHEDULED");
            p.executeUpdate();
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) m.setId(rs.getLong(1));
            }
            return m;
        }
    }

    @Override
    public Maintenance update(Maintenance m) throws Exception {
        String sql = "UPDATE maintenance SET vehicle_id=?, type=?, performed_at=?, mileage_at_service=?, notes=?, cost=?, created_by=? WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setObject(1, m.getVehicleId());
            p.setString(2, m.getType() != null ? m.getType().name() : null);
            p.setTimestamp(3, m.getPerformedAt() != null ? Timestamp.from(m.getPerformedAt()) : null);
            p.setObject(4, m.getMileageAtService());
            p.setString(5, m.getNotes());
            p.setObject(6, m.getCost());
            p.setString(7, m.getCreatedBy());
            p.setLong(8, m.getId());
            p.executeUpdate();
            return m;
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        String sql = "DELETE FROM maintenance WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }

    @Override
    public Optional<Maintenance> findById(Long id) throws Exception {
        String sql = "SELECT * FROM maintenance WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Maintenance> findAll() throws Exception {
        String sql = "SELECT * FROM maintenance";
        List<Maintenance> l = new ArrayList<>();
        try (Connection c = Database.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) l.add(map(rs));
        }
        return l;
    }

    @Override
    public List<Maintenance> findByVehicleId(Long vehicleId) throws Exception {
        String sql = "SELECT * FROM maintenance WHERE vehicle_id=?";
        List<Maintenance> l = new ArrayList<>();
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, vehicleId);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) l.add(map(rs));
            }
        }
        return l;
    }

    private Maintenance map(ResultSet rs) throws Exception {
        String subtype = rs.getString("subtype");
        Maintenance m = "PERFORMED".equals(subtype) ? new PerformedMaintenance() : new ScheduledMaintenance();
        m.setId(rs.getLong("id"));
        m.setVehicleId((Long) rs.getObject("vehicle_id"));
        String t = rs.getString("type");
        m.setType(t != null ? MaintenanceType.valueOf(t) : null);
        Timestamp perf = rs.getTimestamp("performed_at");
        m.setPerformedAt(perf != null ? perf.toInstant() : null);
        Object ms = rs.getObject("mileage_at_service");
        m.setMileageAtService(ms != null ? ((Number)ms).longValue() : null);
        m.setNotes(rs.getString("notes"));
        m.setCost((java.math.BigDecimal) rs.getObject("cost"));
        m.setCreatedBy(rs.getString("created_by"));
        return m;
    }
}
