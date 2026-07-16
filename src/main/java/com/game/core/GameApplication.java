package com.game.core;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Main entry point for the Tower Defense game.
 * Initializes the JavaFX application, sets up the scene manager,
 * and starts the game loop.
 */
public class GameApplication extends Application {

    private SceneManager sceneManager;
    private GameLoop gameLoop;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Configure primary stage
        primaryStage.setTitle("Tower Defense 2D MVP");
        primaryStage.setWidth(1280);
        primaryStage.setHeight(720);
        primaryStage.setResizable(true);

        // Initialize scene manager
        sceneManager = new SceneManager(primaryStage);

        // Switch to main menu scene initially
        Scene menuScene = sceneManager.createMenuScene();
        primaryStage.setScene(menuScene);

        // Initialize and start game loop
        gameLoop = new GameLoop(sceneManager);
        gameLoop.start();

        // Handle window close
        primaryStage.setOnCloseRequest(e -> {
            gameLoop.stop();
            System.exit(0);
        });

        // Show the stage
        primaryStage.show();

        System.out.println("Tower Defense 2D MVP Started");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
