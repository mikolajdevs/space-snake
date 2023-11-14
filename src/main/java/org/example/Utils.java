package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.controller.GameController;
import org.example.controller.HighScoresController;
import org.example.repository.FilePlayerRepository;
import org.example.repository.PlayerRepository;

import java.io.IOException;

public class Utils {
    public final static String MENU = "menu";
    public final static String HIGH_SCORES = "high-scores";
    public final static String GAME = "game";
    private final static PlayerRepository playerRepository = new FilePlayerRepository();

    public static Parent loadFXML(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fileName + ".fxml"));
        if (fileName.equals(HIGH_SCORES)) {
            fxmlLoader.setControllerFactory(c -> new HighScoresController(playerRepository));
        } else if (fileName.equals(GAME)) {
            fxmlLoader.setControllerFactory(c -> new GameController(playerRepository));
        }
        return fxmlLoader.load();
    }

    public static void changeScene(String fileName) throws IOException {
        Parent parent = loadFXML(fileName);
        Scene currentScene = App.stage.getScene();
        Stage currentWindow = (Stage) currentScene.getWindow();
        Scene newScene = new Scene(parent);
        currentWindow.setScene(newScene);
    }

    public static Font font(int size) {
        return Font.loadFont(
                App.class.getResourceAsStream("/org/example/fonts/VCR_OSD_MONO_1.ttf"),
                size
        );
    }
}
