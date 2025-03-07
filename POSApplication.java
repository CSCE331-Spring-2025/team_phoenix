package cashier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.beans.EventHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

// for management side of UI
import cashier.DatabaseConnection;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Creates the cashier section of the POS system.
 */
/**
 * Creates the cashier and management GUI for the POS system.
 * <p>
 * Hosts methods to //add here Dylan//, order/count inventory quatities, display
 * data from database about inventory quantities and sales numbers, and change
 * Employee and Menu Item information.
 * <p>
 * 
 * <p>
 * ===========================
 * TABLE OF CONTENTS
 * ===========================
 * <ol>
 * 
 * Add your stuff here Dylan
 * 
 * <li>
 * ManagementUI Menu
 * <ul>
 * <li>{@link #getManagementStartingScene(Stage)}
 * <li>{@link #createTopMenu(Stage)}
 * <li>{@link #createScrollableSection(VBox)}
 * </ul>
 * 
 * <li>
 * Delivery Menu
 * <ul>
 * <li>{@link #updateInventory(String, String)}
 * <li>{@link #createSupplierMenu(java.util.function.Consumer)}
 * <li>{@link #updateSupplierContent(VBox, int)}
 * <li>{@link #createDeliverySection(int)}
 * </ul>
 * 
 * <li>
 * Count Inventory Menu
 * <ul>
 * <li>{@link #createCountInventorySection(int)}
 * <li>{@link #overrideInventory(int, String, String)}
 * <li>{@link #updateSupplierContentForCount(VBox, int)}
 * </ul>
 * 
 * <li>
 * Reports Menu
 * <ul>
 * <li>{@link #createReportsSection()}
 * <li>{@link #updateGraph(LocalDate, LocalDate, BarChart<String, Number>)}
 * <li>{@link #generateXReport(TextArea)}
 * <li>{@link #generateZReport(TextArea)}
 * </ul>
 * 
 * <li>
 * Employees Menu
 * <ul>
 * <li>{@link #createEmployeesSection()}
 * <li>{@link #updateCurrentEmployee(int, String, String)}
 * <li>{@link #updateEmployeeList()}
 * </ul>
 * 
 * <li>
 * Menu Menu
 * <li>{@link #createMenuSection()}
 * <li>{@link #addMenuItem(String itemName, double price)}
 * <li>{@link #updateMenuItemPrice(String, double)}
 * <li>{@link #updateMenuList()}
 * </ul>
 * 
 * </ol>
 * 
 * @author Dylan Nguyen, Rene Almeida
 */
public class POSApplication extends Application {
    Map<Integer, Integer> orderMap = new HashMap<>(); // A map containing the current order in the form: <id of a
                                                      // product, amount of the product in the order>
    int orderNum; // The id of the current order
    Database database = new Database();
    Map<Integer, String> buttonNameMap = database.getMenuItemNames(); // The map of every menu item in the form <id of a
                                                                      // product, name of the product>

    // managementUI stuff
    private VBox categoryContent;
    private VBox employeeList;

    /**
     * @author Dylan Nguyen
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {

        /*
         * This part is where all the components for the GUI are initialized.
         *
         */

        /*
         * These are the components for the first screen
         *
         */

        Rectangle background = new Rectangle();
        background.setWidth(1080);
        background.setHeight(720);
        background.setFill(Color.PEACHPUFF);

        Rectangle decoration1 = new Rectangle();
        decoration1.setWidth(400);
        decoration1.setFill(Color.SADDLEBROWN);

        Rectangle decoration2 = new Rectangle();
        decoration2.setWidth(100);
        decoration2.setFill(Color.SANDYBROWN);

        Rectangle decoration3 = new Rectangle();
        decoration3.setWidth(100);
        decoration3.setFill(Color.SANDYBROWN);

        Button startOrder = new Button();
        startOrder.setText("Start order");
        startOrder.setPrefSize(300, 50);

        Button goToLogin = new Button();
        goToLogin.setText("Manager Login");
        goToLogin.setPrefSize(300, 50);

        Text idInputLabel = new Text();
        idInputLabel.setText("Please enter your ID:");
        idInputLabel.setFont(Font.font("verdana", 20));

        TextArea employeeIdInput = new TextArea();
        employeeIdInput.setPrefSize(300, 20);
        employeeIdInput.setFont(Font.font("verdana"));

        Group firstRoot = new Group(background, decoration2, decoration3, decoration1, startOrder, goToLogin,
                idInputLabel, employeeIdInput);

