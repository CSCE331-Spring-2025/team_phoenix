package management;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// Add a section called 'Employees' with sections for First Name, Last Name, and Employee ID, do what rubric says

// Change categories to Non-Perishables, Bases, Powder, Toppings

public class managementUI extends Application {
    private VBox categoryContent;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inventory Management");

        // Create the nav bar
        HBox topMenu = createTopMenu(primaryStage);

        // Root Layout
        BorderPane root = new BorderPane();
        root.setTop(topMenu);

        // Starting screen (In this case, Delivery section for Cups)
        categoryContent = new VBox();
        root.setCenter(createDeliverySection("Non-Perishables"));
        Scene mainScene = new Scene(root, 800, 600);

        // Button Actions
        primaryStage.setScene(mainScene);
        primaryStage.show();

        // Change content on screen when clicking buttons
        ((Button) topMenu.getChildren().get(0))
                .setOnAction(e -> root.setCenter(createDeliverySection("Non-Perishables")));
        ((Button) topMenu.getChildren().get(1))
                .setOnAction(e -> root.setCenter(createCountInventorySection("Non-Perishables")));
        ((Button) topMenu.getChildren().get(2)).setOnAction(e -> root.setCenter(createTrendsSection()));

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
    private HBox createCategoryMenu() {
        HBox categoryMenu = new HBox(10);
        String[] categories = { "Non-Perishables", "Milk", "Syrup", "Flavoring", "Fruit", "Miscellaneous" }; // change
                                                                                                             // later
        for (String category : categories) {
            Button button = new Button(category);
            button.setOnAction(e -> updateCategoryContent(category));
            categoryMenu.getChildren().add(button);
        }
        return categoryMenu;
    }

    // Actual items for category, placeholders for now until database is connected
    // Added all the flavoring from Menu Items.txt on out github
    private void updateCategoryContent(String category) { // make categories milk,flavor, non-perishables
        categoryContent.getChildren().clear();
        switch (category) {
            case "Non-Perishables":
                categoryContent.getChildren().addAll(new Label("Straws"), new TextField(),
                        new Label("Napkins"), new TextField(),
                        new Label("Lids"), new TextField(),
                        new Label("Cup Holders"), new TextField(),
                        new Label("Cups"), new TextField());
                break;
            /*
             * case "Milk":
             * categoryContent.getChildren().addAll(new Label("Whole Milk"), new
             * TextField(),
             * new Label("2% Milk"), new TextField(),
             * new Label("Nonfat Milk"), new TextField(),
             * new Label("Soy Milk"), new TextField(),
             * new Label("Lactose-Free Milk"), new TextField(),
             * new Label("Almond Milk"), new TextField(),
             * new Label("Coconut Milk"), new TextField(),
             * new Label("Oat Milk"), new TextField());
             * break;
             */
            case "Syrup":
                categoryContent.getChildren().addAll(new Label("Vanilla"), new TextField(),
                        new Label("Caramel"), new TextField(),
                        new Label("Chocolate"), new TextField());
                break;
            case "Flavoring":
                categoryContent.getChildren().addAll(new Label("Chocolate"), new TextField(),
                        new Label("Caramel"), new TextField(),
                        new Label("Honeydew"), new TextField(),
                        new Label("Coconut"), new TextField(),
                        new Label("Peach"), new TextField(),
                        new Label("Almond"), new TextField());
                break;
            case "Fruit":
                categoryContent.getChildren().addAll(new Label("Peach"), new TextField(),
                        new Label("Frozen Strawberry"), new TextField(),
                        new Label("Honeydew"), new TextField(),
                        new Label("Lychee"), new TextField(),
                        new Label("Mango"), new TextField());
                break;
            case "Miscellaneous": // add vanilla essence
                TextField cocoaInField = new TextField();
                TextField taroInField = new TextField();
                TextField matchaPowderInField = new TextField();
                TextField lemonadeMixInField = new TextField();
                TextField brownSugarInField = new TextField();
                TextField oreosInField = new TextField();
                TextField teaMixInField = new TextField();

                Button submitCocoa = new Button("Submit");
                Button submitTaro = new Button("Submit");
                Button submitMatcha = new Button("Submit");
                Button submitLemonade = new Button("Submit");
                Button submitBrownSugar = new Button("Submit");
                Button submitOreos = new Button("Submit");
                Button submitTeaMix = new Button("Submit");

                categoryContent.getChildren().addAll(new Label("Cocoa Powder"), cocoaInField, submitCocoa,
                        new Label("Taro Powder"), taroInField, submitTaro,
                        new Label("Matcha Powder"), matchaPowderInField, submitMatcha,
                        new Label("Lemonade Mix"), lemonadeMixInField, submitLemonade,
                        new Label("Brown Sugar"), brownSugarInField, submitBrownSugar,
                        new Label("Oreos"), oreosInField, submitOreos,
                        new Label("Tea mix"), teaMixInField, submitTeaMix);

                // below is variable declarations for the different inputs to be inserted into
                // the database, commented out temporarily
                /*
                 * int cocoaInput = cocoaInField.getText();
                 * int taroInput = taroInField.getText();
                 * int matchaPowderInput = matchaPowderInField.getText();
                 * int lemonadeMixInput = lemonadeMixInField.getText();
                 * int brownSugarInput = brownSugarInField.getText();
                 * int oreosInput = oreosInField.getText();
                 * int teaMixInput = teaMixInField.getText();
                 */

                break;
        }
        // Just testing how to take in input from the text fields
        /*
         * TextField inputField = new TextField();
         * Button submitButton = new Button("Submit");
         * 
         * submitButton.setOnAction(e -> {
         * String input = inputField.getText();
         * System.out.println("Input: " + input);
         * // Perform action with the input
         * });
         */

        // categoryContent.getChildren().addAll(inputField, submitButton);
    }

    // Functions for creating the different sections of the management POS UI
    // (Delivery, Count Inventory, Trends)
    private VBox createDeliverySection(String category) {
        VBox layout = new VBox(10);
        layout.getChildren().addAll(createCategoryMenu(), categoryContent);
        updateCategoryContent(category);
        return layout;
    }

    private VBox createCountInventorySection(String category) {
        VBox layout = new VBox(10);
        layout.getChildren().addAll(createCategoryMenu(), categoryContent);
        updateCategoryContent(category);
        return layout;
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
