package cashier;

import java.sql.*;

/*
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

*/

public class Database {
    static Connection conn = null;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://csce-315-db.engr.tamu.edu/team_phoenix_db",
                    dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } // end try catch
    }

    ResultSet select(String statement) throws SQLException {
        Statement stmt = conn.createStatement();
        // send statement to DBMS
        ResultSet result = stmt.executeQuery(statement);
        return result;
    }

    int update(String statement) throws SQLException {
        Statement stmt = conn.createStatement();
        // send statement to DBMS (throws SQLException)
        int result = stmt.executeUpdate(statement);
        return result;
    }

    public int currentOrderNumber() {
        int order_num = -1;
        try {
            ResultSet result = select("SELECT id FROM orders ORDER BY id DESC LIMIT 1");
            if (result != null && result.next()) {
                order_num = result.getInt("id") + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order_num;
    }

    public double getItemPrice(int item_id) {
        double price = -1.0;
        try {
            String statement = "SELECT * FROM menu_items WHERE id=" + item_id;
            ResultSet result = select(statement);
            result.next();
            price = result.getDouble("price");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }

    public String getItemName(int item_id) {
        String item_name = "";
        try {
            String statement = "SELECT * FROM menu_items WHERE id=" + item_id;
            ResultSet result = select(statement);
            result.next();
            item_name = result.getString("item_name");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item_name;
    }
}