        Scene firstScene = new Scene(firstRoot, 1080, 720);

        /*
         * These are the components for the second screen
         *
         */
        Rectangle background2 = new Rectangle();
        background2.setWidth(1080);
        background2.setHeight(720);
        background2.setFill(Color.PEACHPUFF);

        Rectangle bottomBar = new Rectangle();
        bottomBar.setX(0);
        bottomBar.setHeight(50);
        bottomBar.setFill(Color.SADDLEBROWN);

        Rectangle subtotalBar = new Rectangle();
        subtotalBar.setY(0);
        subtotalBar.setWidth(300);
        subtotalBar.setFill(Color.SANDYBROWN);

        Group buttonPane = new Group();

        ArrayList<Button> orderButtons = new ArrayList<>();
        for (Integer id : buttonNameMap.keySet()) {
            orderButtons.add(createButton(id));
        }

        Image button1Image = new Image("file:images/strawberry-lemonade-smoothie.jpg");
        ImageView button1ImageView = new ImageView(button1Image);
        button1ImageView.setPreserveRatio(true);
        button1ImageView.setFitHeight(orderButtons.get(0).getPrefHeight() - 50);
        button1ImageView.setFitWidth(orderButtons.get(0).getPrefWidth());
        orderButtons.get(0).setGraphic(button1ImageView);

        Image button2Image = new Image("file:images/mango-smoothie.jpg");
        ImageView button2ImageView = new ImageView(button2Image);
        button2ImageView.setPreserveRatio(true);
        button2ImageView.setFitHeight(orderButtons.get(1).getPrefHeight() - 50);
        button2ImageView.setFitWidth(orderButtons.get(1).getPrefWidth());
        orderButtons.get(1).setGraphic(button2ImageView);

        Image button3Image = new Image("file:images/almond-milk-tea.jpg");
        ImageView button3ImageView = new ImageView(button3Image);
        button3ImageView.setPreserveRatio(true);
        button3ImageView.setFitHeight(orderButtons.get(2).getPrefHeight() - 50);
        button3ImageView.setFitWidth(orderButtons.get(2).getPrefWidth());
        orderButtons.get(2).setGraphic(button3ImageView);

        Image button4Image = new Image("file:images/caramel-milk-tea.jpg");
        ImageView button4ImageView = new ImageView(button4Image);
        button4ImageView.setPreserveRatio(true);
        button4ImageView.setFitHeight(orderButtons.get(3).getPrefHeight() - 50);
        button4ImageView.setFitWidth(orderButtons.get(3).getPrefWidth());
        orderButtons.get(3).setGraphic(button4ImageView);

        Image button5Image = new Image("file:images/brown-sugar-milk-tea.jpg");
        ImageView button5ImageView = new ImageView(button5Image);
        button5ImageView.setPreserveRatio(true);
        button5ImageView.setFitHeight(orderButtons.get(4).getPrefHeight() - 50);
        button5ImageView.setFitWidth(orderButtons.get(4).getPrefWidth());
        orderButtons.get(4).setGraphic(button5ImageView);

        Image button6Image = new Image("file:images/lychee-milk-tea.jpg");
        ImageView button6ImageView = new ImageView(button6Image);
        button6ImageView.setPreserveRatio(true);
        button6ImageView.setFitHeight(orderButtons.get(5).getPrefHeight() - 50);
        button6ImageView.setFitWidth(orderButtons.get(5).getPrefWidth());
        orderButtons.get(5).setGraphic(button6ImageView);

        Image button7Image = new Image("file:images/vanilla-milk-tea.jpg");
        ImageView button7ImageView = new ImageView(button7Image);
        button7ImageView.setPreserveRatio(true);
        button7ImageView.setFitHeight(orderButtons.get(6).getPrefHeight() - 50);
        button7ImageView.setFitWidth(orderButtons.get(6).getPrefWidth());
        orderButtons.get(6).setGraphic(button7ImageView);

        Image button8Image = new Image("file:images/taro-milk-tea.jpeg");
        ImageView button8ImageView = new ImageView(button8Image);
        button8ImageView.setPreserveRatio(true);
        button8ImageView.setFitHeight(orderButtons.get(7).getPrefHeight() - 50);
        button8ImageView.setFitWidth(orderButtons.get(7).getPrefWidth());
        orderButtons.get(7).setGraphic(button8ImageView);

