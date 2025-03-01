import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/*
        try {
            String statement = "";
            ResultSet result = select(statement);
            if (result.next()) {
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

*/

/**
 * Creates a connection to the team_phoenix database.
 * <p>
 * Hosts methods to run queries in the database without the user needing to know
 * or use SQL commands.
 * <p>
 * 
 * <p>
 * ===========================
 * TABLE OF CONTENTS
 * ===========================
 * <ol>
 * <li>
 * Orders
 * <ul>
 * <li>{@link #createNewOrder(int)}
 * <li>{@link #addToOrder(int, int)}
 * <li>{@link #getOrderSubtotal(int)}
 * </ul>
 * 
 * <li>
 * Menu Items
 * <ul>
 * <li>{@link #getMenuItemNames()}
 * <li>{@link #getItemName(int)}
 * <li>{@link #getItemPrice(int)}
 * <li>{@link #updateItemName(int, String)}
 * <li>{@link #updateItemPrice(int, double)}
 * <li>{@link #addMenuItem(String, double)}
 * <li>{@link #addIngredientsToItem(int, int)}
 * <li>{@link #removeFromItem(int, int)}
 * </ul>
 * 
 * <li>
 * Inventory
 * <ul>
 * <li>{@link #addToInventory(int, int)}
 * <li>{@link #updateInventory(int, int)}
 * </ul>
 * 
 * <li>
 * Suppliers
 * <ul>
 * <li>{@link #getSupplierNames()}
 * <li>{@link #getSupplierName(int)}
 * </ul>
 * 
 * <li>
 * Employees
 * <ul>
 * <li>{@link }
 * </ul>
 * </ol>
 */
public class Database {
    static Connection conn = null;

