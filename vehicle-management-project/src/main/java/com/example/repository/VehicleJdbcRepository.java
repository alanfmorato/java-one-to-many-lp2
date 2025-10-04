package com.example.repository;

import com.example.entity.Vehicle;
import com.example.entity.VehicleStatus;
import com.example.util.Database;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleJdbcRepository implements com.example.repository.VehicleRepository {

    @Override
    public Vehicle save(Vehicle v) throws Exception {
        String sql = "INSERT INTO vehicle(vin, plate, make, model, manuf_year, current_mileage, status, created_at, updated_at) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            p.setString(1, v.getVin());
            p.setString(2, v.getPlate());
            p.setString(3, v.getMake());
            p.setString(4, v.getModel());
            p.setObject(5, v.getYear());
            p.setObject(6, v.getCurrentMileage());
            p.setString(7, v.getStatus() != null ? v.getStatus().name() : null);
            p.setTimestamp(8, v.getCreatedAt() != null ? Timestamp.from(v.getCreatedAt()) : Timestamp.from(Instant.now()));
            p.setTimestamp(9, v.getUpdatedAt() != null ? Timestamp.from(v.getUpdatedAt()) : Timestamp.from(Instant.now()));
            p.executeUpdate();
            try (ResultSet rs = p.getGeneratedKeys()) {
                if (rs.next()) v.setId(rs.getLong(1));
            }
            return v;
        }
    }

    @Override
    public Vehicle update(Vehicle v) throws Exception {
        String sql = "UPDATE vehicle SET vin=?, plate=?, make=?, model=?, manuf_year=?, current_mileage=?, status=?, updated_at=? WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, v.getVin());
            p.setString(2, v.getPlate());
            p.setString(3, v.getMake());
            p.setString(4, v.getModel());
            p.setObject(5, v.getYear());
            p.setObject(6, v.getCurrentMileage());
            p.setString(7, v.getStatus() != null ? v.getStatus().name() : null);
            p.setTimestamp(8, Timestamp.from(Instant.now()));
            p.setLong(9, v.getId());
            p.executeUpdate();
            return v;
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        String sql = "DELETE FROM vehicle WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            p.executeUpdate();
        }
    }

    @Override
    public Optional<Vehicle> findById(Long id) throws Exception {
        String sql = "SELECT * FROM vehicle WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setLong(1, id);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Vehicle> findAll() throws Exception {
        String sql = "SELECT * FROM vehicle";
        List<Vehicle> l = new ArrayList<>();
        try (Connection c = Database.getConnection(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) l.add(map(rs));
        }
        return l;
    }

    @Override
    public Optional<Vehicle> findByVin(String vin) throws Exception {
        String sql = "SELECT * FROM vehicle WHERE vin=?";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, vin);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Vehicle> findByPlate(String plate) throws Exception {
        String sql = "SELECT * FROM vehicle WHERE plate=?";
        try (Connection c = Database.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, plate);
            try (ResultSet rs = p.executeQuery()) {
                if (rs.next()) return Optional.of(map(rs));
            }
        }
        return Optional.empty();
    }

    private Vehicle map(ResultSet rs) throws Exception {
        Vehicle v = new Vehicle();
        v.setId(rs.getLong("id"));
        v.setVin(rs.getString("vin"));
        v.setPlate(rs.getString("plate"));
        v.setMake(rs.getString("make"));
        v.setModel(rs.getString("model"));
        v.setYear((Integer) rs.getObject("manuf_year"));
        Object cm = rs.getObject("current_mileage");
        v.setCurrentMileage(cm != null ? ((Number)cm).longValue() : null);
        String status = rs.getString("status");
        v.setStatus(status != null ? VehicleStatus.valueOf(status) : null);
        v.touchCreated();
        v.touchUpdated();
        return v;
    }
}
