package com.example.ponggame;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class HelloApplication extends Application {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    private boolean isSinglePlayer = false; // Default to Two Player Mode
    private int scoreLimit = 10; // Default score limit

    @Override
    public void start(Stage primaryStage) {
        showMenu(primaryStage);
    }

    // Menu Screen
    private void showMenu(Stage primaryStage) {
        Pane menuRoot = new Pane();
        VBox menuLayout = new VBox(10);
        menuLayout.setStyle("-fx-alignment: center; -fx-padding: 20px;");

        // Title
        Label titleLabel = new Label("Pong Game");
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");

        // Score Limit Input
        Label scoreLimitLabel = new Label("Set Score Limit:");
        TextField scoreLimitInput = new TextField("10");
        scoreLimitInput.setMaxWidth(100);

        // Buttons for Game Mode
        Button singlePlayerButton = new Button("Single Player");
        Button twoPlayerButton = new Button("Two Players");

        // Set Actions
        singlePlayerButton.setOnAction(e -> isSinglePlayer = true);
        twoPlayerButton.setOnAction(e -> isSinglePlayer = false);

        // Start Game Button
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> {
            try {
                scoreLimit = Integer.parseInt(scoreLimitInput.getText());
                if (scoreLimit <= 0) throw new NumberFormatException(); // Validate input
                startGame(primaryStage); // Proceed to game
            } catch (NumberFormatException ex) {
                scoreLimitInput.setStyle("-fx-border-color: red;");
            }
        });

        menuLayout.getChildren().addAll(titleLabel, scoreLimitLabel, scoreLimitInput, singlePlayerButton, twoPlayerButton, startButton);
        menuRoot.getChildren().add(menuLayout);
        Scene menuScene = new Scene(menuRoot, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(menuScene);
        primaryStage.setTitle("Pong Game Menu");
        primaryStage.show();
    }

    // Game Screen
    private void startGame(Stage primaryStage) {
        Pane gameRoot = new Pane();
        gameRoot.setSnapToPixel(true); // Snap elements to pixel boundaries
        GameElements gameElements = new GameElements(gameRoot, WINDOW_WIDTH, WINDOW_HEIGHT, isSinglePlayer, scoreLimit);

        Scene gameScene = new Scene(gameRoot, WINDOW_WIDTH, WINDOW_HEIGHT, Color.BLACK);

        gameScene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W -> movePaddle(gameElements.getPaddle1(), -60);
                case S -> movePaddle(gameElements.getPaddle1(), 60);
                case UP -> {
                    if (!isSinglePlayer) movePaddle(gameElements.getPaddle2(), -60);
                }
                case DOWN -> {
                    if (!isSinglePlayer) movePaddle(gameElements.getPaddle2(), 60);
                }
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameElements.moveBall(WINDOW_WIDTH, WINDOW_HEIGHT);
                if (gameElements.checkForScore(WINDOW_WIDTH)) {
                    stop(); // Stop the game on win condition
                }
            }
        };
        timer.start();

        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Pong Game");
        primaryStage.show();
    }

    private void movePaddle(Rectangle paddle, int deltaY) {
        double newY = paddle.getY() + deltaY;
        if (newY >= 0 && newY + GameElements.PADDLE_HEIGHT <= WINDOW_HEIGHT) {
            paddle.setY(newY);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
