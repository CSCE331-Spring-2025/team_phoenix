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
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

// Add a section called 'Employees' with sections for First Name, Last Name, and Employee ID, do what rubric says

// Change categories to Non-Perishables, Bases, Powder, Toppings

public class ManagementUI extends Application {
    private VBox categoryContent;

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

        topMenu.getChildren().addAll(deliveryButton, countInventoryButton, trendsButton);
        return topMenu;
    }

    // Function for making the category menu
    private Map<String, Integer> supplierMap = new HashMap<>(); // Store supplier name -> ID mapping

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

    private VBox createCountInventorySection(int supplierID) {
        VBox layout = new VBox(10);
        VBox inventoryContent = new VBox(); // Separate container for inventory display

        HBox supplierMenu = createSupplierMenu(
                supplierId -> updateSupplierContentForCount(inventoryContent, supplierId));

        updateSupplierContentForCount(inventoryContent, supplierID); // Load inventory for counting

        layout.getChildren().addAll(supplierMenu, inventoryContent);
        return layout;
    }

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

    public static void main(String[] args) {
        launch(args);
    }
}