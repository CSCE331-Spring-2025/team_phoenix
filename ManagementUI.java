package management;

import management.DatabaseConnection;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import management.Database;
//import java.beans.VetoableChangeListenerProxy;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.LanguageCallback;

public class ManagementUI extends Application {
    private VBox categoryContent;
    private VBox employeeList;

    Database database = new Database();

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
        root.setCenter(createScrollableSection(createDeliverySection(firstSupplierID)));
        Scene mainScene = new Scene(root, 800, 600);

        // Button Actions
        primaryStage.setScene(mainScene);
        primaryStage.show();

        // Change content on screen when clicking buttons
        ((Button) topMenu.getChildren().get(0))
                .setOnAction(e -> root.setCenter(createScrollableSection(createDeliverySection(getFirstSupplierID()))));
        ((Button) topMenu.getChildren().get(1))
                .setOnAction(e -> root
                        .setCenter(createScrollableSection(createCountInventorySection(getFirstSupplierID()))));
        ((Button) topMenu.getChildren().get(2))
                .setOnAction(e -> root.setCenter(createScrollableSection(createReportsSection())));
        ((Button) topMenu.getChildren().get(3))
                .setOnAction(e -> root.setCenter(createScrollableSection(createEmployeesSection())));
        ((Button) topMenu.getChildren().get(4))
                .setOnAction(e -> root.setCenter(createScrollableSection(createMenuSection())));

    }

    // Make screens able to scroll
    private ScrollPane createScrollableSection(VBox content) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
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
        topMenu.setStyle("-fx-background-color:rgb(84, 10, 10); -fx-padding: 10;");

        Button deliveryButton = new Button("Delivery");
        Button countInventoryButton = new Button("Count Inventory");
        Button reportsButton = new Button("Reports");
        Button employeesButton = new Button("Employees");
        Button menuButton = new Button("Menu");

        String buttonStyle = "-fx-background-color: rgb(84, 10, 10); -fx-text-fill: rgb(255, 255, 255); -fx-font-size: 16px; -fx-padding: 10;";
        deliveryButton.setStyle(buttonStyle);
        countInventoryButton.setStyle(buttonStyle);
        reportsButton.setStyle(buttonStyle);
        employeesButton.setStyle(buttonStyle);
        menuButton.setStyle(buttonStyle);

        topMenu.getChildren().addAll(deliveryButton, countInventoryButton, reportsButton, employeesButton, menuButton);
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

        TextField itemNameField = new TextField();
        itemNameField.setPromptText("Item Name");
        TextField supplierIdField = new TextField();
        supplierIdField.setPromptText("Supplier ID (eg, 1 - 5)");
        Button addIngredient = new Button("Add New Ingredient");

        addIngredient.setOnAction(e -> {
            database.addInventoryItem(itemNameField.getText(), Integer.parseInt(supplierIdField.getText()));
            updateSupplierContent(deliveryContent, supplierID); // Refresh UI
        });

        HBox supplierMenu = createSupplierMenu(supplierId -> updateSupplierContent(deliveryContent, supplierId)); // Supplier
                                                                                                                  // dropdown
                                                                                                                  // menu

        updateSupplierContent(deliveryContent, supplierID); // Load items dynamically

        layout.getChildren().addAll(supplierMenu, deliveryContent, new Label("Add a new item:"), itemNameField,
                supplierIdField, addIngredient);

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

        TextField itemNameField = new TextField();
        itemNameField.setPromptText("ID of item to be removed");
        Button removeItem = new Button("Remove Item");

        removeItem.setOnAction(e -> {
            database.removeInventoryItem(Integer.parseInt(itemNameField.getText()));
            updateSupplierContentForCount(inventoryContent, supplierID); // Refresh UI
            System.out.println("Removed item with ID " + itemNameField.getText());
        });

        HBox supplierMenu = createSupplierMenu(
                supplierId -> updateSupplierContentForCount(inventoryContent, supplierId));

        updateSupplierContentForCount(inventoryContent, supplierID); // Load inventory for counting

        layout.getChildren().addAll(supplierMenu, inventoryContent, new Label("Remove an item:"), itemNameField,
                removeItem);
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
    private VBox createReportsSection() {
        VBox layout = new VBox(10);
        layout.getChildren().add(new Label("Set Time Frame: "));
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();
        layout.getChildren().add(startDatePicker);
        layout.getChildren().add(new Label("to "));
        layout.getChildren().add(endDatePicker);
        layout.getChildren().add(new Label("Trends: "));

        // create the bar graph
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> trendsGraph = new BarChart<>(xAxis, yAxis);
        trendsGraph.setTitle("Most Popular Menu Items Over Time Period Selected");

        layout.getChildren().addAll(trendsGraph, new Label("Key:"));

        Button xReportButton = new Button("Generate X Report");
        Button zReportButton = new Button("Generate Z Report");

        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setPrefHeight(200);

        xReportButton
                .setOnAction(e -> generateXReport(startDatePicker.getValue(), endDatePicker.getValue(), reportArea));
        zReportButton
                .setOnAction(e -> generateZReport(startDatePicker.getValue(), endDatePicker.getValue(), reportArea));

        // Show data on graph over selected time frame
        Button updateGraphButton = new Button("Update Graph");
        updateGraphButton
                .setOnAction(e -> updateGraph(startDatePicker.getValue(), endDatePicker.getValue(), trendsGraph));

        layout.getChildren().addAll(updateGraphButton, xReportButton, zReportButton, reportArea);

        return layout;
    }

    private void updateGraph(LocalDate startDate, LocalDate endDate, BarChart<String, Number> trendsGraph) {
        if (startDate == null || endDate == null) {
            System.out.println("Please select both start and end dates.");
            return;
        }

        String query = "SELECT menu_items.item_name, COUNT(items_in_order.menu_id) AS total_usage, orders.time_placed "
                +
                "FROM items_in_order " +
                "JOIN menu_items ON items_in_order.menu_id = menu_items.id " +
                "JOIN orders ON items_in_order.order_id = orders.id " +
                "WHERE orders.time_placed BETWEEN ? AND ? " +
                "GROUP BY menu_items.item_name, orders.time_placed " +
                "ORDER BY total_usage DESC " +
                "LIMIT 5";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate.atTime(LocalTime.MAX)));
            ResultSet rs = stmt.executeQuery();

            trendsGraph.getData().clear();

            Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

            while (rs.next()) {
                String itemName = rs.getString("item_name");
                LocalDate date = rs.getTimestamp("time_placed").toLocalDateTime().toLocalDate();
                int totalUsage = rs.getInt("total_usage");

                XYChart.Series<String, Number> series = seriesMap.get(itemName);
                if (series == null) {
                    series = new XYChart.Series<>();
                    series.setName(itemName);
                    seriesMap.put(itemName, series);
                    trendsGraph.getData().add(series);
                }

                series.getData().add(new XYChart.Data<>(date.toString(), totalUsage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // generate an x report using sql queries
    private void generateXReport(LocalDate startDate, LocalDate endDate, TextArea reportArea) {
        if (startDate == null || endDate == null) {
            reportArea.setText("Please select both start and end dates.");
            return;
        }
        String query = "SELECT SUM(total_cost) AS total_sales FROM orders WHERE time_placed BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate.atTime(LocalTime.MAX)));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double totalSales = rs.getDouble("total_sales");
                reportArea.setText("X Report - Total Sales: $" + totalSales
                        + "\n\t - Percentage of Sales w/ Debit/Credit card: 100% \n\t - Percentage of Sales w/ Cash: 0%");
            } else {
                reportArea.setText("No sales found for the selected date range.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reportArea.setText("Error generating X report: " + e.getMessage());
        }
    }

    // generate a z report using sql queries
    private void generateZReport(LocalDate startDate, LocalDate endDate, TextArea reportArea) {
        if (startDate == null || endDate == null) {
            reportArea.setText("Please select both start and end dates.");
            return;
        }
        String query = "SELECT SUM(total_cost) AS total_sales FROM orders WHERE time_placed BETWEEN ? AND ?";
        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate.atTime(LocalTime.MAX)));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                double totalSales = rs.getDouble("total_sales");
                reportArea.setText("Z Report - Total Sales: $" + totalSales
                        + "\n\t - Percentage of Sales w/ Debit/Credit card: 100% \n\t - Percentage of Sales w/ Cash: 0%");

                resetDailyTotals(conn);
            } else {
                reportArea.setText("No sales found for the selected date range.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            reportArea.setText("Error generating Z report: " + e.getMessage());
        }
    }

    // reset the daily totals for the z report
    private void resetDailyTotals(Connection conn) {
        String resetQuery = "UPDATE orders SET total_cost = 0 WHERE time_placed < ?";
        try (PreparedStatement stmt = conn.prepareStatement(resetQuery)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDate.now().atStartOfDay()));
            int rowsUpdated = stmt.executeUpdate();
            System.out.println("Reset totals for " + rowsUpdated + " orders.");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Button addEmployeeButton = new Button("Add Employee");

        TextField mFirstNameField = new TextField();
        mFirstNameField.setPromptText("First Name");
        TextField mLastNameField = new TextField();
        mLastNameField.setPromptText("Last Name");
        TextField mPinField = new TextField();
        mPinField.setPromptText("4-digit PIN");
        Button addManagerButton = new Button("Add Manager");

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
            database.addEmployee(firstNameField.getText(), lastNameField.getText());
            // isManagerCheckbox.isSelected(), managerPinField.getText());
            updateEmployeeList(); // Refresh UI
        });

        addManagerButton.setOnAction(e -> {
            database.addManager(mFirstNameField.getText(), mLastNameField.getText(), mPinField.getText());
            updateEmployeeList(); // Refresh UI
        });

        updateEmployeeButton.setOnAction(e -> {
            updateCurrentEmployee(Integer.parseInt(employeeIdField.getText()), updateFirstNameField.getText(),
                    updateLastNameField.getText());
            updateEmployeeList(); // Refresh UI
        });

        layout.getChildren().addAll(new Label("Add Employee:"), firstNameField, lastNameField, addEmployeeButton,
                new Label("Add Manager:"), mFirstNameField, mLastNameField, mPinField, addManagerButton,
                new Label("Update an Existing Employee: "), employeeIdField,
                updateFirstNameField, updateLastNameField,
                updateEmployeeButton,
                new Label("Employees:"), employeeList);

        updateEmployeeList(); // Load employees from database

        return layout;
    }

    // Just combines some methods from Database.java to make it easier to call
    private void updateCurrentEmployee(int employeeId, String newFirstName, String newLastName) {
        database.updateEmployeeFirstName(employeeId, newFirstName);
        database.updateEmployeeLastName(employeeId, newLastName);
        System.out.println("Updated employee ID " + employeeId);
        updateMenuList();
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

        TextField itemIdField = new TextField();
        itemIdField.setPromptText("ID of item whose name will be updated");
        TextField updateItemNameField = new TextField();
        updateItemNameField.setPromptText("Change existing item's name");
        Button updateItemNameButton = new Button("Update Item Name");

        addItemButton.setOnAction(e -> {
            database.addMenuItem(newItemNameField.getText(), Double.parseDouble(newItemPriceField.getText()));
            createMenuSection(); // Refresh UI
        });

        updateItemNameButton.setOnAction(e -> {
            database.updateItemName(Integer.parseInt(itemIdField.getText()), updateItemNameField.getText());
            createMenuSection(); // Refresh UI
        });

        layout.getChildren().addAll(new Label("Add New Menu Item:"),
                newItemNameField, newItemPriceField,
                addItemButton);

        layout.getChildren().addAll(new Label("Update an Existing Menu Item:"), itemIdField, updateItemNameField,
                updateItemNameButton);
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