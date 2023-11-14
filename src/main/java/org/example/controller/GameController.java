package org.example.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.Utils;
import org.example.model.Direction;
import org.example.model.Player;
import org.example.model.Position;
import org.example.repository.PlayerRepository;

import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class GameController {

    @FXML
    StackPane stackPane;
    @FXML
    Pane pane;
    @FXML
    Label timer;
    @FXML
    Label scoreLabel;
    @FXML
    Rectangle rectangle;

    @FXML
    Button segmentAdder;

    HBox hBox;
    AnchorPane anchorPane;

    int widthSquaresNumber;
    int heightSquaresNumber;

    private double gamePaneWidth;
    private double gamePaneHeight;

    int snakeSize = 50;

    double coinSpawnInterval;
    double snakeTicksPerSecond;

    double score = 0;

    Direction direction = Direction.UP;

    Position currentPosition;
    Position lastPosition;

    Rectangle snakeHead;
    ArrayList<Rectangle> body = new ArrayList<>();

    Rectangle coin = new Rectangle(snakeSize, snakeSize);
    boolean isSegmentAdder;

    long secondsCounter;

    Timeline time;
    Timeline movement;
    Timeline coinSpawn;

    private final PlayerRepository playerRepository;

    public GameController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @FXML
    public void initialize() {
        prepareGame();
    }

    public void prepareGame() {
        segmentAdder.setVisible(false);
        timer.setVisible(false);
        scoreLabel.setVisible(false);

        widthSquaresNumber = (int) stackPane.getPrefWidth() / snakeSize;
        heightSquaresNumber = (int) stackPane.getPrefHeight() / snakeSize;

        if (widthSquaresNumber % 2 == 0) widthSquaresNumber -= 1;
        if (heightSquaresNumber % 2 == 0) heightSquaresNumber -= 1;

        gamePaneWidth = widthSquaresNumber * snakeSize;
        gamePaneHeight = heightSquaresNumber * snakeSize;

        rectangle.setWidth(stackPane.getPrefWidth());
        rectangle.setHeight(stackPane.getPrefHeight());

        TextField textFieldCoinsPerSecond = new TextField();
        textFieldCoinsPerSecond.setPromptText("Coin spawn interval [s]");
        textFieldCoinsPerSecond.setPrefWidth(700);

        TextField textFieldSnakeSpeed = new TextField();
        textFieldSnakeSpeed.setPromptText("Snake ticks per second");
        textFieldSnakeSpeed.setPrefWidth(700);

        textFieldCoinsPerSecond.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        this.coinSpawnInterval = (Double.parseDouble(newValue));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
        );
        textFieldSnakeSpeed.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        this.snakeTicksPerSecond = (Double.parseDouble(newValue));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
        );


        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(snakeSize);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(textFieldCoinsPerSecond, textFieldSnakeSpeed);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(snakeSize);

        Button startGameButton = new Button("START");
        startGameButton.setPrefWidth(1450);
        Button startGameWithDefaultSettingsButton = new Button("START WITH DEFAULT SETTINGS");
        startGameWithDefaultSettingsButton.setPrefWidth(1450);
        startGameWithDefaultSettingsButton.getStyleClass().add("emphasis");

        Label or = new Label();
        or.setText("\n\nWant more control? Check out the Advanced Settings section!\n");
        or.setFont(Utils.font(30));
        or.setTextFill(Color.WHITE);

        vBox.getChildren().addAll(startGameWithDefaultSettingsButton, or, hBox, startGameButton);
        stackPane.getChildren().addAll(vBox);

        startGameButton.setOnMouseClicked(
                event -> {
                    if (coinSpawnInterval > 0 && snakeTicksPerSecond > 0) {
                        stackPane.getChildren().remove(vBox);
                        startGame();
                    } else {
                        if (coinSpawnInterval <= 0)
                            textFieldCoinsPerSecond.setStyle("-fx-background-color: #ff0000");
                        else {
                            textFieldCoinsPerSecond.setStyle("-fx-background-color: #72FF00");
                        }
                        if (snakeTicksPerSecond <= 0)
                            textFieldSnakeSpeed.setStyle("-fx-background-color: #ff0000");
                        else {
                            textFieldSnakeSpeed.setStyle("-fx-background-color: #72FF00");
                        }
                    }
                }
        );
        startGameWithDefaultSettingsButton.setOnMouseClicked(
                event -> {
                    this.coinSpawnInterval = 10;
                    this.snakeTicksPerSecond = 3.33;
                    stackPane.getChildren().remove(vBox);
                    startGame();
                });
    }

    public void startGame() {
        segmentAdder.setVisible(true);
        segmentAdder.setTranslateY(-(gamePaneHeight / 2) - segmentAdder.getHeight());
        segmentAdder.setTranslateX(-(gamePaneWidth / 2) - segmentAdder.getWidth() / 2 - snakeSize);

        scoreLabel.setVisible(true);
        scoreLabel.setTranslateY(-(gamePaneHeight / 2) + snakeSize * 1.0);
        scoreLabel.setTranslateX(snakeSize * 0.5);

        timer.setVisible(true);

        hBox = new HBox();
        anchorPane = new AnchorPane();

        anchorPane.setPrefWidth(gamePaneWidth);
        anchorPane.setPrefHeight(gamePaneHeight);

        timer.setTranslateY(gamePaneHeight / 2 - snakeSize * 1.5);

        pane.getChildren().add(hBox);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(anchorPane);

        snakeHead = new Rectangle(anchorPane.getPrefWidth() / 2 - snakeSize / 2.0, anchorPane.getPrefHeight() / 2 - snakeSize / 2.0, snakeSize, snakeSize);
        snakeHead.setFill(Color.RED);
        snakeHead.setStroke(Color.web("0xd20000", 1.0));
        snakeHead.setStrokeType(StrokeType.INSIDE);
        snakeHead.setStrokeWidth(5);

        anchorPane.getChildren().addAll(snakeHead, segmentAdder);
        anchorPane.setOnKeyPressed(event -> setDirection(event.getCode()));

        this.initTimelines();
    }

    private void initTimelines() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("mm:ss");
        LocalTime localTime = LocalTime.now();
        time = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            secondsCounter = localTime.until(LocalTime.now(), ChronoUnit.SECONDS);
                            timer.setText(LocalTime.ofSecondOfDay(secondsCounter).format(dateTimeFormatter));
                        }
                ));
        movement = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        event -> {
                            if (coin.getX() == snakeHead.getX() && coin.getY() == snakeHead.getY()) {
                                coin.setVisible(false);
                                score++;
                                scoreLabel.setText("SCORE: " + (int) score);
                            }
                            if (snakeHead.getX() == -snakeSize || snakeHead.getX() == gamePaneWidth
                                    || snakeHead.getY() == -snakeSize || snakeHead.getY() == gamePaneHeight) {
                                gameOver();
                            }
                            for (Rectangle snakeSegment : body) {
                                if (snakeSegment.getX() == snakeHead.getX() && snakeSegment.getY() == snakeHead.getY()) {
                                    gameOver();
                                }
                            }
                        }
                ),
                new KeyFrame(Duration.seconds(1 / snakeTicksPerSecond),
                        event -> {
                            if (coin.getX() == snakeHead.getX() && coin.getY() == snakeHead.getY() && isSegmentAdder) {
                                addBody();
                            }
                            lastPosition = new Position(snakeHead.getX(), snakeHead.getY());
                            moveHead();
                            snakeHead.toFront();
                            moveBody();
                        }
                )
        );
        coinSpawn = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        event -> {
                            String img;
                            if ((int) (Math.random() * 5) == 0) {
                                img = "super-coin.png";
                                isSegmentAdder = false;
                            } else {
                                img = "coin.png";
                                isSegmentAdder = true;
                            }
                            InputStream is = getClass().getResourceAsStream("/org/example/images/" + img);
                            coin.setFill(new ImagePattern(new Image(is)));

                            coin.setX((int) (Math.random() * widthSquaresNumber) * snakeSize);
                            coin.setY((int) (Math.random() * heightSquaresNumber) * snakeSize);
                            coin.setVisible(true);
                            anchorPane.getChildren().add(coin);
                            snakeHead.toFront();
                        }
                ),
                new KeyFrame(Duration.seconds(coinSpawnInterval),
                        event -> anchorPane.getChildren().remove(coin)
                )
        );

        time.setCycleCount(Animation.INDEFINITE);
        time.play();
        movement.setCycleCount(Animation.INDEFINITE);
        movement.play();
        coinSpawn.setCycleCount(Animation.INDEFINITE);
        coinSpawn.play();
    }

    private void gameOver() {
        movement.pause();
        coinSpawn.pause();
        time.pause();

        Rectangle red = new Rectangle(anchorPane.getPrefWidth(), anchorPane.getPrefHeight(), Color.RED);
        Rectangle black = new Rectangle(anchorPane.getPrefWidth(), anchorPane.getPrefHeight(), Color.BLACK);
        black.setOpacity(0.5);
        red.setOpacity(0.0);
        FadeTransition ft = new FadeTransition(Duration.seconds(3), red);
        ft.setFromValue(0.0);
        ft.setToValue(0.1);
        ft.setCycleCount(1);
        ft.play();

        Text textGameOver = new Text("GAME OVER!");
        textGameOver.setFill(Color.WHITE);
        textGameOver.setOpacity(0.0);

        ft = new FadeTransition(Duration.seconds(1), textGameOver);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.play();

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.4), textGameOver);
        tt.setByY(-200);
        tt.play();

        textGameOver.setFont(Utils.font(100));
        textGameOver.toFront();
        textGameOver.setX(anchorPane.getPrefWidth() / 2 - textGameOver.getLayoutBounds().getWidth() / 2);
        textGameOver.setY(anchorPane.getPrefHeight() / 2 + textGameOver.getLayoutBounds().getHeight() / 4);

        anchorPane.getChildren().addAll(black, red, textGameOver);

        Player player = new Player();
        player.setScore(score);

        VBox vBox = new VBox();
        vBox.setOpacity(0.0);

        ft = new FadeTransition(Duration.seconds(3), vBox);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(1);
        ft.play();

        stackPane.getChildren().add(vBox);

        TextField textFieldName = new TextField();
        textFieldName.textProperty().addListener(
                (observable, oldValue, newValue) -> player.setName(newValue));
        textFieldName.setPromptText("NAME");

        Button okButton = new Button("OK");
        okButton.setOnMouseClicked(
                event -> {
                    if (player.getName() != null) {
                        playerRepository.savePlayer(player);
                    } else {
                        textFieldName.setStyle("-fx-background-color: #ff0000");
                    }
                }

        );

        textFieldName.setMaxWidth(600);
        okButton.setPrefWidth(600);


        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(snakeSize);
        vBox.getChildren().addAll(textFieldName, okButton);

    }

    void addBody() {
        Rectangle bodySegment = new Rectangle(snakeSize, snakeSize);
        bodySegment.setFill(Color.web("0x72ff00", 1.0));
        body.add(bodySegment);

        anchorPane.getChildren().add(bodySegment);
    }

    void setDirection(KeyCode keyCode) {
        switch (keyCode) {
            case W -> direction = Direction.UP;
            case A -> direction = Direction.LEFT;
            case S -> direction = Direction.DOWN;
            case D -> direction = Direction.RIGHT;
        }
    }

    void moveHead() {
        switch (direction) {
            case UP -> snakeHead.setY(snakeHead.getY() - snakeSize);
            case LEFT -> snakeHead.setX(snakeHead.getX() - snakeSize);
            case DOWN -> snakeHead.setY(snakeHead.getY() + snakeSize);
            case RIGHT -> snakeHead.setX(snakeHead.getX() + snakeSize);
        }
    }

    void moveBody() {
        for (Rectangle bodySegment : body) {
            currentPosition = new Position(bodySegment.getX(), bodySegment.getY());
            bodySegment.setX(lastPosition.getX());
            bodySegment.setY(lastPosition.getY());
            lastPosition = currentPosition;
        }
    }

    public void addMany() {
        this.addMany(5);
    }

    public void addMany(int count) {
        for (int i = 0; i < count; i++) {
            addBody();
        }
    }
}
