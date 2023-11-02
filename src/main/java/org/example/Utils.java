package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Utils {
    public final static String MENU = "menu";
    public final static String HIGH_SCORES = "high-scores";
    public final static String PREPARE_GAME = "prepare-game";
    public final static String GAME = "game";

    public static Parent loadFXML(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fileName + ".fxml"));
        return fxmlLoader.load();
    }

    public static void changeScene(String fileName) throws IOException {
        Parent parent = loadFXML(fileName);
        Scene currentScene = App.stage.getScene();
        Stage currentWindow = (Stage) currentScene.getWindow();
        Scene newScene = new Scene(parent);
        currentWindow.setScene(newScene);
    }
}
