package cashier;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

public class POSApplication extends Application {
    ArrayList<String> orderList = new ArrayList<>();
    ArrayList<Integer> orderIDList = new ArrayList<>();

    int orderNum;

    Database database = new Database();

    @Override
    public void start(Stage stage) throws IOException {

        Button startOrder = new Button();
        startOrder.setText("Start order");
        startOrder.setPrefSize(300, 50);

        // first group
        Group firstRoot = new Group(startOrder);

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
        Button button1 = new Button();
        button1.setText(database.getItemName(1));
        button1.setPrefSize(150, 150);
        button1.setContentDisplay(ContentDisplay.TOP);
        Image button1Image = new Image("file:images/strawberry-lemonade-smoothie.jpg");
        ImageView button1ImageView = new ImageView(button1Image);
        button1ImageView.setPreserveRatio(true);
        button1ImageView.setFitHeight(button1.getPrefHeight() - 50);
        button1ImageView.setFitWidth(button1.getPrefWidth());
        button1.setGraphic(button1ImageView);

        Button button2 = new Button();
        button2.setText(database.getItemName(2));
        button2.setPrefSize(150, 150);
        button2.setContentDisplay(ContentDisplay.TOP);

        Image button2Image = new Image("file:images/mango-smoothie.jpg");
        ImageView button2ImageView = new ImageView(button2Image);
        button2ImageView.setPreserveRatio(true);
        button2ImageView.setFitHeight(button2.getPrefHeight() - 50);
        button2ImageView.setFitWidth(button2.getPrefWidth());
        button2.setGraphic(button2ImageView);

        Button button3 = new Button();
        button3.setText(database.getItemName(3));
        button3.setPrefSize(150, 150);
        button3.setContentDisplay(ContentDisplay.TOP);

        Image button3Image = new Image("file:images/almond-milk-tea.jpg");
        ImageView button3ImageView = new ImageView(button3Image);
        button3ImageView.setPreserveRatio(true);
        button3ImageView.setFitHeight(button3.getPrefHeight() - 50);
        button3ImageView.setFitWidth(button3.getPrefWidth());
        button3.setGraphic(button3ImageView);

        Button button4 = new Button();
        button4.setText(database.getItemName(4));
        button4.setPrefSize(150, 150);
        button4.setContentDisplay(ContentDisplay.TOP);

        Image button4Image = new Image("file:images/caramel-milk-tea.jpg");
        ImageView button4ImageView = new ImageView(button4Image);
        button4ImageView.setPreserveRatio(true);
        button4ImageView.setFitHeight(button4.getPrefHeight() - 50);
        button4ImageView.setFitWidth(button4.getPrefWidth());
        button4.setGraphic(button4ImageView);

        Button button5 = new Button();
        button5.setText(database.getItemName(5));
        button5.setPrefSize(150, 150);
        button5.setContentDisplay(ContentDisplay.TOP);

        Image button5Image = new Image("file:images/brown-sugar-milk-tea.jpg");
        ImageView button5ImageView = new ImageView(button5Image);
        button5ImageView.setPreserveRatio(true);
        button5ImageView.setFitHeight(button5.getPrefHeight() - 50);
        button5ImageView.setFitWidth(button5.getPrefWidth());
        button5.setGraphic(button5ImageView);

        Button button6 = new Button();
        button6.setText(database.getItemName(6));
        button6.setPrefSize(150, 150);
        button6.setContentDisplay(ContentDisplay.TOP);

        Image button6Image = new Image("file:images/lychee-milk-tea.jpg");
        ImageView button6ImageView = new ImageView(button6Image);
        button6ImageView.setPreserveRatio(true);
        button6ImageView.setFitHeight(button6.getPrefHeight() - 50);
        button6ImageView.setFitWidth(button6.getPrefWidth());
        button6.setGraphic(button6ImageView);

        Button button7 = new Button();
        button7.setText(database.getItemName(7));
        button7.setPrefSize(150, 150);
        button7.setContentDisplay(ContentDisplay.TOP);

        Image button7Image = new Image("file:images/vanilla-milk-tea.jpg");
        ImageView button7ImageView = new ImageView(button7Image);
        button7ImageView.setPreserveRatio(true);
        button7ImageView.setFitHeight(button7.getPrefHeight() - 50);
        button7ImageView.setFitWidth(button7.getPrefWidth());
        button7.setGraphic(button7ImageView);

        Button button8 = new Button();
        button8.setText(database.getItemName(8));
        button8.setPrefSize(150, 150);
        button8.setContentDisplay(ContentDisplay.TOP);

        Image button8Image = new Image("file:images/taro-milk-tea.jpeg");
        ImageView button8ImageView = new ImageView(button8Image);
        button8ImageView.setPreserveRatio(true);
        button8ImageView.setFitHeight(button8.getPrefHeight() - 50);
        button8ImageView.setFitWidth(button8.getPrefWidth());
        button8.setGraphic(button8ImageView);

        Button button9 = new Button();
        button9.setText(database.getItemName(9));
        button9.setPrefSize(150, 150);
        button9.setContentDisplay(ContentDisplay.TOP);

        Image button9Image = new Image("file:images/chocolate-milk-tea.jpg");
        ImageView button9ImageView = new ImageView(button9Image);
        button9ImageView.setPreserveRatio(true);
        button9ImageView.setFitHeight(button9.getPrefHeight() - 50);
        button9ImageView.setFitWidth(button9.getPrefWidth());
        button9.setGraphic(button9ImageView);

        Button button10 = new Button();
        button10.setText(database.getItemName(10));
        button10.setPrefSize(150, 150);
        button10.setContentDisplay(ContentDisplay.TOP);

        Image button10Image = new Image("file:images/black-tea.jpg");
        ImageView button10ImageView = new ImageView(button10Image);
        button10ImageView.setPreserveRatio(true);
        button10ImageView.setFitHeight(button10.getPrefHeight() - 50);
        button10ImageView.setFitWidth(button10.getPrefWidth());
        button10.setGraphic(button10ImageView);

        Button button11 = new Button();
        button11.setText(database.getItemName(11));
        button11.setPrefSize(150, 150);
        button11.setContentDisplay(ContentDisplay.TOP);

        Image button11Image = new Image("file:images/honeydew-milk-tea.jpg");
        ImageView button11ImageView = new ImageView(button11Image);
        button11ImageView.setPreserveRatio(true);
        button11ImageView.setFitHeight(button11.getPrefHeight() - 50);
        button11ImageView.setFitWidth(button11.getPrefWidth());
        button11.setGraphic(button11ImageView);

        Button button12 = new Button();
        button12.setText(database.getItemName(12));
        button12.setPrefSize(150, 150);
        button12.setContentDisplay(ContentDisplay.TOP);

        Image button12Image = new Image("file:images/coffee-milk-tea.jpg");
        ImageView button12ImageView = new ImageView(button12Image);
        button12ImageView.setPreserveRatio(true);
        button12ImageView.setFitHeight(button12.getPrefHeight() - 50);
        button12ImageView.setFitWidth(button12.getPrefWidth());
        button12.setGraphic(button12ImageView);

        Button button13 = new Button();
        button13.setText(database.getItemName(13));
        button13.setPrefSize(150, 150);
        button13.setContentDisplay(ContentDisplay.TOP);

        Image button13Image = new Image("file:images/matcha-milk-tea.jpg");
        ImageView button13ImageView = new ImageView(button13Image);
        button13ImageView.setPreserveRatio(true);
        button13ImageView.setFitHeight(button13.getPrefHeight() - 50);
        button13ImageView.setFitWidth(button13.getPrefWidth());
        button13.setGraphic(button13ImageView);

        Button button14 = new Button();
        button14.setText(database.getItemName(14));
        button14.setPrefSize(150, 150);
        button14.setContentDisplay(ContentDisplay.TOP);

        Image button14Image = new Image("file:images/oreo-milk-tea.jpg");
        ImageView button14ImageView = new ImageView(button14Image);
        button14ImageView.setPreserveRatio(true);
        button14ImageView.setFitHeight(button14.getPrefHeight() - 50);
        button14ImageView.setFitWidth(button14.getPrefWidth());
        button14.setGraphic(button14ImageView);

        Button button15 = new Button();
        button15.setText(database.getItemName(15));
        button15.setPrefSize(150, 150);
        button15.setContentDisplay(ContentDisplay.TOP);

        Image button15Image = new Image("file:images/peach-milk-tea.jpg");
        ImageView button15ImageView = new ImageView(button15Image);
        button15ImageView.setPreserveRatio(true);
        button15ImageView.setFitHeight(button15.getPrefHeight() - 50);
        button15ImageView.setFitWidth(button15.getPrefWidth());
        button15.setGraphic(button15ImageView);

        Button button16 = new Button();
        button16.setText(database.getItemName(16));
        button16.setPrefSize(150, 150);
        button16.setContentDisplay(ContentDisplay.TOP);

        Image button16Image = new Image("file:images/coconut-milk-tea.jpg");
        ImageView button16ImageView = new ImageView(button16Image);
        button16ImageView.setPreserveRatio(true);
        button16ImageView.setFitHeight(button16.getPrefHeight() - 50);
        button16ImageView.setFitWidth(button16.getPrefWidth());
        button16.setGraphic(button16ImageView);

        Button button17 = new Button();
        button17.setText(database.getItemName(17));
        button17.setPrefSize(150, 150);
        button17.setContentDisplay(ContentDisplay.TOP);

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

        if(database.getItemName(1) != ""){
            root.getChildren().add(button1);
        }
        if(database.getItemName(2) != ""){
            root.getChildren().add(button2);
        }
        if(database.getItemName(3) != ""){
            root.getChildren().add(button3);
        }
        if(database.getItemName(4) != ""){
            root.getChildren().add(button4);
        }
        if(database.getItemName(5) != ""){
            root.getChildren().add(button5);
        }
        if(database.getItemName(6) != ""){
            root.getChildren().add(button6);
        }
        if(database.getItemName(7) != ""){
            root.getChildren().add(button7);
        }
        if(database.getItemName(8) != ""){
            root.getChildren().add(button8);
        }
        if(database.getItemName(9) != ""){
            root.getChildren().add(button9);
        }
        if(database.getItemName(10) != ""){
            root.getChildren().add(button10);
        }
        if(database.getItemName(11) != ""){
            root.getChildren().add(button11);
        }
        if(database.getItemName(12) != ""){
            root.getChildren().add(button12);
        }
        if(database.getItemName(13) != ""){
            root.getChildren().add(button13);
        }
        if(database.getItemName(14) != ""){
            root.getChildren().add(button14);
        }
        if(database.getItemName(15) != ""){
            root.getChildren().add(button15);
        }
        if(database.getItemName(16) != ""){
            root.getChildren().add(button16);
        }
        if(database.getItemName(17) != ""){
            root.getChildren().add(button17);
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

        // checkout group
        Group checkoutRoot = new Group(bottomBar2,
                backButton,
                finishOrder);
        Scene checkoutScene = new Scene(checkoutRoot);

        //place components correctly
        startOrder.layoutXProperty().bind(scene.widthProperty().divide(2).subtract(startOrder.widthProperty().divide(2)));
        startOrder.layoutYProperty().bind(scene.heightProperty().divide(2).subtract(startOrder.heightProperty().divide(2)));

        bottomBar.layoutYProperty().bind(scene.heightProperty().subtract(bottomBar.heightProperty()));
        bottomBar.widthProperty().bind(scene.widthProperty());

        subtotalBar.layoutXProperty().bind(scene.widthProperty().subtract(subtotalBar.widthProperty()));
        subtotalBar.heightProperty().bind(scene.heightProperty());

        button1.layoutXProperty().bind(scene.xProperty().add(20));
        button1.layoutYProperty().bind(scene.yProperty());

        button2.layoutXProperty().bind(button1.layoutXProperty().add(150));
        button2.layoutYProperty().bind(button1.layoutYProperty());

        button3.layoutXProperty().bind(button2.layoutXProperty().add(150));
        button3.layoutYProperty().bind(button2.layoutYProperty());

        button4.layoutXProperty().bind(button3.layoutXProperty().add(150));
        button4.layoutYProperty().bind(button3.layoutYProperty());

        button5.layoutXProperty().bind(button1.layoutXProperty());
        button5.layoutYProperty().bind(button1.layoutYProperty().add(150));

        button6.layoutXProperty().bind(button5.layoutXProperty().add(150));
        button6.layoutYProperty().bind(button5.layoutYProperty());

        button7.layoutXProperty().bind(button6.layoutXProperty().add(150));
        button7.layoutYProperty().bind(button6.layoutYProperty());

        button8.layoutXProperty().bind(button7.layoutXProperty().add(150));
        button8.layoutYProperty().bind(button7.layoutYProperty());

        button9.layoutXProperty().bind(button5.layoutXProperty());
        button9.layoutYProperty().bind(button5.layoutYProperty().add(150));

        button10.layoutXProperty().bind(button9.layoutXProperty().add(150));
        button10.layoutYProperty().bind(button9.layoutYProperty());

        button11.layoutXProperty().bind(button10.layoutXProperty().add(150));
        button11.layoutYProperty().bind(button10.layoutYProperty());

        button12.layoutXProperty().bind(button11.layoutXProperty().add(150));
        button12.layoutYProperty().bind(button11.layoutYProperty());

        button13.layoutXProperty().bind(button9.layoutXProperty());
        button13.layoutYProperty().bind(button9.layoutYProperty().add(150));

        button14.layoutXProperty().bind(button13.layoutXProperty().add(150));
        button14.layoutYProperty().bind(button13.layoutYProperty());

        button15.layoutXProperty().bind(button14.layoutXProperty().add(150));
        button15.layoutYProperty().bind(button14.layoutYProperty());

        button16.layoutXProperty().bind(button15.layoutXProperty().add(150));
        button16.layoutYProperty().bind(button15.layoutYProperty());

        button17.layoutXProperty().bind(button13.layoutXProperty());
        button17.layoutYProperty().bind(button13.layoutYProperty().add(150));

        subtotal.layoutXProperty().bind(scene.widthProperty().subtract(subtotalBar.widthProperty()).add(10));
        subtotal.layoutYProperty().bind(scene.heightProperty().subtract(subtotalBar.heightProperty()).add(30));
        subtotal.setText("Subtotal \n" +
                listToString(orderList));

        checkout.layoutXProperty().bind(scene.widthProperty().subtract(checkout.widthProperty()));
        checkout.layoutYProperty().bind(scene.heightProperty().subtract(checkout.heightProperty()));

        bottomBar2.layoutYProperty().bind(scene.heightProperty().subtract(bottomBar2.heightProperty()));
        bottomBar2.widthProperty().bind(scene.widthProperty());

        backButton.layoutXProperty().bind(scene.xProperty());
        backButton.layoutYProperty().bind(scene.heightProperty().subtract(backButton.heightProperty()));

        finishOrder.layoutXProperty().bind(scene.widthProperty().subtract(checkout.widthProperty()));
        finishOrder.layoutYProperty().bind(scene.heightProperty().subtract(checkout.heightProperty()));


        startOrder.setOnAction(e -> {stage.setScene(scene);
            orderNum = database.addOrder(1);
            orderList.clear();
            orderIDList.clear();
            subtotal.setText("Subtotal \n");});

        button1.setOnAction(e -> {orderList.add("Strawberry Lemonade Smoothie");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(1);});
        button2.setOnAction(e -> {orderList.add("Mango Smoothie");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(2);});
        button3.setOnAction(e -> {orderList.add("Almond Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(3);});
        button4.setOnAction(e -> {orderList.add("Caramel Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(4);});
        button5.setOnAction(e -> {orderList.add("Brown Sugar Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(5);});
        button6.setOnAction(e -> {orderList.add("Lychee Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(6);});
        button7.setOnAction(e -> {orderList.add("Vanilla Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(7);});
        button8.setOnAction(e -> {orderList.add("Taro Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(8);});
        button9.setOnAction(e -> {orderList.add("Chocolate Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(9);});
        button10.setOnAction(e -> {orderList.add("Black Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(10);});
        button11.setOnAction(e -> {orderList.add("Honeydew Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(11);});
        button12.setOnAction(e -> {orderList.add("Coffee Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(12);});
        button13.setOnAction(e -> {orderList.add("Matcha Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(13);});
        button14.setOnAction(e -> {orderList.add("Oreo Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(14);});
        button15.setOnAction(e -> {orderList.add("Peach Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(15);});
        button16.setOnAction(e -> {orderList.add("Coconut Milk Tea");
            subtotal.setText("Subtotal \n" + listToString(orderList));
            orderIDList.add(16);});
        checkout.setOnAction(e -> {stage.setScene(checkoutScene);});

        backButton.setOnAction(e -> stage.setScene(scene));
        finishOrder.setOnAction(e -> {addAllItems(orderIDList);
            stage.setScene(firstScene);});

        stage.setTitle("GUI");
        stage.setScene(firstScene);
        stage.show();
    }

    public String listToString(ArrayList<String> list){
        String temp = "";
        for(int i = 0; i < list.size(); i++){
            temp = temp + list.get(i) + "\n";
        }
        return temp;
    }

    public void addAllItems(List<Integer> list){
        for(int i = 0; i < list.size(); i++){
            database.addToOrder(orderNum, list.get(i));
        }
    }

    public static void main(String[] args) {
        launch();
    }
}