package com.game.core;

import com.game.util.GameConfig;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

<<<<<<< Updated upstream
=======
/**
 * Cổng khởi động chính .
 * Khởi tạo giao diện JavaFX, bộ quản lý SceneManager và GameLoop
 */
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
            gameLoop.stop(); 
            System.exit(0);  
=======
            if (gameLoop != null) {
                gameLoop.stop();
            }
            System.exit(0);
>>>>>>> Stashed changes
        });

        primaryStage.show();

<<<<<<< Updated upstream
        System.out.println(GameConfig.WINDOW_TITLE + " has been started successfully!");
=======
        System.out.println(">>> Tower Defense 2D đã khởi động thành công!");
>>>>>>> Stashed changes
    }

    public static void main(String[] args) {
        launch(args);
    }
}