// package reports;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ZReport {
    private Database db;

    public ZReport(Database db) {
        this.db = db;
    }

    // Get total sales for the current day
    public double getTotalSales() {
        String query = "SELECT SUM(total_cost) AS total_sales FROM orders WHERE time_placed >= CURRENT_DATE;";
        return getSingleValue(query, "total_sales");
    }

    // Placeholder: Get total tax for the current day (easy to implement with script)
    public double getTotalTax() {
        String query = "SELECT SUM(tax_amount) AS total_tax FROM orders WHERE time_placed >= CURRENT_DATE;";
        return getSingleValue(query, "total_tax");
    }

    // Placeholder: Payment methods not tracked
    public Map<String, Double> getPaymentBreakdown() {
        return new HashMap<>();
    }

    // Placeholder: Discounts, Voids, Service Charges not tracked
    public Map<String, Double> getVoidsReturnsDiscards() {
        return new HashMap<>();
    }

    // Helper method to get a single numerical value from the database
    // testing this to improve readability of methods
    private double getSingleValue(String query, String columnName) {
        double value = 0.0;
        try {
            ResultSet rs = db.select(query);
            if (rs.next()) {
                value = rs.getDouble(columnName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
