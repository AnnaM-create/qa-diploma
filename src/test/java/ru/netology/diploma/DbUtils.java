package ru.netology.diploma;

import java.sql.*;

public class DbUtils {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/app?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false";
    private static final String dbUser = "app";
    private static final String dbPass = "pass";

    public static void clearTables() throws SQLException {
        String deleteOrders = "DELETE FROM order_entity;";
        String deletePayments = "DELETE FROM payment_entity;";
        String deleteCredits = "DELETE FROM credit_request_entity;";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(deleteOrders);
            stmt.executeUpdate(deletePayments);
            stmt.executeUpdate(deleteCredits);
        }
    }

    public static int countOrders() throws SQLException {
        String query = "SELECT COUNT(*) FROM order_entity;";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public static boolean isCardNumberSaved(String cardNumber) throws SQLException {

        String queryPayment = "SELECT COUNT(*) FROM payment_entity WHERE status = ?;";
        String queryCredit = "SELECT COUNT(*) FROM credit_request_entity WHERE status = ?;";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
            DatabaseMetaData metaData = conn.getMetaData();
            try (ResultSet columns = metaData.getColumns(null, null, "payment_entity", null)) {
                while (columns.next()) {
                    String columnName = columns.getString("COLUMN_NAME");
                    if (columnName.toLowerCase().contains("card") || columnName.toLowerCase().contains("number")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getCreditStatus() throws SQLException {
        String status = null;
        String query = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1;";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                status = rs.getString("status");
            }
        }
        return status;
    }

    public static String getPaymentStatus() throws SQLException {
        String status = null;
        String query = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1;";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                status = rs.getString("status");
            }
        }
        return status;
    }
}
