package Connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteConnector {
    private static final String DATABASE_URL = "jdbc:sqlite:RetailHub.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    /**
     * Initializes the database by creating tables if they do not already exist.
     */
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            String sqlProducts = "CREATE TABLE IF NOT EXISTS Products (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "sellPrice REAL NOT NULL," +
                    "purchasePrice REAL NOT NULL," +
                    "stock INTEGER NOT NULL," +
                    "category TEXT NOT NULL" +
                    ");";
            stmt.execute(sqlProducts);

            String sqlCustomers = "CREATE TABLE IF NOT EXISTS Customers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "email TEXT NOT NULL UNIQUE," +
                    "phone TEXT NOT NULL UNIQUE," +
                    "gender TEXT NOT NULL," +
                    "age INTEGER NOT NULL," +
                    "loyaltyPoints INTEGER NOT NULL DEFAULT 0" +
                    ");";
            stmt.execute(sqlCustomers);

            System.out.println("Database initialized successfully: Tables created or already exist.");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}