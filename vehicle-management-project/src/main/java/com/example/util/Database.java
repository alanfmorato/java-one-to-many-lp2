package com.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:h2:./data/vehicledb;AUTO_SERVER=TRUE";
    
    // O bloco static {} é executado na primeira vez que a classe Database é carregada,
    // garantindo que as tabelas sejam criadas antes do uso.
    static {
        try {
            // Garante que o driver H2 esteja carregado
            Class.forName("org.h2.Driver");
            
            // Tenta criar as tabelas
            try (Connection c = getConnection(); Statement s = c.createStatement()) {
                
                // Vehicles
                // Corrigido: 'year' foi renomeado para 'manuf_year' para evitar o erro de palavra reservada no H2.
                s.executeUpdate("CREATE TABLE IF NOT EXISTS vehicle (" +
                        "id IDENTITY PRIMARY KEY, " +
                        "vin VARCHAR(17) NOT NULL UNIQUE, " +
                        "plate VARCHAR(50) NOT NULL UNIQUE, " +
                        "make VARCHAR(100), " +
                        "model VARCHAR(100), " +
                        "manuf_year INT, " + 
                        "current_mileage BIGINT, " +
                        "status VARCHAR(50), " +
                        "created_at TIMESTAMP, " +
                        "updated_at TIMESTAMP" +
                        ")");
                
                // Maintenance
                s.executeUpdate("CREATE TABLE IF NOT EXISTS maintenance (" +
                        "id IDENTITY PRIMARY KEY, " +
                        "vehicle_id BIGINT, " +
                        "type VARCHAR(50), " +
                        "performed_at TIMESTAMP, " +
                        "mileage_at_service BIGINT, " +
                        "notes VARCHAR(1000), " +
                        "cost DECIMAL(15,2), " +
                        "created_by VARCHAR(100), " +
                        "created_at TIMESTAMP, " +
                        "subtype VARCHAR(50)," +
                        "FOREIGN KEY (vehicle_id) REFERENCES vehicle(id) ON DELETE CASCADE" +
                        ")");
            }
        } catch (Exception e) {
            // Em caso de falha na inicialização do banco de dados, lança uma RuntimeException
            throw new RuntimeException(e);
        }
    }

    /**
     * Retorna uma nova conexão com o banco de dados H2.
     * @return Connection
     * @throws Exception se a conexão falhar.
     */
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, "sa", "");
    }
}
