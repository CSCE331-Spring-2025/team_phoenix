// package reports;

// Includes methods for various information that can be used to generate X/Z reports

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Reports {
    private Database db;


    // Uses existing Database instance, need to test if working properly.
    public Reports(Database db) {
        this.db = db;
    }


    public Map<String, String> getMerchantInfo() {
        Map<String, String> merchantInfo = new HashMap<>();
        merchantInfo.put("Store Name", "Boba Tea Store");
        merchantInfo.put("Store Address", "123 Boba Street, Galveston, TX");
        merchantInfo.put("Terminal ID", "POS-01");
        merchantInfo.put("Register ID", "Reg-07");
        return merchantInfo;
    }


    public String getManagerName() {
        // id = 1 is the manager, Nathan Lee
        String query = "SELECT first_name, last_name FROM employees WHERE id = 1;";
        String managerName = "Unknown Manager"; // default 

        try {
            ResultSet rs = db.select(query);
            if (rs.next()) {
                managerName = rs.getString("first_name") + " " + rs.getString("last_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return managerName;
    }


    // X-REPORT: Gives sales per hour for the currrent day of operation. Can be run anytime.
    public Map<String, Double> getSalesPerHour() {
        Map<String, Double> salesPerHour = new HashMap<>();
        String query = """
            SELECT DATE_TRUNC('hour', time_placed) AS hour, SUM(total_cost) AS total_sales
            FROM orders
            WHERE time_placed >= CURRENT_DATE
            GROUP BY hour
            ORDER BY hour;
        """;

        try {
            ResultSet rs = db.select(query);
            while (rs.next()) {
                salesPerHour.put(rs.getTimestamp("hour").toString(), rs.getDouble("total_sales"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return salesPerHour;
    }


    // Z-REPORT: Get total sales for the current day. Run once at end of day.
    public double getTotalSales() {
        String query = "SELECT SUM(total_cost) AS total_sales FROM orders WHERE time_placed >= CURRENT_DATE;";
        return getSingleValue(query, "total_sales");
    }


    // Placeholder: Get total tax for the current day (trivial to implement)
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

    
    // Helper method when getting a *single* numerical value from the database
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
