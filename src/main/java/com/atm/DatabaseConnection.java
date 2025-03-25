
/*  
 Developer: @Mertcan  
 Week: 7  
 Version: 3.1.0  

 Feature Overview:  
 This functionality establishes a connection to a PostgreSQL database using JDBC.  
 - It retrieves database credentials and attempts to connect.  
 - If successful, it prints a confirmation message.  
 - The connection can be used to execute SQL queries.  
 - Closes the connection once operations are completed.  
*/

package com.atm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseConnection {
    // Load dotenv
    private static final Dotenv dotenv = Dotenv.load();

    // Database credentials getting from .env
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the PostgreSQL JDBC driver (may not be necessary to new java versions)
            Class.forName("org.postgresql.Driver");

            // Establish the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the PostgreSQL database successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
        return connection;
    }

    // Keeping the DB connection for further use
    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                // Example: Run a simple query before closing the connection
                var stmt = conn.createStatement();
                var rs = stmt.executeQuery("SELECT version();"); // Get PostgreSQL version

                if (rs.next()) {
                    System.out.println("PostgreSQL version: " + rs.getString(1));
                }

                // Don't close immediately if you need to keep it open
                System.out.println("Keeping connection open for further use...");

                // conn.close(); // Uncomment this only when you're done with the connection
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
