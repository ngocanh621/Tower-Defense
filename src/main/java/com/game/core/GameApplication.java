package com.game.core;

import com.game.util.GameConfig;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Cổng khởi động chính.
 * Khởi tạo giao diện JavaFX, bộ quản lý SceneManager và GameLoop.
 */
public class GameApplication extends Application {

    private SceneManager sceneManager;
    private GameLoop gameLoop;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(GameConfig.WINDOW_TITLE);
        primaryStage.setWidth(GameConfig.WINDOW_WIDTH);
        primaryStage.setHeight(GameConfig.WINDOW_HEIGHT);
        primaryStage.setResizable(false);

        sceneManager = new SceneManager(primaryStage);

        Scene menuScene = sceneManager.createMenuScene();
        primaryStage.setScene(menuScene);

        gameLoop = new GameLoop(sceneManager);
        gameLoop.start();

        primaryStage.setOnCloseRequest(e -> {
            if (gameLoop != null) {
                gameLoop.stop();
            }
            System.exit(0);
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}