        Image button9Image = new Image("file:images/chocolate-milk-tea.jpg");
        ImageView button9ImageView = new ImageView(button9Image);
        button9ImageView.setPreserveRatio(true);
        button9ImageView.setFitHeight(orderButtons.get(8).getPrefHeight() - 50);
        button9ImageView.setFitWidth(orderButtons.get(8).getPrefWidth());
        orderButtons.get(8).setGraphic(button9ImageView);

        Image button10Image = new Image("file:images/black-tea.jpg");
        ImageView button10ImageView = new ImageView(button10Image);
        button10ImageView.setPreserveRatio(true);
        button10ImageView.setFitHeight(orderButtons.get(9).getPrefHeight() - 50);
        button10ImageView.setFitWidth(orderButtons.get(9).getPrefWidth());
        orderButtons.get(9).setGraphic(button10ImageView);

        Image button11Image = new Image("file:images/honeydew-milk-tea.jpg");
        ImageView button11ImageView = new ImageView(button11Image);
        button11ImageView.setPreserveRatio(true);
        button11ImageView.setFitHeight(orderButtons.get(10).getPrefHeight() - 50);
        button11ImageView.setFitWidth(orderButtons.get(10).getPrefWidth());
        orderButtons.get(10).setGraphic(button11ImageView);

        Image button12Image = new Image("file:images/coffee-milk-tea.jpg");
        ImageView button12ImageView = new ImageView(button12Image);
        button12ImageView.setPreserveRatio(true);
        button12ImageView.setFitHeight(orderButtons.get(11).getPrefHeight() - 50);
        button12ImageView.setFitWidth(orderButtons.get(11).getPrefWidth());
        orderButtons.get(11).setGraphic(button12ImageView);

        Image button13Image = new Image("file:images/matcha-milk-tea.jpg");
        ImageView button13ImageView = new ImageView(button13Image);
        button13ImageView.setPreserveRatio(true);
        button13ImageView.setFitHeight(orderButtons.get(12).getPrefHeight() - 50);
        button13ImageView.setFitWidth(orderButtons.get(12).getPrefWidth());
        orderButtons.get(12).setGraphic(button13ImageView);

        Image button14Image = new Image("file:images/oreo-milk-tea.jpg");
        ImageView button14ImageView = new ImageView(button14Image);
        button14ImageView.setPreserveRatio(true);
        button14ImageView.setFitHeight(orderButtons.get(13).getPrefHeight() - 50);
        button14ImageView.setFitWidth(orderButtons.get(13).getPrefWidth());
        orderButtons.get(13).setGraphic(button14ImageView);

        Image button15Image = new Image("file:images/peach-milk-tea.jpg");
        ImageView button15ImageView = new ImageView(button15Image);
        button15ImageView.setPreserveRatio(true);
        button15ImageView.setFitHeight(orderButtons.get(14).getPrefHeight() - 50);
        button15ImageView.setFitWidth(orderButtons.get(14).getPrefWidth());
        orderButtons.get(14).setGraphic(button15ImageView);

        Image button16Image = new Image("file:images/coconut-milk-tea.jpg");
        ImageView button16ImageView = new ImageView(button16Image);
        button16ImageView.setPreserveRatio(true);
        button16ImageView.setFitHeight(orderButtons.get(15).getPrefHeight() - 50);
        button16ImageView.setFitWidth(orderButtons.get(15).getPrefWidth());
        orderButtons.get(15).setGraphic(button16ImageView);

        Button checkout = new Button();
        checkout.setText("Checkout");
        checkout.setPrefSize(300, 50);

        Text subtotal = new Text();
        subtotal.setFont(Font.font("verdana", 20));
        subtotal.setWrappingWidth(subtotalBar.getWidth() - 10);

        Group orderingRoot = new Group(background2,
                bottomBar,
                subtotalBar,
                subtotal,
                checkout);

        for (int i = 0; i < orderButtons.size(); i++) {
            if (database.getItemName(i + 1) != "") {
                buttonPane.getChildren().add(orderButtons.get(i));
            }
        }

        ScrollPane scrollPane = new ScrollPane(buttonPane);
        scrollPane.setPrefSize(625, 600);

        orderingRoot.getChildren().add(scrollPane);

        Scene orderingScene = new Scene(orderingRoot);

        /*
         * These are the components for the third page.
         *
         */
        Rectangle background3 = new Rectangle();
        background3.setWidth(1080);
        background3.setHeight(720);
        background3.setFill(Color.PEACHPUFF);

