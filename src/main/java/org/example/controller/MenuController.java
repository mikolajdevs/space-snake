package org.example.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.example.Utils;

import java.io.IOException;

import static org.example.Utils.GAME;
import static org.example.Utils.HIGH_SCORES;

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
    public void newGame() throws IOException {
        Utils.changeScene(GAME);
    }

    @FXML
    public void highScores() throws IOException {
        Utils.changeScene(HIGH_SCORES);
    }

    @FXML
    public void exit() {
        Platform.exit();
    }

}
