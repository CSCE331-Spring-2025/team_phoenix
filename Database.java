import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/*
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

*/

/**
 * Creates a connection to the team_phoenix database.
 * Hosts methods to run queries in the database without the user needing to know
 * or use SQL commands.
 */
public class Database {
    static Connection conn = null;

    static {
        try {
            // Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://csce-315-db.engr.tamu.edu/team_phoenix_db",
                    dbSetup.user, dbSetup.pswd);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } // end try catch
    }

    /**
     * For SQL quaries that return a table of data.
     * 
     * @param statement The query typed into the PSQL terminal.
     * @return {@code RestultSet} with the SQL quary output.
     * @throws SQLException
     */
    ResultSet select(String statement) throws SQLException {
        Statement stmt = conn.createStatement();
        // send statement to DBMS
        ResultSet result = stmt.executeQuery(statement);
        return result;
    }

    /**
     * For SQL queries that returns no data.
     * 
     * @param statement The query typed into the PSQL terminal.
     * @return {@code int} of the number of rows updated.
     * @throws SQLException
     */
    int update(String statement) throws SQLException {
        Statement stmt = conn.createStatement();
        // send statement to DBMS (throws SQLException)
        int result = stmt.executeUpdate(statement);
        return result;
    }

    // public int currentOrderNumber() {
    // int order_num = -1;
    // try {
    // ResultSet result = select("SELECT id FROM orders ORDER BY id DESC LIMIT 1");
    // if (result != null && result.next()) {
    // order_num = result.getInt("id") + 1;
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return order_num;
    // }

    /**
     * 
     * @param item_id
     * @return
     */
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

    /**
     * 
     * @param item_id
     * @return
     */
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

    /**
     * 
     * @return
     */
    public Map<String, Integer> getMenuItemNames() {
        Map<String, Integer> menuMap = new HashMap<>();
        try {
            String statement = "SELECT * FROM menu_items";
            ResultSet result = select(statement);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("item_name");

                menuMap.put(name, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuMap;
    }

    /**
     * 
     * @param supplier_id
     * @return
     */
    public String getSupplierName(int supplier_id) {
        String name = "";
        try {
            String statement = "SELECT * FROM suppliers WHERE id=" + supplier_id;
            ResultSet result = select(statement);
            while (result.next()) {
                name = result.getString("supplier_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * 
     * @return
     */
    public Map<String, Integer> getSupplierNames() {
        Map<String, Integer> supplierMap = new HashMap<>();
        try {
            String statement = "SELECT * FROM suppliers ORDER BY id ASC";
            ResultSet result = select(statement);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("supplier_name");

                supplierMap.put(name, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierMap;
    }

    /**
     * 
     * @param order_id
     * @param menu_id
     * @return {@code boolean} wheather inventory was successfully updated.
     */
    public boolean addToOrder(int order_id, int menu_id) {
        boolean added = false;
        try {
            String statement = "INSERT INTO items_in_order (order_id, menu_id) VALUES ("
                    + order_id + ", " + menu_id + ")";
            int x = update(statement);
            if (x > 0) {
                added = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return added;
    }

    // TODO: get order price

    /**
     * 
     * @param employee_id
     * @return
     */
    public int addOrder(int employee_id) {
        int orderID = -1;
        try {
            String statement = "INSERT INTO orders (employee_id) VALUES (" + employee_id + ") RETURNING id";
            ResultSet x = select(statement);
            if (x.next()) {
                orderID = x.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderID;
    }

    /**
     * @deprecated SQL trigger does this automatically.
     * 
     * @param ingredient_id
     * @return
     */
    @Deprecated
    public boolean subtractIngredient(int ingredient_id) {
        boolean success = false;
        try {
            String statement = "UPDATE inventory SET quantity = quantity - 1 WHERE id = " + ingredient_id
                    + " AND quantity > 0";
            int result = update(statement);
            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // TODO: menu_items add new item (takes- name, price)
    // TODO: menu_items delete item (takes- id)

    /**
     * 
     * @param item_id
     * @param newName
     * @return {@code boolean} wheather inventory was successfully updated.
     */
    public boolean updateItemName(int item_id, String newName) {
        boolean success = false;
        try {
            String statement = "UPDATE menu_items SET item_name = '" + newName + "' WHERE id = " + item_id;
            int result = update(statement);
            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * 
     * @param item_id
     * @param newPrice
     * @return
     */
    public boolean updateItemPrice(int item_id, double newPrice) {
        boolean success = false;
        try {
            String statement = "UPDATE menu_items SET price = " + newPrice + " WHERE id = " + item_id;
            int result = update(statement);
            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // TODO: inventory item map () {name, id, quantity, supplierID}

    /**
     * For delivery GUI to update the current {@code quantity} with the
     * {@code amount} on the truck.
     * 
     * @param inventory_id
     * @param amount       To add to current quantity.
     * @return The updated quantity.
     */
    public int addToInventory(int inventory_id, int amount) {
        int quantity = -1;
        try {
            String statement = "UPDATE inventory SET quantity = quantity + " + amount +
                    " WHERE id = " + inventory_id + " RETURNING quantity";
            ResultSet result = select(statement);
            if (result.next()) {
                quantity = result.getInt("quantity");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quantity;
    }

    /**
     * Table "public.inventory"
     * <p>
     * To update {@code quantity} to the current count from manager GUI.
     * 
     * @param inventory_id
     * @param amount       The most current phyical count of the inventory item.
     * @return {@code boolean} wheather inventory was successfully updated.
     */
    public boolean updateInventory(int inventory_id, int amount) {
        boolean success = false;
        try {
            String statement = "UPDATE inventory SET quantity = " + amount
                    + " WHERE id = " + inventory_id;
            int result = update(statement);
            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // TODO: employees add new employee
    // TODO: employees update info
    // TODO: employees change to is manager
    // TODO: employees removes

    // public boolean addToOrder(int order_id, int menu_id) {
    //     boolean added = false;
    //     try {
    //         String statement = "INSERT INTO items_in_order (order_id, menu_id) VALUES ("
    //                 + order_id + ", " + menu_id + ")";
    //         int x = update(statement);
    //         if (x > 0) {
    //             added = true;
    //         }
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }
    //     return added;
    // }

}
