package com.example.controller;

import com.example.entity.Vehicle;
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

public class VehicleController {
    private static final VehicleService service = new VehicleService();
    private static final ObjectMapper M = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
            .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static void register(HttpServer server) {
        server.createContext("/vehicles", VehicleController::handleVehicles);
    }

    private static void handleVehicles(HttpExchange ex) {
        try {
            String method = ex.getRequestMethod();
            URI uri = ex.getRequestURI();
            String path = uri.getPath();
            if ("GET".equals(method) && path.equals("/vehicles")) {
                List<Vehicle> all = service.listAll();
                sendJson(ex, 200, all);
                return;
            }
            if ("POST".equals(method) && path.equals("/vehicles")) {
                InputStream is = ex.getRequestBody();
                Vehicle v = M.readValue(is, Vehicle.class);
                Vehicle created = service.createVehicle(v);
                sendJson(ex, 201, created);
                return;
            }
            // path /vehicles/{id}
            String[] parts = path.split("/");
            if (parts.length==3) {
                Long id = Long.parseLong(parts[2]);
                if ("GET".equals(method)) {
                    sendJson(ex, 200, service.getById(id));
                    return;
                } else if ("PUT".equals(method)) {
                    Vehicle v = M.readValue(ex.getRequestBody(), Vehicle.class);
                    sendJson(ex, 200, service.updateVehicle(id, v));
                    return;
                } else if ("DELETE".equals(method)) {
                    service.deleteVehicle(id);
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
