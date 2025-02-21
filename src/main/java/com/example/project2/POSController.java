package com.example.project2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class POSController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("");
    }
}