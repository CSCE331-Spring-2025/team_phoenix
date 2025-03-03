package cashier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.beans.EventHandler;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Creates the cashier section of the POS system.
 */
public class POSApplication extends Application {
    //ArrayList<Integer> orderIDList = new ArrayList<>();

    Map<Integer, Integer> orderMap = new HashMap<>();

    int orderNum;

    Database database = new Database();

    Map<Integer, String> buttonNameMap = database.getMenuItemNames();

    /**
     * @author Dylan Nguyen
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {

        Button startOrder = new Button();
        startOrder.setText("Start order");
        startOrder.setPrefSize(300, 50);

        Text idInputLabel = new Text();
        idInputLabel.setText("Please enter employee id:");
        idInputLabel.setFont(Font.font("verdana", 20));

        TextArea employeeIdInput = new TextArea();
        employeeIdInput.setPrefSize(300, 20);
        employeeIdInput.setFont(Font.font("verdana"));

        // first group
        Group firstRoot = new Group(startOrder, idInputLabel, employeeIdInput);

        Scene firstScene = new Scene(firstRoot, 1080, 720);


        Rectangle bottomBar = new Rectangle();
        bottomBar.setX(0);
        bottomBar.setHeight(50);
        bottomBar.setFill(Color.LIGHTGRAY);

        Rectangle subtotalBar = new Rectangle();
        subtotalBar.setY(0);
        subtotalBar.setWidth(300);
        subtotalBar.setFill(Color.GRAY);

        // buttons
        ArrayList<Button> orderButtons = new ArrayList<>();
        for(Integer id : buttonNameMap.keySet()){
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

        // Subtotal text
        Text subtotal = new Text();
        subtotal.setFont(Font.font("verdana", 20));
        subtotal.setWrappingWidth(subtotalBar.getWidth() - 10);

        // middle group
        Group root = new Group(bottomBar,
                subtotalBar,
                subtotal,
                checkout);

        for(int i = 0; i < orderButtons.size(); i++){
            if(database.getItemName(i+1) != ""){
                root.getChildren().add(orderButtons.get(i));
            }
        }

        Scene scene = new Scene(root, 1080, 720);

        Rectangle bottomBar2 = new Rectangle();
        bottomBar2.setX(0);
        bottomBar2.setHeight(50);
        bottomBar2.setFill(Color.LIGHTGRAY);

        Button backButton = new Button();
        backButton.setText("Back");
        backButton.setPrefSize(300, 50);

        Button finishOrder = new Button();
        finishOrder.setText("Finish order");
        finishOrder.setPrefSize(300, 50);

        Text checkoutTotal = new Text();
        checkoutTotal.setFont(Font.font("verdana", 20));
        checkoutTotal.setWrappingWidth(scene.getWidth());

        // checkout group
        Group checkoutRoot = new Group(bottomBar2,
                backButton,
                finishOrder,
                checkoutTotal);
        Scene checkoutScene = new Scene(checkoutRoot);

        //place components correctly
        startOrder.layoutXProperty().bind(scene.widthProperty().divide(2).subtract(startOrder.widthProperty().divide(2)));
        startOrder.layoutYProperty().bind(scene.heightProperty().divide(2).subtract(startOrder.heightProperty().divide(2)));

        idInputLabel.layoutXProperty().bind(startOrder.layoutXProperty());
        idInputLabel.layoutYProperty().bind(startOrder.layoutYProperty().add(100));

        employeeIdInput.layoutXProperty().bind(startOrder.layoutXProperty());
        employeeIdInput.layoutYProperty().bind(startOrder.layoutYProperty().add(120));

        bottomBar.layoutYProperty().bind(scene.heightProperty().subtract(bottomBar.heightProperty()));
        bottomBar.widthProperty().bind(scene.widthProperty());

        subtotalBar.layoutXProperty().bind(scene.widthProperty().subtract(subtotalBar.widthProperty()));
        subtotalBar.heightProperty().bind(scene.heightProperty());


        for(int i = 0; i < orderButtons.size(); i++){
            orderButtons.get(i).layoutXProperty().bind(scene.xProperty().add(20 + (150 * (i % 4))));
            orderButtons.get(i).layoutYProperty().bind(scene.yProperty().add(150 * (i / 4)));
        }

        subtotal.layoutXProperty().bind(scene.widthProperty().subtract(subtotalBar.widthProperty()).add(10));
        subtotal.layoutYProperty().bind(scene.heightProperty().subtract(subtotalBar.heightProperty()).add(30));
        subtotal.setText("Subtotal \n" +
                mapToString(orderMap));

        checkout.layoutXProperty().bind(scene.widthProperty().subtract(checkout.widthProperty()));
        checkout.layoutYProperty().bind(scene.heightProperty().subtract(checkout.heightProperty()));

        bottomBar2.layoutYProperty().bind(scene.heightProperty().subtract(bottomBar2.heightProperty()));
        bottomBar2.widthProperty().bind(scene.widthProperty());

        backButton.layoutXProperty().bind(scene.xProperty());
        backButton.layoutYProperty().bind(scene.heightProperty().subtract(backButton.heightProperty()));

        finishOrder.layoutXProperty().bind(scene.widthProperty().subtract(checkout.widthProperty()));
        finishOrder.layoutYProperty().bind(scene.heightProperty().subtract(checkout.heightProperty()));

        checkoutTotal.layoutXProperty().bind(scene.xProperty());
        checkoutTotal.layoutYProperty().bind(scene.yProperty());


        startOrder.setOnAction(e -> {stage.setScene(scene);
            orderNum = database.createNewOrder(Integer.parseInt(employeeIdInput.getText()));
            for(Integer key : orderMap.keySet()){
                orderMap.put(key, 0);
            }
            //orderIDList.clear();
            subtotal.setText("Subtotal \n");});

        for(int i = 0; i < orderButtons.size(); i++){
            orderMap.put(i + 1, 0);
        }

        for(int i = 0; i < orderButtons.size(); i++){
            final int temp = i;
            orderButtons.get(temp).setOnMouseClicked(e -> {
                if(e.getButton() == MouseButton.PRIMARY){
                    //orderList.add(database.getItemName(temp+1));
                    orderMap.put(temp+1, orderMap.get(temp+1) + 1);
                    database.addToOrder(orderNum, temp+1);
                    subtotal.setText("Subtotal \n" + mapToString(orderMap));
                    //orderIDList.add(temp+1);
                }
                if(e.getButton() == MouseButton.SECONDARY && orderMap.get(temp+1) > 0){
                    orderMap.put(temp+1, orderMap.get(temp+1) - 1);
                    database.removeFromOrder(orderNum, temp+1);
                    subtotal.setText("Subtotal \n" + mapToString(orderMap));
                    //orderIDList.add(temp+1);
                }
            });
        }

        checkout.setOnAction(e -> {stage.setScene(checkoutScene);
            checkoutTotal.setText("Total: \n" + mapToStringCheckout(orderMap));});

        backButton.setOnAction(e -> stage.setScene(scene));
        finishOrder.setOnAction(e -> {stage.setScene(firstScene);});

        stage.setTitle("GUI");
        stage.setScene(firstScene);
        stage.show();
    }

    /** Convert the map containing orders to the text in the subtotal area
     *
     * @author Dylan Nguyen
     * @param map
     * @return
     */
    private String mapToString(Map<Integer, Integer> map){
        String temp = "";
        for(int i = 0; i < map.size(); i++){
            if(map.get(i + 1) > 0){
                temp = temp + "$" + (database.getItemPrice(i + 1) * map.get(i + 1)) + " - " + database.getItemName(i + 1) + ": " + map.get(i + 1) + "\n";
            }
        }
        return temp;
    }

    /** Convert the map containing orders to the text in the checkout area
     *
     * @author Dylan Nguyen
     * @param map
     * @return
     */
    private String mapToStringCheckout(Map<Integer, Integer> map){
        String temp = "";
        for(int i = 0; i < map.size(); i++){
            if(map.get(i + 1) > 0){
                temp = temp + "$" + (database.getItemPrice(i + 1) * map.get(i + 1)) + " - " + map.get(i + 1) + "x " + database.getItemName(i + 1) + "\n";
            }
        }
        return temp;
    }

    /** Create all necessary buttons
     *
     * @author Dylan Nguyen
     * @param idNum
     * @return Button
     */
    private Button createButton(int idNum){
        Button button = new Button();
        button.setText(database.getItemName(idNum));
        button.setPrefSize(150, 150);
        button.setContentDisplay(ContentDisplay.TOP);

        return button;
    }

    public static void main(String[] args) {
        launch();
    }
}