        Rectangle decoration4 = new Rectangle();
        decoration4.setWidth(500);
        decoration4.setFill(Color.SANDYBROWN);

        Rectangle bottomBar2 = new Rectangle();
        bottomBar2.setX(0);
        bottomBar2.setHeight(50);
        bottomBar2.setFill(Color.SADDLEBROWN);

        Button backButton = new Button();
        backButton.setText("Back");
        backButton.setPrefSize(300, 50);

        Button finishOrder = new Button();
        finishOrder.setText("Finish order");
        finishOrder.setPrefSize(300, 50);

        Text checkoutTotal = new Text();
        checkoutTotal.setFont(Font.font("verdana", 20));
        checkoutTotal.setWrappingWidth(600);

        Group checkoutRoot = new Group(background3,
                decoration4,
                bottomBar2,
                backButton,
                finishOrder,
                checkoutTotal);
        Scene checkoutScene = new Scene(checkoutRoot, 1080, 720);

        /*
         * These are the components for the manager login page
         *
         */
        Button login = new Button();
        login.setText("Login");
        login.setPrefSize(300, 50);

        Button backToStart = new Button();
        backToStart.setText("Back");
        backToStart.setPrefSize(300, 50);

        Text managerIdInputLabel = new Text();
        managerIdInputLabel.setText("Please enter your ID:");
        managerIdInputLabel.setFont(Font.font("verdana", 20));

        TextArea managerIdInput = new TextArea();
        managerIdInput.setPrefSize(300, 20);
        managerIdInput.setFont(Font.font("verdana"));

        Text managerPinInputLabel = new Text();
        managerPinInputLabel.setText("Please enter your manager PIN:");
        managerPinInputLabel.setFont(Font.font("verdana", 20));

        TextArea managerPinInput = new TextArea();
        managerPinInput.setPrefSize(300, 20);
        managerPinInput.setFont(Font.font("verdana"));

        Group loginRoot = new Group(login,
                backToStart,
                managerIdInputLabel,
                managerIdInput,
                managerPinInputLabel,
                managerPinInput);

        Scene loginScene = new Scene(loginRoot, 1080, 720);

        /*
         * This part is where all the components get placed correctly.
         * Order is similar to how the components were initialized.
         *
         *
         */
        startOrder.layoutXProperty()
                .bind(firstScene.widthProperty().divide(2).subtract(startOrder.widthProperty().divide(2)));
        startOrder.layoutYProperty()
                .bind(firstScene.heightProperty().divide(2).subtract(startOrder.heightProperty().divide(2)));

        goToLogin.layoutXProperty().bind(startOrder.layoutXProperty());
        goToLogin.layoutYProperty().bind(startOrder.layoutYProperty().subtract(100));

        idInputLabel.layoutXProperty().bind(startOrder.layoutXProperty());
        idInputLabel.layoutYProperty().bind(startOrder.layoutYProperty().add(100));

        employeeIdInput.layoutXProperty().bind(startOrder.layoutXProperty());
        employeeIdInput.layoutYProperty().bind(startOrder.layoutYProperty().add(120));

        background.layoutXProperty().bind(firstScene.widthProperty().subtract(firstScene.widthProperty()));
        background.layoutYProperty().bind(firstScene.heightProperty().subtract(firstScene.heightProperty()));
        background.heightProperty().bind(firstScene.heightProperty());
        background.widthProperty().bind(firstScene.widthProperty());

        decoration1.layoutXProperty().bind(startOrder.layoutXProperty().subtract(50));
        decoration1.layoutYProperty().bind(firstScene.heightProperty().subtract(firstScene.heightProperty()));
        decoration1.heightProperty().bind(firstScene.heightProperty());

        decoration2.layoutXProperty().bind(decoration1.layoutXProperty().subtract(100));
        decoration2.layoutYProperty().bind(firstScene.heightProperty().subtract(firstScene.heightProperty()));
        decoration2.heightProperty().bind(firstScene.heightProperty());

        decoration3.layoutXProperty().bind(decoration1.layoutXProperty().add(decoration1.widthProperty()));
        decoration3.layoutYProperty().bind(firstScene.heightProperty().subtract(firstScene.heightProperty()));
        decoration3.heightProperty().bind(firstScene.heightProperty());

        background2.layoutXProperty().bind(orderingScene.widthProperty().subtract(orderingScene.widthProperty()));
        background2.layoutYProperty().bind(orderingScene.heightProperty().subtract(orderingScene.heightProperty()));
        background2.heightProperty().bind(orderingScene.heightProperty());
        background2.widthProperty().bind(orderingScene.widthProperty());

