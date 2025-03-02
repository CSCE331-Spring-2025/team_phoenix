// Notes:
// Ideally, reports are kept in a separate folder and package. Will not work with our current implementation.
// package reports;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

// Necessary to import if in different folders
// import Database;

public class XReport {
    private Database db;

    
    // Haven't tested the connection to the database.
    public XReport(Database db) {
        this.db = db;
    }


    // Gives sales per hour for the currrent day of operation.
    public Map<String, Double> getTotalSalesPerHour() {
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


    // Placeholder for returns/voids/discards (they don't actually exist)
    public Map<String, Double> getVoidsReturnsDiscardsPerHour() {
        // Map<String, Double> data = new HashMap<>();
        // String query = """
        //     SELECT DATE_TRUNC('hour', order_time) AS hour,
        //            SUM(CASE WHEN is_void THEN total_price ELSE 0 END) AS total_voids,
        //            SUM(CASE WHEN is_return THEN total_price ELSE 0 END) AS total_returns,
        //            SUM(CASE WHEN is_discard THEN total_price ELSE 0 END) AS total_discards
        //     FROM orders WHERE order_time >= CURRENT_DATE
        //     GROUP BY hour ORDER BY hour;
        // """;

        // try {
        //     ResultSet rs = db.select(query);
        //     while (rs.next()) {
        //         String hour = rs.getTimestamp("hour").toString();
        //         data.put(hour + "_voids", rs.getDouble("total_voids"));
        //         data.put(hour + "_returns", rs.getDouble("total_returns"));
        //         data.put(hour + "_discards", rs.getDouble("total_discards"));
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // return data;
        return new HashMap<>();
    }


    // Placeholder for payment methods
    public Map<String, Double> getPaymentsPerHour() {
        // Map<String, Double> payments = new HashMap<>();
        // String query = """
        //     SELECT DATE_TRUNC('hour', orders.order_time) AS hour, 
        //            payments.payment_method, SUM(orders.total_price) AS payment_total
        //     FROM orders
        //     JOIN payments ON orders.payment_id = payments.payment_id
        //     WHERE orders.order_time >= CURRENT_DATE
        //     GROUP BY hour, payments.payment_method
        //     ORDER BY hour;
        // """;

        // try {
        //     ResultSet rs = db.select(query);
        //     while (rs.next()) {
        //         String hour = rs.getTimestamp("hour").toString();
        //         String paymentMethod = rs.getString("payment_method");
        //         payments.put(hour + "_" + paymentMethod, rs.getDouble("payment_total"));
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // return payments;
        return new HashMap<>();
    }
}