package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MenuController {

    @FXML
    StackPane stackPane;

    @FXML
    Button newGame;
    @FXML
    Button highScores;
    @FXML
    Button exit;

    @FXML
    public void newGame() {
    }

    @FXML
    public void highScores() {
    }

    @FXML
    public void exit() {
        Platform.exit();
    }

}
