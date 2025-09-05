package com.discryptment.db;

import com.discryptment.service.AdminService;
import com.discryptment.service.AuthService;
import com.discryptment.util.EnvReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;


public class Database {
    private static final String DB_URL = "jdbc:duckdb:bankingbot.duckdb"; // persistent file
    private static final AuthService authService = new AuthService();
    private static final AdminService adminService = new AdminService();

    public static void init() throws Exception {
        findFile();
        boolean fileExists = Files.exists(Paths.get("bankingbot.duckdb"));
        try (Connection conn = getConnection()) {
            if (!fileExists) {
                System.out.println("Created DB");
            } else {
                System.out.println("Using existing DB");
            }
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL);
    }

    public static void schema() {


        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Load schema.sql
            String sql = new BufferedReader(
                    new InputStreamReader(
                            Database.class.getResourceAsStream("/schema.sql")
                    )
            ).lines().collect(Collectors.joining("\n"));
            // Split on semicolon and execute
            for (String command : sql.split(";")) {
                if (command.trim().isEmpty()) continue;
                stmt.execute(command.trim());
            }
            System.out.println("Schema initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void findFile() throws SQLException {
        EnvReader.init("src/main/resources", ".env");
        String pass = EnvReader.get("AUTH_PASS");
        long initAdminId = Long.parseLong(EnvReader.get("INIT_ADMIN"));
        File dbFile = new File("bankingbot.duckdb");
        if (!(dbFile.isFile())) {
            Database.schema();
            authService.setPassword(pass);
            authService.setRegistration(true);
            adminService.initAdmin(initAdminId);
        }
    }
}
