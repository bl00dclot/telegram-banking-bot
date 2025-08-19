package com.discryptment.db;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;


public class Database {
    private static final String DB_URL = "jdbc:duckdb:bankingbot.duckdb"; // persistent file

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(DB_URL);
    }
    public static void initSchema(){

        try (Connection conn = getConnection();
        Statement stmt = conn.createStatement()){
            // Load schema.sql
            String sql = new BufferedReader(
                    new InputStreamReader(
                            Database.class.getResourceAsStream("/schema.sql")
                    )
            ).lines().collect(Collectors.joining("\n"));
            // Split on semicolon and execute
            for(String command: sql.split(";")) {
                if(command.trim().isEmpty()) continue;
                stmt.execute(command.trim());
            }
            System.out.println("Schema initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
