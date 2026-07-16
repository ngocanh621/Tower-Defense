package com.game.core;

import com.game.util.Constants;
import com.game.util.GameConfig;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Lớp quản lý và chuyển đổi màn hình
 * Đóng vai trò làm trung tâm điều phối trạng thái giữa Menu chính, Màn chơi chính và GameOver.
 */
public class SceneManager {

    private final Stage primaryStage;
    private GameScene currentGame;

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene createMenuScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + Constants.COLOR_EMPTY + ";");

        Canvas menuCanvas = new Canvas(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        GraphicsContext gc = menuCanvas.getGraphicsContext2D();

        gc.setFill(Color.web(Constants.COLOR_EMPTY));
        gc.fillRect(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 64));
        gc.fillText("Tower Defense 2D", 300, 200);

        gc.setFont(Font.font("Arial", 24));
        gc.fillText("Press SPACE to start game", 400, 400);
        gc.fillText("Press ESC to exit", 450, 450);

        root.setCenter(menuCanvas);

        Scene scene = new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        
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

        menuCanvas.requestFocus();

        return scene;
    }

    public Scene createGameScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + Constants.COLOR_BACKGROUND + ";");

        Canvas gameCanvas = new Canvas(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        GameScene gameScene = new GameScene(gameCanvas, gc);
        this.currentGame = gameScene;

        root.setCenter(gameCanvas);

        Scene scene = new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

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

        gameCanvas.requestFocus();

        return scene;
    }

    public void switchToMenuScene() {
        Scene menuScene = createMenuScene();
        primaryStage.setScene(menuScene);
        this.currentGame = null; 
    }

    public void switchToGameScene() {
        Scene gameScene = createGameScene();
        primaryStage.setScene(gameScene);
    }

    public GameScene getCurrentGame() {
        return currentGame;
    }
}