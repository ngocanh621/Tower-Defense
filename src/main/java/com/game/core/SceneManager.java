package com.game.core;

import com.game.system.EventBus;
import com.game.system.GameEvent;
import com.game.util.Constants;
import com.game.util.GameConfig;
import com.game.system.SoundManager;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * quản lý chuyển cảnh giữa menu, màn chơi và lưu trữ High Score
 */
public class SceneManager {

    private final Stage primaryStage;
    private GameScene currentGame;
    private static final String HIGH_SCORE_FILE = "highscore.txt";

    public SceneManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * tạo giao diện menu chính với tiêu đề, high score, nút bắt đầu và thoát
     */
    public Scene createMenuScene() {
        SoundManager.getInstance().playBGM("/audio/mainMenuMusic.mp3");

        StackPane root = new StackPane();
        String bgPath = getClass().getResource("/assets/MainScreen.png") != null
                ? getClass().getResource("/assets/MainScreen.png").toExternalForm()
                : "";

        if (!bgPath.isEmpty()) {
            root.setStyle("-fx-background-image: url('" + bgPath + "'); "
                    + "-fx-background-size: cover; "
                    + "-fx-background-position: center center;");
        } else {
            root.setStyle("-fx-background-color: #0f172a;");
        }

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35);");
        
        ImageView titleImage = new ImageView();
        try {
            Image img = new Image(getClass().getResourceAsStream("/assets/logo.png"));
            titleImage.setImage(img);
            titleImage.setFitWidth(400); 
            titleImage.setPreserveRatio(true); 
        } catch (Exception e) {
        }

        VBox titleBox = new VBox(4, titleImage);
        titleBox.setAlignment(Pos.CENTER);

        DropShadow titleShadow = new DropShadow();
        titleShadow.setColor(Color.rgb(0, 0, 0, 0.8));
        titleShadow.setRadius(15);
        titleShadow.setOffsetY(5);
        titleBox.setEffect(titleShadow);

        int bestScore = loadHighScore();
        Label highScoreLabel = new Label("🏆 BEST SCORE: " + bestScore);
        highScoreLabel.setStyle(
            "-fx-font-family: 'Arial'; -fx-font-size: 18px; -fx-font-weight: bold; "
            + "-fx-text-fill: #f59e0b; -fx-background-color: rgba(30, 41, 59, 0.85); "
            + "-fx-padding: 6px 20px; -fx-background-radius: 15px; "
            + "-fx-border-color: #f59e0b; -fx-border-radius: 15px; -fx-border-width: 1.5px;"
        );

        Button startBtn = createStyledButton("▶  START GAME");
        startBtn.setOnAction(e -> switchToGameScene());

        Button quitBtn = createStyledButton("❌  QUIT GAME");
        quitBtn.setOnAction(e -> System.exit(0));

        VBox menuBox = new VBox(15, titleBox, highScoreLabel, startBtn, quitBtn);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setMaxWidth(500);

        root.getChildren().addAll(overlay, menuBox);

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
     * Button
     */
    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(280);
        btn.setPrefHeight(50);

        String styleNormal =
                "-fx-background-color: linear-gradient(to bottom, #7f4f24, #582f0e); " +
                        "-fx-text-fill: #ffedd5; " +
                        "-fx-font-family: 'Arial'; -fx-font-size: 18px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-border-color: #b07d62 #381f0d #381f0d #b07d62; " + // Viền tạo hiệu ứng 3D
                        "-fx-border-radius: 8px; -fx-border-width: 2.5px; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 8, 0, 0, 4);";

        String styleHover =
                "-fx-background-color: linear-gradient(to bottom, #936639, #6c3a11); " +
                        "-fx-text-fill: #ffffff; " +
                        "-fx-font-family: 'Arial'; -fx-font-size: 18px; -fx-font-weight: bold; " +
                        "-fx-background-radius: 8px; " +
                        "-fx-border-color: #ffd166 #381f0d #381f0d #ffd166; " + // Viền sáng lên khi hover
                        "-fx-border-radius: 8px; -fx-border-width: 2.5px; " +
                        "-fx-cursor: hand; " +
                        "-fx-scale-x: 1.05; -fx-scale-y: 1.05; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(255,209,102,0.4), 12, 0, 0, 4);";

        btn.setStyle(styleNormal);
        btn.setOnMouseEntered(e -> btn.setStyle(styleHover));
        btn.setOnMouseExited(e -> btn.setStyle(styleNormal));