        bottomBar.layoutYProperty().bind(orderingScene.heightProperty().subtract(bottomBar.heightProperty()));
        bottomBar.widthProperty().bind(orderingScene.widthProperty());

        subtotalBar.layoutXProperty().bind(orderingScene.widthProperty().subtract(subtotalBar.widthProperty()));
        subtotalBar.heightProperty().bind(orderingScene.heightProperty());

        scrollPane.layoutXProperty().bind(orderingScene.xProperty());
        scrollPane.layoutYProperty().bind(orderingScene.yProperty());

        for (int i = 0; i < orderButtons.size(); i++) {
            orderButtons.get(i).layoutXProperty().bind(scrollPane.layoutXProperty().add((150 * (i % 4))));
            orderButtons.get(i).layoutYProperty().bind(scrollPane.layoutYProperty().add(150 * (i / 4)));
        }

        subtotal.layoutXProperty().bind(orderingScene.widthProperty().subtract(subtotalBar.widthProperty()).add(10));
        subtotal.layoutYProperty().bind(orderingScene.heightProperty().subtract(subtotalBar.heightProperty()).add(30));
        subtotal.setText("Subtotal \n================ \n" +
                mapToString(orderMap));

        checkout.layoutXProperty().bind(orderingScene.widthProperty().subtract(checkout.widthProperty()));
        checkout.layoutYProperty().bind(orderingScene.heightProperty().subtract(checkout.heightProperty()));

        background3.layoutXProperty().bind(checkoutScene.widthProperty().subtract(checkoutScene.widthProperty()));
        background3.layoutYProperty().bind(checkoutScene.heightProperty().subtract(checkoutScene.heightProperty()));
        background3.heightProperty().bind(checkoutScene.heightProperty());
        background3.widthProperty().bind(checkoutScene.widthProperty());

        decoration4.layoutXProperty().bind(checkoutScene.widthProperty().subtract(checkoutScene.widthProperty()));
        decoration4.layoutYProperty().bind(checkoutScene.heightProperty().subtract(checkoutScene.heightProperty()));
        decoration4.heightProperty().bind(checkoutScene.heightProperty());

        bottomBar2.layoutYProperty().bind(checkoutScene.heightProperty().subtract(bottomBar2.heightProperty()));
        bottomBar2.widthProperty().bind(checkoutScene.widthProperty());

        backButton.layoutXProperty().bind(checkoutScene.widthProperty().subtract(checkoutScene.widthProperty()));
        backButton.layoutYProperty().bind(checkoutScene.heightProperty().subtract(backButton.heightProperty()));

        finishOrder.layoutXProperty().bind(checkoutScene.widthProperty().subtract(checkout.widthProperty()));
        finishOrder.layoutYProperty().bind(checkoutScene.heightProperty().subtract(checkout.heightProperty()));

        checkoutTotal.layoutXProperty().bind(checkoutScene.xProperty().add(50));
        checkoutTotal.layoutYProperty().bind(checkoutScene.yProperty().add(50));

        login.layoutXProperty().bind(loginScene.widthProperty().divide(2).subtract(login.widthProperty().divide(2)));
        login.layoutYProperty().bind(loginScene.heightProperty().divide(2).subtract(login.heightProperty().divide(2)));

        backToStart.layoutXProperty().bind(login.layoutXProperty());
        backToStart.layoutYProperty().bind(login.layoutYProperty().subtract(100));

        managerIdInputLabel.layoutXProperty().bind(login.layoutXProperty());
        managerIdInputLabel.layoutYProperty().bind(login.layoutYProperty().add(100));

        managerIdInput.layoutXProperty().bind(login.layoutXProperty());
        managerIdInput.layoutYProperty().bind(login.layoutYProperty().add(120));

        managerPinInputLabel.layoutXProperty().bind(managerIdInput.layoutXProperty());
        managerPinInputLabel.layoutYProperty().bind(managerIdInput.layoutYProperty().add(100));

        managerPinInput.layoutXProperty().bind(managerIdInput.layoutXProperty());
        managerPinInput.layoutYProperty().bind(managerIdInput.layoutYProperty().add(120));

        /*
         * This is where the events for buttons are handled
         *
         */
        startOrder.setOnAction(e -> {
            stage.setScene(orderingScene);
            orderNum = database.createNewOrder(Integer.parseInt(employeeIdInput.getText()));
            for (Integer key : orderMap.keySet()) {
                orderMap.put(key, 0);
            }
            subtotal.setText("Subtotal \n================ \n");
        });

