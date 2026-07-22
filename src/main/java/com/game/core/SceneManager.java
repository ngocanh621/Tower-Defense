package com.game.core;

import com.game.util.Constants;
import com.game.util.GameConfig;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Quản lý chuyển cảnh giữa menu, màn chơi và lưu trữ High Score.
 */
public class SceneManager {

    private final Stage primaryStage;
    private GameScene currentGame;
    private static final String HIGH_SCORE_FILE = "highscore.txt";

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Tạo giao diện menu chính với tiêu đề, high score, nút bắt đầu và thoát.
     */
    public Scene createMenuScene() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #0f172a;");

        Label titleLabel = new Label("TOWER DEFENSE 2D");
        titleLabel.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 52px; -fx-font-weight: bold; -fx-text-fill: #38bdf8;");

        DropShadow glowEffect = new DropShadow();
        glowEffect.setColor(Color.web("#0284c7"));
        glowEffect.setRadius(20);
        glowEffect.setSpread(0.4);
        titleLabel.setEffect(glowEffect);

        int bestScore = loadHighScore();
        Label highScoreLabel = new Label("🏆 BEST SCORE: " + bestScore);
        highScoreLabel.setStyle(
            "-fx-font-family: 'Arial'; -fx-font-size: 20px; -fx-font-weight: bold; "
            + "-fx-text-fill: #f59e0b; -fx-background-color: #1e293b; "
            + "-fx-padding: 8px 24px; -fx-background-radius: 20px; "
            + "-fx-border-color: #f59e0b; -fx-border-radius: 20px; -fx-border-width: 1.5px;"
        );

        Button startBtn = createStyledButton("▶  START GAME", "#22c55e", "#16a34a");
        startBtn.setOnAction(e -> switchToGameScene());

        Button quitBtn = createStyledButton("❌  QUIT GAME", "#ef4444", "#dc2626");
        quitBtn.setOnAction(e -> System.exit(0));

        VBox menuBox = new VBox(20, titleLabel, highScoreLabel, startBtn, quitBtn);
        menuBox.setAlignment(Pos.CENTER);

        root.getChildren().add(menuBox);

        Scene scene = new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case SPACE -> switchToGameScene();
                case ESCAPE -> System.exit(0);
                default -> {}
            }
        });

        return scene;
    }

    /**
     * Hàm phụ trợ thiết kế nút với hiệu ứng hover.
     */
    private Button createStyledButton(String text, String normalColor, String hoverColor) {
        Button btn = new Button(text);
        btn.setPrefWidth(260);
        btn.setPrefHeight(50);

        String styleNormal = String.format(
            "-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 18px; "
            + "-fx-font-weight: bold; -fx-background-radius: 12px; -fx-cursor: hand;",
            normalColor
        );

        String styleHover = String.format(
            "-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 18px; "
            + "-fx-font-weight: bold; -fx-background-radius: 12px; -fx-cursor: hand; "
            + "-fx-scale-x: 1.05; -fx-scale-y: 1.05;",
            hoverColor
        );

        btn.setStyle(styleNormal);
        btn.setOnMouseEntered(e -> btn.setStyle(styleHover));
        btn.setOnMouseExited(e -> btn.setStyle(styleNormal));

        return btn;
    }

    /**
     * Đọc High Score từ file lưu trữ local.
     */
    public static int loadHighScore() {
        File file = new File(HIGH_SCORE_FILE);
        if (!file.exists()) {
            return 0;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            return (line != null) ? Integer.parseInt(line.trim()) : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Cập nhật High Score mới nếu kỷ lục bị phá.
     */
    public static void saveHighScore(int newScore) {
        int currentBest = loadHighScore();
        if (newScore > currentBest) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORE_FILE))) {
                writer.write(String.valueOf(newScore));
                System.out.println(">>> Kỷ lục mới đã được lưu: " + newScore);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Tạo màn chơi chính (GameScene).
     */
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
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                switchToMenuScene();
            } else {
                gameScene.handleKeyPress(event);
            }
        });

        scene.setOnMouseClicked(gameScene::handleMouseClick);
        scene.setOnMouseMoved(gameScene::handleMouseMove);

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