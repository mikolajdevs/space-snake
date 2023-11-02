package org.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static org.example.Utils.MENU;
import static org.example.Utils.loadFXML;

public class App extends Application {

    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        App.stage = stage;
        stage.setTitle("SnakeX");
        stage.setScene(new Scene(loadFXML(MENU)));
        stage.setResizable(false);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
        Platform.exit();
    }
}