package com.game.core;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Manages scene transitions between Menu, Game, and GameOver screens.
 * Provides a central place to switch between different game states.
 */
public class SceneManager {

    private final Stage primaryStage;
    private GameScene currentGame;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Create and return the main menu scene.
     */
    public Scene createMenuScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1a1a1a;");

        // Create a simple menu UI
        Canvas menuCanvas = new Canvas(1280, 720);
        GraphicsContext gc = menuCanvas.getGraphicsContext2D();

        // Draw menu background and text
        gc.setFill(Color.web("#1a1a1a"));
        gc.fillRect(0, 0, 1280, 720);

        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 64));
        gc.fillText("Tower Defense 2D", 300, 200);

        gc.setFont(javafx.scene.text.Font.font("Arial", 24));
        gc.fillText("Press SPACE to start game", 400, 400);
        gc.fillText("Press ESC to exit", 450, 450);

        root.setCenter(menuCanvas);

        Scene scene = new Scene(root, 1280, 720);
        
        // Handle input for menu
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case SPACE:
                    switchToGameScene();
                    break;
                case ESCAPE:
                    System.exit(0);
                    break;
                default:
                    break;
            }
        });

        // Request focus for key events
        menuCanvas.requestFocus();

        return scene;
    }

    /**
     * Create and return the main game scene.
     */
    public Scene createGameScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0e27;");

        // Create canvas for game rendering
        Canvas gameCanvas = new Canvas(1280, 720);
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        // Initialize game scene (placeholder for now)
        GameScene gameScene = new GameScene(gameCanvas, gc);
        this.currentGame = gameScene;

        root.setCenter(gameCanvas);

        Scene scene = new Scene(root, 1280, 720);

        // Handle input for game scene
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    switchToMenuScene();
                    break;
                default:
                    gameScene.handleKeyPress(event);
                    break;
            }
        });

        scene.setOnMouseClicked(event -> gameScene.handleMouseClick(event));
        scene.setOnMouseMoved(event -> gameScene.handleMouseMove(event));

        // Request focus for key events
        gameCanvas.requestFocus();

        return scene;
    }

    /**
     * Switch to menu scene.
     */
    public void switchToMenuScene() {
        Scene menuScene = createMenuScene();
        primaryStage.setScene(menuScene);
        this.currentGame = null;
    }

    /**
     * Switch to game scene.
     */
    public void switchToGameScene() {
        Scene gameScene = createGameScene();
        primaryStage.setScene(gameScene);
    }

    /**
     * Get the current game scene.
     */
    public GameScene getCurrentGame() {
        return currentGame;
    }
}
