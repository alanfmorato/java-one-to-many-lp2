package com.example.controller;

import com.example.entity.*;
import com.example.service.MaintenanceService;
import com.example.service.VehicleService;
import com.example.exception.ValidationException;
import com.example.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

public class MaintenanceController {
    private static final MaintenanceService service = new MaintenanceService();
    private static final ObjectMapper M = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static void register(HttpServer server) {
        server.createContext("/maintenance", MaintenanceController::handleMaintenance);
    }

    private static void handleMaintenance(HttpExchange ex) {
        try {
            String method = ex.getRequestMethod();
            URI uri = ex.getRequestURI();
            String path = uri.getPath();
            String query = uri.getQuery();
            if ("GET".equals(method) && path.equals("/maintenance") && query!=null && query.startsWith("vehicleId=")) {
                Long vid = Long.parseLong(query.substring("vehicleId=".length()));
                List<Maintenance> list = service.getHistory(vid);
                sendJson(ex, 200, list);
                return;
            }
            if ("POST".equals(method) && path.equals("/maintenance")) {
                InputStream is = ex.getRequestBody();
                java.util.Map m = M.readValue(is, java.util.Map.class);
                Maintenance ma;
                String subtype = (String) m.get("subtype");
                m.remove("subtype");
                if ("PERFORMED".equalsIgnoreCase(subtype))
                    ma = M.convertValue(m, PerformedMaintenance.class);
                else
                    ma = M.convertValue(m, ScheduledMaintenance.class);          
                Maintenance created = service.createMaintenance(ma);
                sendJson(ex, 201, created);
                return;
            }
            // /maintenance/{id}
            String[] parts = path.split("/");
            if (parts.length==3) {
                Long id = Long.parseLong(parts[2]);
                if ("GET".equals(method)) {
                    sendJson(ex, 200, service.getById(id));
                    return;
                } else if ("PUT".equals(method)) {
                    InputStream is = ex.getRequestBody();
                    java.util.Map m = M.readValue(is, java.util.Map.class);
                    Maintenance ma;
                    String subtype = (String) m.get("subtype");
                    m.remove("subtype");

                    if ("PERFORMED".equalsIgnoreCase(subtype))
                        ma = M.convertValue(m, PerformedMaintenance.class);
                    else
                        ma = M.convertValue(m, ScheduledMaintenance.class);

                    ma.setId(id);
                    sendJson(ex, 200, service.updateMaintenance(ma));
                    return;
                } else if ("DELETE".equals(method)) {
                    service.deleteMaintenance(id);
                    sendJson(ex, 204, Map.of());
                    return;
                }
            }
            sendJson(ex, 404, Map.of("error","not found"));
        } catch (ValidationException ve) {
            sendJson(ex, 400, Map.of("error", ve.getMessage()));
        } catch (NotFoundException ne) {
            sendJson(ex, 404, Map.of("error", ne.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            sendJson(ex, 500, Map.of("error", e.getMessage()));
        }
    }

    private static void sendJson(HttpExchange ex, int status, Object body) {
        try {
            byte[] b = M.writeValueAsBytes(body);
            ex.getResponseHeaders().add("Content-Type", "application/json");
            ex.sendResponseHeaders(status, b.length);
            OutputStream os = ex.getResponseBody();
            os.write(b);
            os.close();
        } catch (Exception e) { e.printStackTrace(); }
    }
}