        return btn;
    }

    /**
     * đọc high score 
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
     * lưu high score 
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
     * tạo màn chơi chính GameScene
     */
    public Scene createGameScene() {
        EventBus.getInstance().clear();
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + Constants.COLOR_BACKGROUND + ";");

        Canvas gameCanvas = new Canvas(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        GameScene gameScene = new GameScene(gameCanvas, gc);
        this.currentGame = gameScene;

        // lắng nghe khi GameScene phát tín hiệu GAME_OVER
        EventBus.getInstance().subscribe(GameEvent.GAME_OVER, data -> {
            if (data instanceof Integer score) {
                switchToGameOverScene(score);
            }
        });

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
        if (currentGame != null && currentGame.getPlayerState() != null) {
            saveHighScore(currentGame.getPlayerState().getScore());
        }
        //nhạc MENU
        SoundManager.getInstance().playBGM("/audio/mainMenuMusic.mp3");

        Scene menuScene = createMenuScene();
        primaryStage.setScene(menuScene);
        this.currentGame = null;
    }

    public void switchToGameScene() {
        //nhạc gameplay
        SoundManager.getInstance().playBGM("/audio/GamePlayMusic.mp3");

        Scene gameScene = createGameScene();
        primaryStage.setScene(gameScene);
    }

    /**
     * Chuyển sang màn hình Game Over khi chơi thua
     */
    public void switchToGameOverScene(int score) {
        Scene gameOverScene = createGameOverScene(score);
        primaryStage.setScene(gameOverScene);
        this.currentGame = null;
    }

    /**
     * Tạo Scene Game Over 
     */
    public Scene createGameOverScene(int finalScore) {
        //nhạc game over
        SoundManager.getInstance().playBGM("/audio/mainMenuMusic.mp3");

        StackPane root = new StackPane();

        String bgPath = getClass().getResource("/assets/MainScreen.png") != null
                ? getClass().getResource("/assets/MainScreen.png").toExternalForm()
                : "";

        if (!bgPath.isEmpty()) {
            root.setStyle("-fx-background-image: url('" + bgPath + "'); "
                    + "-fx-background-size: cover; "
                    + "-fx-background-position: center center;");
        } else {
            root.setStyle("-fx-background-color: #0f172a;");
        }

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(15, 23, 42, 0.75);");

        //GAME OVER
        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.setStyle(
                "-fx-font-family: 'Arial'; -fx-font-size: 50px; -fx-font-weight: bold; "
                        + "-fx-text-fill: linear-gradient(to bottom, #ef4444, #991b1b); "
                        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.9), 10, 0, 0, 4);"
        );

        VBox headerBox = new VBox(gameOverLabel);
        headerBox.setAlignment(Pos.CENTER);

        DropShadow titleShadow = new DropShadow();
        titleShadow.setColor(Color.rgb(0, 0, 0, 0.8));
        titleShadow.setRadius(15);
        titleShadow.setOffsetY(5);
        headerBox.setEffect(titleShadow);

        //kiểm tra và cập nhật High Score
        int currentBest = loadHighScore();
        boolean isNewHighScore = finalScore > currentBest;
        if (isNewHighScore) {
            saveHighScore(finalScore);
            currentBest = finalScore;
        }

        //khung hiển thị điểm
        Label currentScoreLabel = createBadgeLabel("🎯 YOUR SCORE: " + finalScore, "#38bdf8");
        Label bestScoreLabel = createBadgeLabel("🏆 BEST SCORE: " + currentBest, "#f59e0b");

        VBox scoreCard = new VBox(10, currentScoreLabel, bestScoreLabel);
        scoreCard.setAlignment(Pos.CENTER);
        scoreCard.setStyle(
                "-fx-background-color: rgba(30, 41, 59, 0.9); "
                        + "-fx-padding: 16px 30px; -fx-background-radius: 12px; "
                        + "-fx-border-color: #b07d62; -fx-border-radius: 12px; -fx-border-width: 2px; "
                        + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 10, 0, 0, 4);"
        );

        if (isNewHighScore) {
            Label newRecordBadge = new Label("🎉 NEW HIGH SCORE! 🎉");
            newRecordBadge.setStyle(
                    "-fx-font-family: 'Arial'; -fx-font-size: 16px; -fx-font-weight: bold; "
                            + "-fx-text-fill: #10b981; -fx-padding: 0 0 5px 0;"
            );
            scoreCard.getChildren().add(0, newRecordBadge);
        }

        Button restartBtn = createStyledButton("🔄  PLAY AGAIN");
        restartBtn.setOnAction(e -> switchToGameScene());

        Button menuBtn = createStyledButton("🏠  MAIN MENU");
        menuBtn.setOnAction(e -> switchToMenuScene());

        Button quitBtn = createStyledButton("❌  QUIT GAME");
        quitBtn.setOnAction(e -> System.exit(0));

        //gom tất cả vào VBox trung tâm
        VBox gameOverBox = new VBox(16, headerBox, scoreCard, restartBtn, menuBtn, quitBtn);
        gameOverBox.setAlignment(Pos.CENTER);
        gameOverBox.setMaxWidth(480);

        root.getChildren().addAll(overlay, gameOverBox);

        Scene scene = new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case SPACE, ENTER -> switchToGameScene();
                case ESCAPE -> switchToMenuScene();
                default -> {}
            }
        });

        return scene;
    }

    /**
     * tạo nhãn hiển thị điểm số
     */
    private Label createBadgeLabel(String text, String colorHex) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-font-family: 'Arial'; -fx-font-size: 18px; -fx-font-weight: bold; "
                        + "-fx-text-fill: " + colorHex + "; -fx-background-color: rgba(15, 23, 42, 0.7); "
                        + "-fx-padding: 6px 20px; -fx-background-radius: 15px; "
                        + "-fx-border-color: " + colorHex + "; -fx-border-radius: 15px; -fx-border-width: 1.5px;"
        );
        return label;
    }

    public GameScene getCurrentGame() {
        return currentGame;
    }
}