        goToLogin.setOnAction(e -> {
            stage.setScene(loginScene);
        });

        for (int i = 0; i < orderButtons.size(); i++) {
            orderMap.put(i + 1, 0);
        }

        for (int i = 0; i < orderButtons.size(); i++) {
            final int temp = i;
            orderButtons.get(temp).setOnMouseClicked(e -> {
                if (e.getButton() == MouseButton.PRIMARY) {
                    orderMap.put(temp + 1, orderMap.get(temp + 1) + 1);
                    database.addToOrder(orderNum, temp + 1);
                    subtotal.setText("Subtotal \n================ \n" + mapToString(orderMap));
                }
                if (e.getButton() == MouseButton.SECONDARY && orderMap.get(temp + 1) > 0) {
                    orderMap.put(temp + 1, orderMap.get(temp + 1) - 1);
                    database.removeFromOrder(orderNum, temp + 1);
                    subtotal.setText("Subtotal \n================ \n" + mapToString(orderMap));
                }
            });
        }

        checkout.setOnAction(e -> {
            stage.setScene(checkoutScene);
            checkoutTotal.setText("Final Order: \n================ \n" + mapToStringCheckout(orderMap) + "\nTotal - $"
                    + database.getOrderSubtotal(orderNum));
        });

        backButton.setOnAction(e -> stage.setScene(orderingScene));
        finishOrder.setOnAction(e -> {
            stage.setScene(firstScene);
        });

        backToStart.setOnAction(e -> {
            stage.setScene(firstScene);
        });

        login.setOnAction(e -> {
            if (database.checkManagerPIN(Integer.parseInt(managerIdInput.getText()), managerPinInput.getText()) == 1) {
                stage.setScene(getManagementStartingScene(stage));
                System.out.print(database.checkManagerPIN(Integer.parseInt(managerIdInput.getText()),
                        managerPinInput.getText()));
            }
        });

