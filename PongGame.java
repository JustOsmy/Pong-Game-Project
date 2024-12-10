package com.example.ponggame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PongGame extends Application {
    private boolean isSinglePlayer = false; // Default value
    private int scoreLimit = 10; // Default value

    // No-argument constructor (required by JavaFX)
    public PongGame() {
        // Default settings can go here if needed
    }

    // Constructor with parameters (can be used for testing or advanced setups)
    public PongGame(boolean isSinglePlayer, int scoreLimit) {
        this.isSinglePlayer = isSinglePlayer;
        this.scoreLimit = scoreLimit;
    }

    @Override
    public void start(Stage primaryStage) {
        Pane gameRoot = new Pane();

        // Add background
        gameRoot.setStyle("-fx-background-color: black;");

        // Initialize game elements
        GameElements gameElements = new GameElements(gameRoot, 800, 600, isSinglePlayer, scoreLimit);

        Scene gameScene = new Scene(gameRoot, 800, 600, Color.BLACK);
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("Pong Game");
        primaryStage.show();

        // Debug log
        System.out.println("Game setup complete.");
    }
}       
