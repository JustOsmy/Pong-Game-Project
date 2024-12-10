package com.example.ponggame;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;


public class GameElements {
    public static final int PADDLE_WIDTH = 10;
    public static final int PADDLE_HEIGHT = 60; // Shorter paddles for retro style
    public static final int BALL_SIZE = 10;     // Square ball size

    private Rectangle paddle1;
    private Rectangle paddle2;
    private Rectangle ball; // Change from Circle to Rectangle

    private double ballSpeedX = 1.5;
    private double ballSpeedY = 1.5;

    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;
    private Text scoreText;
    private Text winMessage;

    private boolean isSinglePlayer;
    private int scoreLimit;

    private boolean scoringInProgress = false; // Prevent double scoring


    public GameElements(Pane root, int windowWidth, int windowHeight, boolean isSinglePlayer, int scoreLimit) {
        this.isSinglePlayer = isSinglePlayer;
        this.scoreLimit = scoreLimit;
        // Load pixel font
        Font pixelFont = Font.loadFont(getClass().getResource("/fonts/AtariSmall.ttf").toExternalForm(), 24);

        // Create paddles
        paddle1 = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT, Color.WHITE);
        paddle1.setX(20);
        paddle1.setY(windowHeight / 2 - PADDLE_HEIGHT / 2);

        // Set background color
        root.setStyle("-fx-background-color: black;");

        paddle2 = new Rectangle(PADDLE_WIDTH, PADDLE_HEIGHT, Color.WHITE);
        paddle2.setX(windowWidth - 20 - PADDLE_WIDTH);
        paddle2.setY(windowHeight / 2 - PADDLE_HEIGHT / 2);

        ball = new Rectangle(BALL_SIZE, BALL_SIZE, Color.WHITE);
        ball.setX(windowWidth / 2 - BALL_SIZE / 2);
        ball.setY(windowHeight / 2 - BALL_SIZE / 2);

        // Create player labels
        Text player1Label = new Text(50, 30, "Player 1");
        player1Label.setFill(Color.WHITE);
        player1Label.setFont(pixelFont);

        Text player2Label = new Text(windowWidth - 150, 30, "Player 2");
        player2Label.setFill(Color.WHITE);
        player2Label.setFont(pixelFont);


        // Add elements to root
        root.getChildren().addAll(paddle1, paddle2, ball, player1Label, player2Label);

        // Create score display
        scoreText = new Text("0 : 0");
        scoreText.setFill(Color.WHITE);
        scoreText.setStyle("-fx-font-size: 60px;");
        scoreText.setFont(pixelFont);
        double textWidth = scoreText.getBoundsInLocal().getWidth();
        scoreText.setX(windowWidth / 2.305 - textWidth / 2.305); // Center horizontally
        scoreText.setY(50);
        root.getChildren().add(scoreText);

        // Create win message (hidden initially)
        winMessage = new Text(windowWidth / 3 - 100, windowHeight / 2, "");
        winMessage.setFill(Color.YELLOW);
        winMessage.setStyle("-fx-font-size: 70px;");
        winMessage.setFont(pixelFont);
        root.getChildren().add(winMessage);

        // Add center line (dotted net)
        for (int i = 0; i < windowHeight; i += 20) { // Adjust spacing as needed
            Rectangle dot = new Rectangle(2, 10, Color.WHITE); // Small rectangle
            dot.setX(windowWidth / 2 - 1); // Centered horizontally
            dot.setY(i); // Spaced vertically
            root.getChildren().add(dot);
        }
    }

    // Method to move the ball
    public void moveBall(int windowWidth, int windowHeight) {
        ball.setX(ball.getX() + ballSpeedX); // Update horizontal position
        ball.setY(ball.getY() + ballSpeedY); // Update vertical position

        // Bounce off the top and bottom walls
        if (ball.getY() <= 0 || ball.getY() + BALL_SIZE >= windowHeight) {
            ballSpeedY *= -1; // Reverse vertical direction
        }

        // Bounce off paddles
        if (ball.getBoundsInParent().intersects(paddle1.getBoundsInParent())) {
            ballSpeedX *= -1; // Reverse horizontal direction
            ball.setX(paddle1.getX() + PADDLE_WIDTH); // Adjust position to prevent sticking
        }
        if (ball.getBoundsInParent().intersects(paddle2.getBoundsInParent())) {
            ballSpeedX *= -1; // Reverse horizontal direction
            ball.setX(paddle2.getX() - BALL_SIZE); // Adjust position to prevent sticking
        }
        if (isSinglePlayer) {
            // Simple AI logic: Move right paddle to follow the ball
            double aiPaddleY = paddle2.getY();
            double ballY = ball.getY();
            if (ballY > aiPaddleY + PADDLE_HEIGHT / 2) {
                paddle2.setY(aiPaddleY + 2); // AI paddle moves down
            } else if (ballY < aiPaddleY + PADDLE_HEIGHT / 2) {
                paddle2.setY(aiPaddleY - 2); // AI paddle moves up
            }
        }

    }


    // Method to check for scoring and the win condition
    public boolean checkForScore(int windowWidth) {
        // Prevent scoring if a scoring event is already in progress
        if (scoringInProgress) {
            return false;
        }

        // Check if the ball passed the left side
        if (ball.getX() <= 0) {
            scoringInProgress = true; // Mark scoring as in progress
            scorePlayer2++; // Right player scores
            resetBall(windowWidth);
        }
        // Check if the ball passed the right side
        else if (ball.getX() + BALL_SIZE >= windowWidth) {
            scoringInProgress = true; // Mark scoring as in progress
            scorePlayer1++; // Left player scores
            resetBall(windowWidth);
        }

        // Update score display
        scoreText.setText(scorePlayer1 + " : " + scorePlayer2);

        // Check win condition
        if (scorePlayer1 >= scoreLimit) {
            displayWinMessage("Player 1 Wins!");
            return true;
        } else if (scorePlayer2 >= scoreLimit) {
            displayWinMessage(isSinglePlayer ? "AI Wins!" : "Player 2 Wins!");
            return true;
        }
        return false; // Continue game

    }


    // Display the win message
    private void displayWinMessage(String message) {
        winMessage.setText(message);
        // Center horizontally
        double textWidth = winMessage.getBoundsInLocal().getWidth();
        winMessage.setX((800 - textWidth) / 2); // 800 is the window width
        // Center vertically
        winMessage.setY(600 / 2); // 600 is the window height
    }

    // Reset the ball to the center
    private void resetBall(int windowWidth) {
        ball.setX(windowWidth / 2 - BALL_SIZE / 2);
        ball.setY(300 - BALL_SIZE / 2);
        ballSpeedX *= -1; // Reverse direction
        scoringInProgress = false; // Reset scoring flag
    }


    // Getters for paddles
    public Rectangle getPaddle1() {
        return paddle1;
    }

    public Rectangle getPaddle2() {
        return paddle2;
    }

}