        /*
         * Run the GUI
         *
         */
        stage.setTitle("Boba Tea POS v1.0.0");
        stage.setScene(firstScene);
        stage.show();
    }

    /**
     * Convert the map containing orders to the text in the subtotal area
     *
     * @author Dylan Nguyen
     * @param map
     * @return
     */
    private String mapToString(Map<Integer, Integer> map) {
        String temp = "";
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i + 1) > 0) {
                temp = temp + "$" + (database.getItemPrice(i + 1) * map.get(i + 1)) + " - "
                        + database.getItemName(i + 1) + ": " + map.get(i + 1) + "\n";
            }
        }
        return temp;
    }

    /**
     * Convert the map containing orders to the text in the checkout area
     *
     * @author Dylan Nguyen
     * @param map
     * @return
     */
    private String mapToStringCheckout(Map<Integer, Integer> map) {
        String temp = "";
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i + 1) > 0) {
                temp = temp + "$" + (database.getItemPrice(i + 1) * map.get(i + 1)) + " - " + map.get(i + 1) + "x "
                        + database.getItemName(i + 1) + "\n";
            }
        }
        return temp;
    }

    /**
     * Create all necessary buttons
     *
     * @author Dylan Nguyen
     * @param idNum
     * @return Button
     */
    private Button createButton(int idNum) {
        Button button = new Button();
        button.setText(database.getItemName(idNum));
        button.setPrefSize(150, 150);
        button.setContentDisplay(ContentDisplay.TOP);

        return button;
    }

    // management stuff starts here
    /**
     * @author Rene Almeida
     * @param stage The stage to display the management UI
     * @return The scene for the management UI
     */
    private Scene getManagementStartingScene(Stage stage) {
        // ManagementUI components
        HBox topMenu = createTopMenu(stage);

        BorderPane root = new BorderPane();
        root.setTop(topMenu);

        int firstSupplierID = getFirstSupplierID();
        categoryContent = new VBox();
        root.setCenter(createScrollableSection(createDeliverySection(firstSupplierID)));
        Scene mainScene = new Scene(root, 800, 600);

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

        return mainScene;
    }

    /**
     * Create the scroll pane for the management UI allowing for scrolling
     * 
     * @param content The content to be displayed in the scroll pane
     * @return The scroll pane for the management UI
     */
    private ScrollPane createScrollableSection(VBox content) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    /**
     * Gets the first supplier ID from the database to display the first supplier's
     * content on either the delivery or count inventory sections
     * 
     * @return The first supplier ID
     */
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
        return -1;
    }

    // creates the top nav bar for all the categroies (delivery, count inventory,
    // reports, employees, menu)
    /**
     * Creates the top navigation bar for the management UI
     * 
     * @param primaryStage The stage to display the management UI
     * @return The top navigation bar for the management UI
     */
    private HBox createTopMenu(Stage primaryStage) {
        HBox topMenu = new HBox(10);
        topMenu.setStyle("-fx-background-color:rgb(202, 103, 53); -fx-padding: 10;"); // CSS styling for nav bar

        Button deliveryButton = new Button("Delivery");
        Button countInventoryButton = new Button("Count Inventory");
        Button reportsButton = new Button("Reports");
        Button employeesButton = new Button("Employees");
        Button menuButton = new Button("Menu");

        // CSS for buttons
        String buttonStyle = "-fx-background-color: rgb(84, 10, 10); -fx-text-fill: rgb(255, 255, 255); -fx-font-size: 16px; -fx-padding: 10;";
        deliveryButton.setStyle(buttonStyle);
        countInventoryButton.setStyle(buttonStyle);
        reportsButton.setStyle(buttonStyle);
        employeesButton.setStyle(buttonStyle);
        menuButton.setStyle(buttonStyle);

        topMenu.getChildren().addAll(deliveryButton, countInventoryButton, reportsButton, employeesButton, menuButton);
        return topMenu;
    }

    /**
     * Updates the inventory quantity for the count inventory section
     * 
     * @param itemName    The name of the item to update
     * @param newQuantity The new quantity to set for the item
     */
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

    private Map<String, Integer> supplierMap = new HashMap<>(); // Store supplier name -> ID mapping

    // Create the supplier dropdown menu for the management UI
    /**
     * Creates the supplier dropdown menu for the management UI
     * 
     * @param updateFunction The function to call when the supplier is changed
     * @return HBox containing the supplier dropdown menu
     */
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

    // Update the inventory content for the delivery section
    /**
     * Loads inventory items from the suppliers to use for the delivery section
     * 
     * @param deliveryContent The VBox to display the inventory items
     * @param supplierId      The supplier ID to load the inventory items from
     */
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

    // Create the Delivery Tab for the management UI
    /**
     * Creates the delivery section for the management UI
     * 
     * @param supplierID The supplier ID to display the delivery section for
     * @return VBox containing the delivery section
     */
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
    /**
     * Updates the inventory quantity for the delivery section
     * 
     * @param supplierId    The supplier ID to update the inventory for
     * @param itemName      The name of the item to update
     * @param inputQuantity The new quantity to set for the item
     */
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

    // create the count inventory section for the management UI
    /**
     * Creates the count inventory section for the management UI
     * 
     * @param supplierID The supplier ID to display the inventory for
     * @return The count inventory section for the management UI
     */
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
    /**
     * Updates the inventory quantity for the count inventory section
     * 
     * @param supplierId  The supplier ID to update the inventory for
     * @param itemName    The name of the item to update
     * @param newQuantity The new quantity to set for the item
     */
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
    /**
     * Updates the inventory content for the count inventory section
     * 
     * @param inventoryContent The content to be displayed in the count inventory
     * @param supplierId       The supplier ID to display the inventory for
     */
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

    // create the reports tab for the management UI, with a bar graph showing the
    // usage of different inventory items, I spent all
    // night working on this UI please help me
    /**
     * Creates the reports section for the management UI
     * 
     * @return The reports section for the management UI
     */
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
        trendsGraph.setTitle("Usage of Inventory over Time Period Selected");

        layout.getChildren().addAll(trendsGraph, new Label("Key:"));

        Button xReportButton = new Button("Generate X Report");
        Button zReportButton = new Button("Generate Z Report");

        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setPrefHeight(200);

        xReportButton
                .setOnAction(e -> generateXReport(reportArea));
        zReportButton
                .setOnAction(e -> generateZReport(reportArea));

        // Show data on graph over selected time frame
        Button updateGraphButton = new Button("Update Graph");
        updateGraphButton
                .setOnAction(e -> updateGraph(startDatePicker.getValue(), endDatePicker.getValue(), trendsGraph));

        layout.getChildren().addAll(updateGraphButton, xReportButton, zReportButton, reportArea);

        return layout;
    }

    // display data on the graph for the inputted time frame
    /**
     * Updates the graph with the inventory usage data for the selected time frame
     * 
     * @param startDate   The start date for the time frame
     * @param endDate     The end date for the time frame
     * @param trendsGraph The graph to display the inventory usage data
     */
    private void updateGraph(LocalDate startDate, LocalDate endDate, BarChart<String, Number> trendsGraph) {
        if (startDate == null || endDate == null) {
            System.out.println("Please select both start and end dates.");
            return;
        }

        String query = "SELECT * FROM get_inventory_usage (?, ?)";

        try (Connection conn = DatabaseConnection.connect();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setTimestamp(1, Timestamp.valueOf(startDate.atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate.atTime(LocalTime.MAX)));
            ResultSet rs = stmt.executeQuery();

            trendsGraph.getData().clear();

            Map<String, XYChart.Series<String, Number>> seriesMap = new HashMap<>();

            while (rs.next()) {
                String itemName = rs.getString("item_name");
                int totalUsed = rs.getInt("total_used");

                XYChart.Series<String, Number> series = seriesMap.get(itemName);
                if (series == null) {
                    series = new XYChart.Series<>();
                    series.setName(itemName);
                    seriesMap.put(itemName, series);
                    trendsGraph.getData().add(series);
                }

                series.getData().add(new XYChart.Data<>(itemName, totalUsed));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // generate an x report using methods from Database.java
    /**
     * Generates an X report for the management UI
     * 
     * @param reportArea The text area to display the X report
     */
    private void generateXReport(TextArea reportArea) {
        try {
            Map<String, Double> salesPerHour = database.getSalesPerHour();
            StringBuilder report = new StringBuilder("X Report - Sales Per Hour:\n");
            report.append("Boba Tea Store\n");
            for (Map.Entry<String, Double> entry : salesPerHour.entrySet()) {
                report.append(entry.getKey()).append(": $").append(entry.getValue()).append("\n");
            }
            report.append("Percentage of Credit Card Sales: 100%\n");
            report.append("Percentage of Cash Sales: 0%\n");
            reportArea.setText(report.toString());
        } catch (Exception e) {
            e.printStackTrace();
            reportArea.setText("Error generating X report: " + e.getMessage());
        }
    }

    // generate a z report using methods from Database.java
    /**
     * Generates a Z report for the management UI
     * 
     * @param reportArea The text area to display the Z report
     */
    private void generateZReport(TextArea reportArea) {
        try {
            double totalSales = database.getTotalSales();
            float totalTax = (float) (totalSales * 0.0825);
            StringBuilder report = new StringBuilder("Z Report - Total Sales:\n");
            report.append("Boba Tea Store\n");
            report.append("123 Boba Street, Galveston TX\n");
            report.append("POS-01\n");
            report.append("Reg-07\n");
            report.append("Total Sales: $").append(totalSales).append("\n");
            report.append("Total Tax: $" + totalTax).append("\n");
            report.append("Total Credit Card Sales: $").append(totalSales).append("\n");
            report.append("Total Cash Sales: $0.00").append("\n");
            reportArea.setText(report.toString());
        } catch (Exception e) {
            e.printStackTrace();
            reportArea.setText("Error generating Z report: " + e.getMessage());
        }
    }

    // create tab for employees, with sections for adding new employees and viewing
    // existing ones
    /**
     * Creates the employees section for the management UI
     * 
     * @return The employees section
     */
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
    /**
     * Updates the first and last name of an employee
     * 
     * @param employeeId   The ID of the employee to update
     * @param newFirstName The new first name
     * @param newLastName  The new last name
     */
    private void updateCurrentEmployee(int employeeId, String newFirstName, String newLastName) {
        database.updateEmployeeFirstName(employeeId, newFirstName);
        database.updateEmployeeLastName(employeeId, newLastName);
        System.out.println("Updated employee ID " + employeeId);
        updateMenuList();
    }

    // Updates the employee list in the UI (once a new employee is added, refreshes
    // it and shows the updated list)
    /**
     * Updates the employee list in the management UI
     */
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
    /**
     * Creates the menu section for the management UI
     * 
     * @return The menu section
     */
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
    /**
     * Adds a new menu item to the database
     * 
     * @param itemName
     * @param price
     */
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

    /**
     * Updates the price of a menu item
     * 
     * @param itemName
     * @param newPrice
     */
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

    /**
     * Updates the menu list in the management UI
     */
    private void updateMenuList() {
        createMenuSection(); // Refresh UI
    }

    public static void main(String[] args) {
        launch();
    }
}