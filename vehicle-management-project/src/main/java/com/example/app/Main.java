package com.example.app;

import com.example.controller.VehicleController;
import com.example.controller.MaintenanceController;
import com.example.util.Database;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        Database.getConnection().close();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        VehicleController.register(server);
        MaintenanceController.register(server);
        server.start();
        System.out.println("Server started at http://localhost:8080");
    }
}