    // TODO: close db
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
    private ResultSet select(String statement) throws SQLException {
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
    private int update(String statement) throws SQLException {
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
    // order_num = result.getInt("id");
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return order_num;
    // }

    /**
     * Pull an item's price from menu items table.
     * 
     * @param item_id ~
     * @return A {@code double} of the item's price or {@code -1} if item id not
     *         found.
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
     * Pull an item name from menu items table.
     * 
     * @param item_id ~
     * @return A {@code String} of the item's name or empty {@code String} if item
     *         id not found.
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

    // TODO: amount of menu items

    /**
     * Pull all item names from menu items table.
     * 
     * @return A {@code Map} containing all menu item id mapped to their
     *         respective names.
     */
    public Map<Integer, String> getMenuItemNames() {
        Map<Integer, String> menuMap = new HashMap<>();
        try {
            String statement = "SELECT * FROM menu_items";
            ResultSet result = select(statement);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("item_name");

                menuMap.put(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return menuMap;
    }

    /**
     * Change item name in menu items table.
     * 
     * @param item_id ~
     * @param newName ~
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
     * Change item price in menu items table.
     * 
     * @param item_id  ~
     * @param newPrice ~
     * @return {@code boolean} wheather inventory was successfully updated.
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

    /**
     * Add a new item to menu items table.
     * 
     * @param name  ~
     * @param price ~
     * @return The new menu item id.
     */
    public int addMenuItem(String name, double price) {
        int id = -1;
        try {
            String statement = "INSERT INTO menu_items (item_name, price) VALUES ('"
                    + name + "', " + price + ") RETURNING id";
            ResultSet result = select(statement);
            if (result.next()) {
                id = result.getInt(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * Add an ingredient to a menu item.
     * 
     * @param menu_id      ~
     * @param inventory_id ~
     * @return {@code boolean} wheather inventory was successfully updated.
     */
    public boolean addIngredientsToItem(int menu_id, int inventory_id) {
        boolean success = false;
        try {
            String statement = "INSERT INTO ingredients_in_item (menu_id, inventory_id) VALUES ("
                    + menu_id + ", " + inventory_id + ")";
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
     * Update item to not include an ingredient.
     * 
     * @param menu_id      ~
     * @param inventory_id ~
     * @return {@code boolean} wheather inventory was successfully updated.
     */
    public boolean removeFromItem(int menu_id, int inventory_id) {
        boolean success = false;
        try {
            String statement = "DELETE FROM ingredients_in_item WHERE menu_id = " + menu_id
                    + " AND inventory_id = " + inventory_id;
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
     * Pull a supplier's name from suppliers table.
     * 
     * @param supplier_id ~
     * @return A {@code String} of the supplier's name or empty string if id not
     *         found.
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
     * Pull all supplier names from suppliers table.
     * 
     * @return A {@code Map} containing all supplier id mapped to their
     *         respective names.
     */
    public Map<Integer, String> getSupplierNames() {
        Map<Integer, String> supplierMap = new HashMap<>();
        try {
            String statement = "SELECT * FROM suppliers ORDER BY id ASC";
            ResultSet result = select(statement);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("supplier_name");

                supplierMap.put(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierMap;
    }

    /**
     * Add item to the order.
     * 
     * @param order_id ~
     * @param menu_id  ~
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

    /**
     * Creates a new empty order in orders table.
     * 
     * @param employee_id The current cashier on till.
     * @return The order number for the new order.
     */
    public int createNewOrder(int employee_id) {
        int orderID = -1;
        try {
            String statement = "INSERT INTO orders (employee_id) VALUES (" + employee_id + ") RETURNING id";
            ResultSet result = select(statement);
            if (result.next()) {
                orderID = result.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderID;
    }

    /**
     * Pull the current subtotal to the order from orders table.
     * 
     * @param order_id ~
     * @return {@code double} The current order subtotal or {@code -1} if id not
     *         found.
     */
    public double getOrderSubtotal(int order_id) {
        double subtotal = -1.0;
        try {
            String statement = "SELECT * FROM orders WHERE id = " + order_id;
            ResultSet result = select(statement);
            if (result.next()) {
                subtotal = result.getDouble("total_cost");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subtotal;
    }

    // TODO: remove item from order (should be functional)
    public boolean removeFromOrder(int order_id, int menu_id) {
        boolean success = false;
        try {
            String statement = "DELETE FROM items_in_order WHERE order_id = " + order_id
                    + " AND menu_id = " + menu_id;
            int result = update(statement);
            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // public boolean subtractIngredient(int ingredient_id) {
    // boolean success = false;
    // try {
    // String statement = "UPDATE inventory SET quantity = quantity - 1 WHERE id = "
    // + ingredient_id
    // + " AND quantity > 0";
    // int result = update(statement);
    // if (result > 0) {
    // success = true;
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return success;
    // }

    // TODO: inventory item map () {name, id}
    // TODO: inventory get supplier id
    // TODO: inventory set supplier id
    // TODO: inventory get quantity

    /**
     * Add {@code amount} to the current {@code quantity} in inventory table.
     * 
     * @param inventory_id ~
     * @param amount       To add to current quantity.
     * @return The updated quantity.
     */
    public int addToQuantity(int inventory_id, int amount) {
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
     * Update the {@code quantity} in inventory table.
     * 
     * @param inventory_id ~
     * @param amount       The most current phyical count of the inventory item.
     * @return {@code boolean} wheather inventory was successfully updated.
     */
    public boolean setQuantity(int inventory_id, int amount) {
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

    // TODO: inventory add new item
    public boolean addInventoryItem(String item_name, int supplier_id) {
        boolean success = false;
        try {
            String statement = "INSERT INTO inventory (item_name, supplier_id) VALUES ('"
                    + item_name + "', " + supplier_id + ")";
            int result = update(statement);
            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // TODO: inventory delete item
    public boolean removeInventoryItem(int inventory_id) {
        boolean success = false;
        try {
            String statement = "DELETE FROM inventory WHERE id = " + inventory_id;
            int result = update(statement);
            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // TODO: employees map {full name(string pair), id}
    // TODO: employees get is manager
    // TODO: employees compare pin to id (id,pin)

    // TODO: employees add new employee
    public int addEmployee(String first_name, String last_name) {
        int id = -1;
        try {
            String statement = "INSERT INTO employees (first_name, last_name) VALUES ('"
                    + first_name + "', '" + last_name + "') RETURN id";
            ResultSet result = select(statement);
            if (result.next()) {
                id = result.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public int addManager(String first_name, String last_name, String pin) {
        int id = -1;
        try {
            String statement = "INSERT INTO employees (first_name, last_name, is_manager, manager_pin) VALUES ('"
                    + first_name + "', '" + last_name + "', 't', '" + pin + "') RETURNING id";
            ResultSet result = select(statement);
            if (result.next()) {
                id = result.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    // TODO: employees update info
    public boolean updateEmployeeFirstName(int employee_id, String new_first_name) {
        boolean success = false;
        try {
            String statement = "UPDATE employees SET first_name = '" + new_first_name
                    + "' WHERE id = " + employee_id;
            int result = update(statement);
            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    public boolean updateEmployeeLastName(int employee_id, String new_last_name) {
        boolean success = false;
        try {
            String statement = "UPDATE employees SET last_name = '" + new_last_name
                    + "' WHERE id = " + employee_id;
            int result = update(statement);
            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // TODO: employees change to is manager
    public boolean addManager(int employee_id, String pin) {
        boolean success = false;
        try {
            String statement = "UPDATE employees SET is_manager = 't'"
                    + " WHERE id = " + employee_id;
            int result = update(statement);
            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    // TODO: employees removes
    public boolean removeEmployee(int employee_id) {
        boolean success = false;
        try {
            String statement = "DELETE FROM employees WHERE id = " + employee_id;
            int result = update(statement);
            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

}
