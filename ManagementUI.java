package management;

import management.DatabaseConnection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.beans.VetoableChangeListenerProxy;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ManagementUI extends Application {
    private VBox categoryContent;
    private VBox employeeList;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management");

        // Create the nav bar
        HBox topMenu = createTopMenu(primaryStage);

        // Root Layout
        BorderPane root = new BorderPane();
        root.setTop(topMenu);

        // Starting screen
        int firstSupplierID = getFirstSupplierID(); // Get the first supplier ID
        categoryContent = new VBox();
        root.setCenter(createDeliverySection(firstSupplierID));
        Scene mainScene = new Scene(root, 800, 600);

        // Button Actions
        primaryStage.setScene(mainScene);
        primaryStage.show();

        // Change content on screen when clicking buttons
        ((Button) topMenu.getChildren().get(0))
                .setOnAction(e -> root.setCenter(createDeliverySection(getFirstSupplierID())));
        ((Button) topMenu.getChildren().get(1))
                .setOnAction(e -> root.setCenter(createCountInventorySection(getFirstSupplierID())));
        ((Button) topMenu.getChildren().get(2)).setOnAction(e -> root.setCenter(createTrendsSection()));
        ((Button) topMenu.getChildren().get(3)).setOnAction(e -> root.setCenter(createEmployeesSection()));
        ((Button) topMenu.getChildren().get(4)).setOnAction(e -> root.setCenter(createMenuSection()));

    }

    private int getFirstSupplierID() {
        String query = "SELECT id FROM suppliers LIMIT 1";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // Default if no supplier is found
    }

    // Function for making the navigation bar
    private HBox createTopMenu(Stage primaryStage) {
        HBox topMenu = new HBox(10);
        Button deliveryButton = new Button("Delivery");
        Button countInventoryButton = new Button("Count Inventory");
        Button trendsButton = new Button("Trends");
        Button employeesButton = new Button("Employees");
        Button menuButton = new Button("Menu");

        topMenu.getChildren().addAll(deliveryButton, countInventoryButton, trendsButton, employeesButton, menuButton);
        return topMenu;
    }

    // Function for making the category menu
    private Map<String, Integer> supplierMap = new HashMap<>(); // Store supplier name -> ID mapping

    // Create the supplier dropdown menu for the management UI
    private HBox createSupplierMenu(java.util.function.Consumer<Integer> updateFunction) {
        HBox supplierMenu = new HBox(10);
        ComboBox<String> supplierDropdown = new ComboBox<>();

        String query = "SELECT id, supplier_name FROM suppliers";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int supplierId = rs.getInt("id");
                String supplierName = rs.getString("supplier_name");

                supplierMap.put(supplierName, supplierId);
                supplierDropdown.getItems().add(supplierName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        supplierDropdown.setOnAction(e -> {
            String selectedSupplier = supplierDropdown.getValue();
            if (selectedSupplier != null) {
                int supplierId = supplierMap.get(selectedSupplier);
                updateFunction.accept(supplierId); // Calls the correct UI update function
            }
        });

        supplierMenu.getChildren().addAll(new Label("Select Supplier:"), supplierDropdown);
        return supplierMenu;
    }

    // Actual items for category, placeholders for now until database is connected
    // Added all the flavoring from Menu Items.txt on out github
    private void updateInventory(String itemName, String newQuantity) {
        String updateQuery = "UPDATE inventory SET quantity = ? WHERE item_name = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, Integer.parseInt(newQuantity));
            stmt.setString(2, itemName);

            stmt.executeUpdate();
            System.out.println("Updated " + itemName + " to " + newQuantity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update the inventory content for the delivery section
    private void updateSupplierContent(VBox deliveryContent, int supplierId) {
        deliveryContent.getChildren().clear();

        String query = "SELECT item_name, quantity FROM inventory WHERE supplier_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, supplierId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");

                Label quantityLabel = new Label("Current: " + quantity); // Show current stock
                TextField quantityField = new TextField();
                Button updateButton = new Button("Update");

                updateButton.setOnAction(e -> {
                    updateInventory(supplierId, itemName, quantityField.getText());
                    updateSupplierContent(deliveryContent, supplierId); // Refresh the UI
                });

                HBox itemRow = new HBox(10, new Label(itemName), quantityLabel, quantityField, updateButton);
                deliveryContent.getChildren().add(itemRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Functions for creating the different sections of the management POS UI
    // (Delivery, Count Inventory, Trends)
    private VBox createDeliverySection(int supplierID) {
        VBox layout = new VBox(10);
        VBox deliveryContent = new VBox(); // Fresh VBox for the supplier's inventory items

        HBox supplierMenu = createSupplierMenu(supplierId -> updateSupplierContent(deliveryContent, supplierId)); // Supplier
                                                                                                                  // dropdown
                                                                                                                  // menu

        updateSupplierContent(deliveryContent, supplierID); // Load items dynamically

        layout.getChildren().addAll(supplierMenu, deliveryContent);
        return layout;
    }

    // Update the inventory quantity for the delivery section
    private void updateInventory(int supplierId, String itemName, String inputQuantity) {
        String updateQuery = "UPDATE inventory SET quantity = quantity + ? WHERE supplier_id = ? AND item_name = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, Integer.parseInt(inputQuantity));
            stmt.setInt(2, supplierId);
            stmt.setString(3, itemName);

            stmt.executeUpdate();
            System.out.println("Added " + inputQuantity + " to " + itemName);

            updateSupplierContent(categoryContent, supplierId); // Refresh UI
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create the count inventory section for the management UI, with a section for
    private VBox createCountInventorySection(int supplierID) {
        VBox layout = new VBox(10);
        VBox inventoryContent = new VBox(); // Separate container for inventory display

        HBox supplierMenu = createSupplierMenu(
                supplierId -> updateSupplierContentForCount(inventoryContent, supplierId));

        updateSupplierContentForCount(inventoryContent, supplierID); // Load inventory for counting

        layout.getChildren().addAll(supplierMenu, inventoryContent);
        return layout;
    }

    // Update the inventory quantity for the count inventory section
    private void overrideInventory(int supplierId, String itemName, String newQuantity) {
        String updateQuery = "UPDATE inventory SET quantity = ? WHERE supplier_id = ? AND item_name = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, Integer.parseInt(newQuantity));
            stmt.setInt(2, supplierId);
            stmt.setString(3, itemName);

            stmt.executeUpdate();
            System.out.println("Set " + itemName + " quantity to " + newQuantity);

            updateSupplierContent(categoryContent, supplierId); // Refresh UI
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update the inventory content for the count inventory section
    private void updateSupplierContentForCount(VBox inventoryContent, int supplierId) {
        inventoryContent.getChildren().clear();

        String query = "SELECT item_name, quantity FROM inventory WHERE supplier_id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, supplierId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String itemName = rs.getString("item_name");
                int quantity = rs.getInt("quantity");

                TextField quantityField = new TextField();
                Button updateButton = new Button("Update");

                updateButton.setOnAction(e -> overrideInventory(supplierId, itemName, quantityField.getText()));

                inventoryContent.getChildren().addAll(new Label(itemName), quantityField, updateButton);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create the trends tab for the management UI, with a line graph showing the
    // trends of the different drinks, not complete yet, finish soon I spent all
    // night working on this UI please help me
    private VBox createTrendsSection() {
        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Set Time Frame: ")); // add db functionality later
        layout.getChildren().add(new DatePicker());
        layout.getChildren().add(new Label("to "));
        layout.getChildren().add(new DatePicker());
        layout.getChildren().add(new Label("Trends: ")); // unsure how to implement this as a horizontal thing instead
                                                         // of vertical, for now wokrs ig but change by end of project

        // create the line graph, somehow implement db connectivity idk how yet
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> trendsGraph = new LineChart<>(xAxis, yAxis);
        XYChart.Series<Number, Number> sampleSeries = new XYChart.Series<>();
        sampleSeries.getData().add(new XYChart.Data<>(6, 9));
        sampleSeries.getData().add(new XYChart.Data<>(4, 20));
        sampleSeries.getData().add(new XYChart.Data<>(3, 60));
        trendsGraph.getData().add(sampleSeries);

        layout.getChildren().addAll(trendsGraph, new Label("Key:"), new Label("Black Tea - Red"),
                new Label("Oreo Milk Tea - Blue"), new Label("Matcha Milk Tea - Green")); // placeholders, change once
                                                                                          // database is connected
        return layout;
    }

    // create tab for employees, with sections for adding new employees and viewing
    // existing ones
    private VBox createEmployeesSection() {
        VBox layout = new VBox(10);
        employeeList = new VBox(); // Initialize employee list container

        layout.getChildren().add(new Label("Manage Employees"));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        CheckBox isManagerCheckbox = new CheckBox("Is Manager?");
        TextField managerPinField = new TextField();
        managerPinField.setPromptText("Manager 4-digit PIN");
        Button addEmployeeButton = new Button("Add Employee");

        TextField employeeIdField = new TextField();
        employeeIdField.setPromptText("ID of employee that will be updated");
        TextField updateFirstNameField = new TextField();
        updateFirstNameField.setPromptText("Change First Name");
        TextField updateLastNameField = new TextField();
        updateLastNameField.setPromptText("Change Last Name");
        CheckBox updateIsManagerCheckbox = new CheckBox("Change Manager Status");
        TextField updateManagerPinField = new TextField();
        updateManagerPinField.setPromptText("Change Manager 4-digit PIN");
        Button updateEmployeeButton = new Button("Update Employee");

        addEmployeeButton.setOnAction(e -> {
            addEmployee(firstNameField.getText(), lastNameField.getText(),
                    isManagerCheckbox.isSelected(), managerPinField.getText());
            updateEmployeeList(); // Refresh UI
        });

        updateEmployeeButton.setOnAction(e -> {
            updateCurrentEmployee(Integer.parseInt(employeeIdField.getText()), updateFirstNameField.getText(),
                    updateLastNameField.getText(), updateIsManagerCheckbox.isSelected());
            updateEmployeeList(); // Refresh UI
        });

        layout.getChildren().addAll(new Label("Add Employee:"), firstNameField, lastNameField, isManagerCheckbox,
                managerPinField, addEmployeeButton, new Label("Update an Existing Employee: "), employeeIdField,
                updateFirstNameField, updateLastNameField, updateIsManagerCheckbox, updateManagerPinField,
                updateEmployeeButton,
                new Label("Employees:"), employeeList);

        updateEmployeeList(); // Load employees from database

        return layout;
    }

    // Adds a new employee to the database
    private void addEmployee(String firstName, String lastName, boolean isManager, String managerPin) {
        String insertQuery = "INSERT INTO employees (first_name, last_name, is_manager, manager_pin) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setBoolean(3, isManager);
            stmt.setString(4, managerPin);

            stmt.executeUpdate();
            System.out.println("Added employee: " + firstName + " " + lastName);

            updateEmployeeList(); // Refresh UI
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCurrentEmployee(int employeeId, String newFirstName, String newLastName, boolean isManager) {
        String updateQuery = "UPDATE employees SET first_name = ?, last_name = ?, is_manager = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setString(1, newFirstName);
            stmt.setString(2, newLastName);
            stmt.setBoolean(3, isManager);
            stmt.setInt(4, employeeId);

            stmt.executeUpdate();
            System.out.println("Updated employee ID " + employeeId);

            updateMenuList(); // Refresh UI
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Updates the employee list in the UI (once a new employee is added, refreshes
    // it and shows the updated list)
    private void updateEmployeeList() {
        employeeList.getChildren().clear();

        String query = "SELECT id, first_name, last_name, is_manager FROM employees";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("first_name") + " " + rs.getString("last_name");
                boolean managerStatus = rs.getBoolean("is_manager");
                Label label = new Label(name + (managerStatus ? " (Manager)" : ""));
                employeeList.getChildren().add(label);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // creates the menu tab for the management UI, with places to add new menu items
    // and update the prices of existing ones
    private VBox createMenuSection() {
        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Menu Items"));

        String query = "SELECT item_name, price FROM menu_items";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String itemName = rs.getString("item_name");
                double price = rs.getDouble("price");

                Label label = new Label(itemName + " - $" + String.format("%.2f", price));
                layout.getChildren().add(label);

                TextField priceField = new TextField(String.format("%.2f", price));
                Button updateButton = new Button("Update Price");

                updateButton.setOnAction(e -> {
                    updateMenuItemPrice(itemName, Double.parseDouble(priceField.getText()));
                    createMenuSection(); // Refresh UI
                });

                HBox itemRow = new HBox(10, label, priceField, updateButton);
                layout.getChildren().add(itemRow);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Add new menu item section
        TextField newItemNameField = new TextField();
        newItemNameField.setPromptText("Item Name");
        TextField newItemPriceField = new TextField();
        newItemPriceField.setPromptText("Price");
        Button addItemButton = new Button("Add Menu Item");

        addItemButton.setOnAction(e -> {
            addMenuItem(newItemNameField.getText(), Double.parseDouble(newItemPriceField.getText()));
            createMenuSection(); // Refresh UI
        });

        layout.getChildren().addAll(new Label("Add New Menu Item:"), newItemNameField, newItemPriceField,
                addItemButton);
        return layout;
    }

    // adds a new menu item to the database
    private void addMenuItem(String itemName, double price) {
        String insertQuery = "INSERT INTO menu_items (item_name, price) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

            stmt.setString(1, itemName);
            stmt.setDouble(2, price);

            stmt.executeUpdate();
            System.out.println("Added menu item: " + itemName);

            updateMenuList(); // Refresh UI
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMenuItemPrice(String itemName, double newPrice) {
        String updateQuery = "UPDATE menu_items SET price = ? WHERE item_name = ?";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setDouble(1, newPrice);
            stmt.setString(2, itemName);

            stmt.executeUpdate();
            System.out.println("Updated " + itemName + " price to $" + newPrice);

            updateMenuList(); // Refresh UI
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMenuList() {
        createMenuSection(); // Refresh UI
    }

    public static void main(String[] args) {
        launch(args);
    }
}