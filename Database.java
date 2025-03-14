package cashier;

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
 * <li>{@link #removeFromOrder(int, int)}
 * </ul>
 * 
 * <li>
 * Menu Items
 * <ul>
 * <li>{@link #getMenuItemNames()}
 * <li>{@link #addMenuItem(String, double)}
 * <li>{@link #getItemName(int)}
 * <li>{@link #getItemPrice(int)}
 * <li>{@link #canBeMade(int)}
 * <li>{@link #updateItemName(int, String)}
 * <li>{@link #updateItemPrice(int, double)}
 * <li>{@link #addIngredientsToItem(int, int)}
 * <li>{@link #removeIngredientsFromItem(int, int)}
 * <li>{@link #removeMenuItem(int)}
 * </ul>
 * 
 * <li>
 * Inventory
 * <ul>
 * <li>{@link #getInventoryNames()}
 * <li>{@link #addInventoryItem(String, int)}
 * <li>{@link #removeInventoryItem(int)}
 * <li>{@link #getItemQuantity(int)}
 * <li>{@link #setQuantity(int, int)}
 * <li>{@link #addToQuantity(int, int)}
 * <li>{@link #getItemSupplier(int)}
 * <li>{@link #updateItemSupplier(int, int)}
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
 * <li>{@link #getEmployeeNames()}
 * <li>{@link #addEmployee(String, String)}
 * <li>{@link #addManager(String, String, String)}
 * <li>{@link #addManager(int, String)}
 * <li>{@link #managerStatus(int)}
 * <li>{@link #checkManagerPIN(int, String)}
 * <li>{@link #updateEmployeeFirstName(int, String)}
 * <li>{@link #updateEmployeeLastName(int, String)}
 * <li>{@link #removeEmployee(int)}
 * </ul>
 * </ol>
 * 
 * @author Miles Q. Ryder
 */
public class Database {
    private static Connection conn = null;

    static {
        try {
            // Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:postgresql://csce-315-db.engr.tamu.edu/team_phoenix_db",
                    dbSetup.user, dbSetup.pswd);

            // shutdown hook to close DB connection
            Runtime.getRuntime().addShutdownHook(new Thread(() -> closeConnection()));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } // end try catch
    }

    static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * For SQL quaries that return a table of data.
     * 
     * @param statement The query typed into the PSQL terminal.
     * @return {@code ResultSet} with the SQL query output.
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

    // int currentOrderNumber() {
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
     * @param item_id The menu item id.
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
     * @param item_id The menu item id.
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

    /**
     * Pull all item names from menu items table.
     * 
     * @return A {@code Map} containing all menu item ids mapped to their
     *         respective names.
     */
    public Map<Integer, String> getMenuItemNames() {
        Map<Integer, String> menuMap = new HashMap<>();
        try {
            String statement = "SELECT * FROM menu_items ORDER BY id ASC";
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
     * @param item_id The menu item id.
     * @param newName The menu item's new name.
     * @return {@code boolean} wheather item was successfully updated.
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
     * @param item_id  The menu item id.
     * @param newPrice The menu item's new price.
     * @return {@code boolean} wheather item was successfully updated.
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
     * @param name  The menu item name.
     * @param price The menu item price.
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
     * Removes an item from the menu.
     * 
     * @param menu_id The menu item id.
     * @return {@code boolean} wheather item was successfully updated.
     */
    public boolean removeMenuItem(int menu_id) {
        boolean success = false;
        try {
            String sql = "DELETE FROM menu_items WHERE menu_id = " + menu_id;
            int result = update(sql);

            if (result > 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    /**
     * Add an ingredient to a menu item.
     * 
     * @param menu_id      The menu item id.
     * @param inventory_id The ingredient item id.
     * @return {@code boolean} wheather item was successfully updated.
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
     * @param menu_id      The menu item id.
     * @param inventory_id The ingredient item id.
     * @return {@code boolean} wheather item was successfully removed.
     */
    public boolean removeIngredientsFromItem(int menu_id, int inventory_id) {
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
     * Checks if a menu item is capable of being made.
     * (i.e., ingredients are in-stock and not removed)
     * 
     * @param menu_id The item to be checked.
     * @return An {@code int} to represent a {@code boolean}.
     *         (1=true, 0=false, -1=error)
     */
    public int canBeMade(int menu_id) {
        int makable = -1;
        try {
            String statement = "UPDATE inventory SET quantity = quantity + " + menu_id + " RETURNING quantity";
            ResultSet result = select(statement);
            if (result.next()) {
                String makableString = result.getString("can_make_menu_item");
                makable = (makableString.equals("t")) ? 1 : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return makable;
    }

    /**
     * Pull a supplier's name from suppliers table.
     * 
     * @param supplier_id The supplier id.
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
     * @return A {@code Map} containing all supplier ids mapped to their
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
     * @param order_id The order id.
     * @param menu_id  The menu item id.
     * @return {@code boolean} wheather order was successfully updated.
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
     * @param order_id The order id.
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

    /**
     * Remove an item from the order.
     * 
     * @param order_id The order id.
     * @param menu_id  The item id to be removed.
     * @return {@code boolean} wheather order was successfully updated.
     */
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

    /**
     * Pull all item names from the inventory table.
     * 
     * @return A {@code Map} containing all inventory ids mapped to their
     *         respective names.
     */
    public Map<Integer, String> getInventoryNames() {
        Map<Integer, String> inventoryMap = new HashMap<>();
        try {
            String statement = "SELECT * FROM inventory WHERE is_deleted = false ORDER BY id ASC";
            ResultSet result = select(statement);
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("item_name");

                inventoryMap.put(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inventoryMap;
    }

    /**
     * Pull the current supplier for an inventory item.
     * 
     * @param inventory_id The inventory item's id.
     * @return The supplier's id.
     */
    public int getItemSupplier(int inventory_id) {
        int supplier_id = -1;
        try {
            String statement = "SELECT * FROM inventory WHERE id = " + inventory_id;
            ResultSet result = select(statement);
            if (result.next()) {
                supplier_id = result.getInt("supplier_id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplier_id;
    }

    /**
     * Change an inventory item's supplier.
     * 
     * @param inventory_id The inventory item id.
     * @param supplier_id  The supplier id
     * @return {@code boolean} wheather inventory was successfully updated.
     */
    public boolean updateItemSupplier(int inventory_id, int new_supplier_id) {
        boolean success = false;
        try {
            String statement = "UPDATE inventory SET supplier_id = " + new_supplier_id
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

    /**
     * Pulls the quantity of the inventory item.
     * 
     * @param inventory_id The inventory item id.
     * @return The quantity as an {@code int}, -1 if id not found.
     */
    public int getItemQuantity(int inventory_id) {
        int quantity = -1;
        try {
            String statement = "SELECT * FROM inventory WHERE id = " + inventory_id;
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
     * Add {@code amount} to the current {@code quantity} in inventory table.
     * 
     * @param inventory_id The inventory item id.
     * @param amount       The amount to add to current quantity.
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
     * @param inventory_id The inventory item id.
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

    /**
     * Add a new inventory item.
     * 
     * @param item_name   The name of the inventory item.
     * @param supplier_id The id for the item's supplier
     * @return The new inventory id.
     */
    public int addInventoryItem(String item_name, int supplier_id) {
        int id = -1;
        try {
            String statement = "INSERT INTO inventory (item_name, supplier_id) VALUES ('"
                    + item_name + "', " + supplier_id + ") RETURNING id";
            ResultSet result = select(statement);
            if (result.next()) {
                id = result.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * Remove an item from the inventory.
     * 
     * @param inventory_id The item's id to be removed.
     * @return {@code boolean} wheather inventory was successfully updated.
     */
    public boolean removeInventoryItem(int inventory_id) {
        boolean success = false;
        try {
            String statement = "UPDATE inventory SET is_deleted = TRUE WHERE id = " + inventory_id;
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
     * Pull all employee names from the employees table.
     * 
     * @return A {@code Map} containing all employee ids mapped to Strings
     *         containing thier full names. (formated as "First Last")
     */
    public Map<Integer, String> getEmployeeNames() {
        Map<Integer, String> employeeMap = new HashMap<>();
        try {
            String sql = "SELECT * FROM inventory WHERE is_deleted = false ORDER BY id ASC";
            ResultSet result = select(sql);

            while (result.next()) {
                int id = result.getInt("id");
                String first_name = result.getString("first_name");
                String last_name = result.getString("last_name");
                String name = first_name.concat(" ").concat(last_name);

                employeeMap.put(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeMap;
    }

    /**
     * Pull the manager status of an employee.
     * 
     * @param employee_id The employee id.
     * @return An {@code int} to represent a {@code boolean}.
     *         (1=true, 0=false, -1=error)
     */
    public int managerStatus(int employee_id) {
        int manager = -1;
        try {
            String statement = "SELECT * FROM employees WHERE id = " + employee_id;
            ResultSet result = select(statement);
            if (result.next()) {
                String managerString = result.getString("is_manager");
                manager = (managerString.equals("t")) ? 1 : 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return manager;
    }

    /**
     * Checks the database to see if a manager's PIN is valid.
     * 
     * @param employee_id The manager's emloyee id.
     * @param pin         The manager PIN.
     * @return An {@code int} value representing the valitity of a manager's PIN.
     *         (0=non-manager id, 1=valid id & pin, 2=invalid pin, -1=error)
     */
    public int checkManagerPIN(int employee_id, String pin) {
        int status = managerStatus(employee_id);
        if (status == 1) {
            try {
                String statement = "SELECT * FROM employees WHERE id = " + employee_id;
                ResultSet result = select(statement);
                if (result.next()) {
                    String managerString = result.getString("manager_pin");
                    status = (managerString.equals(pin)) ? 1 : 2;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return status;
    }

    /**
     * Adds a new employee.
     * 
     * @param first_name The new employee's first name.
     * @param last_name  The new employee's last name.
     * @return The new employee id.
     */
    public int addEmployee(String first_name, String last_name) {
        int id = -1;
        try {
            String statement = "INSERT INTO employees (first_name, last_name) VALUES ('"
                    + first_name + "', '" + last_name + "') RETURNING id";
            ResultSet result = select(statement);
            if (result.next()) {
                id = result.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * Adds a new employee who is a manager.
     * 
     * @param first_name The new employee's first name.
     * @param last_name  The new employee's last name.
     * @param pin        The new employee's manager PIN.
     * @return The new employee id.
     */
    public int addManager(String first_name, String last_name, String pin) {
        int id = -1;
        try {
            String statement = "INSERT INTO employees (first_name, last_name, is_manager, manager_pin) VALUES ('"
                    + first_name + "', '" + last_name + "', TRUE, '" + pin + "') RETURNING id";
            ResultSet result = select(statement);
            if (result.next()) {
                id = result.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    /**
     * Update an employee's first name.
     * 
     * @param employee_id    The employee's id.
     * @param new_first_name The employee's new first name.
     * @return {@code boolean} wheather employee was successfully updated.
     */
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

    /**
     * Update an employee's last name.
     * 
     * @param employee_id   The employee's id.
     * @param new_last_name The employee's new last name.
     * @return {@code boolean} wheather employee was successfully updated.
     */
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

    /**
     * Promote an employee to manager status.
     * 
     * @param employee_id The employee's id.
     * @param pin         The employee's new manager PIN.
     * @return {@code boolean} wheather employee was successfully updated.
     */
    public boolean addManager(int employee_id, String pin) {
        boolean success = false;
        try {
            String statement = "UPDATE employees SET is_manager = TRUE, manager_pin = " + pin
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

    /**
     * Remove an employee from the system.
     * 
     * @param employee_id The employee id.
     * @return {@code boolean} wheather employee was successfully removed.
     */
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

    /**
     * Pull the information on the merchant.
     * 
     * @return A map of information to data consisting of store name, store address,
     *         terminal ID, and register ID.
     */
    public Map<String, String> getMerchantInfo() {
        Map<String, String> merchantInfo = new HashMap<>();
        merchantInfo.put("Store Name", "Boba Tea Store");
        merchantInfo.put("Store Address", "123 Boba Street, Galveston, TX");
        merchantInfo.put("Terminal ID", "POS-01");
        merchantInfo.put("Register ID", "Reg-07");
        return merchantInfo;
    }

    /**
     * Pull the store manager's name from the employees table.
     * 
     * @return The name of the store manager.
     */
    public String getManagerName() {
        // id = 1 is the manager, Nathan Lee
        String query = "SELECT first_name, last_name FROM employees WHERE id = 1;";
        String managerName = "Unknown Manager"; // default

        try {
            ResultSet rs = select(query);
            if (rs.next()) {
                managerName = rs.getString("first_name") + " " + rs.getString("last_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return managerName;
    }

    /**
     * X-REPORT: Gives sales per hour for the currrent day of operation. Can be run
     * anytime.
     * 
     * @return A {@code Map} of the time to the total sales in that hour.
     */
    public Map<String, Double> getSalesPerHour() {
        Map<String, Double> salesPerHour = new HashMap<>();
        String query = """
                    SELECT DATE_TRUNC('hour', time_placed) AS hour, SUM(total_cost) AS total_sales
                    FROM orders
                    WHERE time_placed >= CURRENT_DATE
                    GROUP BY hour
                    ORDER BY hour DESC;
                """;

        try {
            ResultSet rs = select(query);
            while (rs.next()) {
                salesPerHour.put(rs.getTimestamp("hour").toString(), rs.getDouble("total_sales"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return salesPerHour;
    }

    /**
     * Z-REPORT: Get total sales for the current day. Run once at end of day.
     * 
     * @return The total sales from the current day.
     */
    public double getTotalSales() {
        String query = "SELECT SUM(total_cost) AS total_sales FROM orders WHERE time_placed >= CURRENT_DATE;";
        return getSingleValue(query, "total_sales");
    }

    /**
     * Placeholder: Discounts, Voids, Service Charges not tracked
     * 
     * @return
     */
    public Map<String, Double> getVoidsReturnsDiscards() {
        return new HashMap<>();
    }

    /**
     * Helper method when getting a *single* numerical value from the database
     * testing this to improve readability of methods.
     * 
     * @param query
     * @param columnName
     * @return
     */
    private double getSingleValue(String query, String columnName) {
        double value = 0.0;
        try {
            ResultSet rs = select(query);
            if (rs.next()) {
                value = rs.getDouble(columnName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
