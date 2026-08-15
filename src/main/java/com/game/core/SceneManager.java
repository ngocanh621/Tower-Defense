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
        // === BẬT NHẠC NỀN MENU KHỞI TẠO ===
        SoundManager.getInstance().playBGM("/audio/mainMenuMusic.mp3");

        StackPane root = new StackPane();
        // 1. Nạp đường dẫn ảnh nền
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
        // 2. Lớp phủ mờ cho background
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35);");
        // 3.Tiêu đề
        ImageView titleImage = new ImageView();
        try {
            Image img = new Image(getClass().getResourceAsStream("/assets/logo.png"));
            titleImage.setImage(img);
            titleImage.setFitWidth(400); // Chỉnh chiều rộng logo theo ý muốn
            titleImage.setPreserveRatio(true); // Giữ đúng tỷ lệ ảnh
        } catch (Exception e) {
            // Fallback nếu chưa có ảnh
        }

        VBox titleBox = new VBox(4, titleImage);
        titleBox.setAlignment(Pos.CENTER);

        DropShadow titleShadow = new DropShadow();
        titleShadow.setColor(Color.rgb(0, 0, 0, 0.8));
        titleShadow.setRadius(15);
        titleShadow.setOffsetY(5);
        titleBox.setEffect(titleShadow);

        // 4. High score
        int bestScore = loadHighScore();
        Label highScoreLabel = new Label("🏆 BEST SCORE: " + bestScore);
        highScoreLabel.setStyle(
            "-fx-font-family: 'Arial'; -fx-font-size: 18px; -fx-font-weight: bold; "
            + "-fx-text-fill: #f59e0b; -fx-background-color: rgba(30, 41, 59, 0.85); "
            + "-fx-padding: 6px 20px; -fx-background-radius: 15px; "
            + "-fx-border-color: #f59e0b; -fx-border-radius: 15px; -fx-border-width: 1.5px;"
        );

        // 5. Các nút bấm
        Button startBtn = createStyledButton("▶  START GAME");
        startBtn.setOnAction(e -> switchToIntroScene());

        Button quitBtn = createStyledButton("❌  QUIT GAME");
        quitBtn.setOnAction(e -> System.exit(0));

        // 6. Khung chứa căn giữa
        VBox menuBox = new VBox(15, titleBox, highScoreLabel, startBtn, quitBtn);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setMaxWidth(500);

        root.getChildren().addAll(overlay, menuBox);

        Scene scene = new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case SPACE -> switchToIntroScene();
                case ESCAPE -> System.exit(0);
                default -> {}
            }
        });

        return scene;
    }

    /**
     * Hàm tạo Button kiểu Wooden Banner giả gỗ với hiệu ứng Hover phóng to nhẹ
     */
    private Button createStyledButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(280);
        btn.setPrefHeight(50);

        // Style khung gỗ + viền vàng đồng + đổ bóng
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

    public void switchToIntroScene() {
        IntroScene introScene = new IntroScene(this);
        primaryStage.setScene(introScene.getScene());
    }

    /**
     * Tạo màn chơi chính (GameScene).
     */
    public Scene createGameScene() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + Constants.COLOR_BACKGROUND + ";");

        Canvas gameCanvas = new Canvas(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        GameScene gameScene = new GameScene(gameCanvas, gc, this);
        this.currentGame = gameScene;

        // Lắng nghe khi GameScene phát tín hiệu GAME_OVER
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
        // === ĐỔI SANG NHẠC MENU KHI THOÁT RA GAME ===
        SoundManager.getInstance().playBGM("/audio/mainMenuMusic.mp3");

        Scene menuScene = createMenuScene();
        primaryStage.setScene(menuScene);
        this.currentGame = null;
    }

    public void switchToGameScene() {
        // === ĐỔI SANG NHẠC IN-GAME KHI BẮT ĐẦU CHƠI ===
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
     * Tạo Scene Game Over đồng bộ phong cách thiết kế với Main Menu
     */
    public Scene createGameOverScene(int finalScore) {
        // === ĐỔI SANG NHẠC GAME OVER (Nếu có) ===
        // SoundManager.getInstance().playBGM("/audio/gameOverMusic.mp3");

        StackPane root = new StackPane();

        // 1. Nạp ảnh nền
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

        // 2. Lớp phủ mờ tối màu hơn
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(15, 23, 42, 0.75);");

        // 3. Tiêu đề GAME OVER
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

        // 4. Kiểm tra và Cập nhật High Score
        int currentBest = loadHighScore();
        boolean isNewHighScore = finalScore > currentBest;
        if (isNewHighScore) {
            saveHighScore(finalScore);
            currentBest = finalScore;
        }

        // 5. Khung hiển thị điểm (Score Card Panel)
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

        // 6. Các nút bấm điều hướng
        Button restartBtn = createStyledButton("🔄  PLAY AGAIN");
        restartBtn.setOnAction(e -> switchToGameScene());

        Button menuBtn = createStyledButton("🏠  MAIN MENU");
        menuBtn.setOnAction(e -> switchToMenuScene());

        Button quitBtn = createStyledButton("❌  QUIT GAME");
        quitBtn.setOnAction(e -> System.exit(0));

        // 7. Gom tất cả vào VBox trung tâm
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
     * Hàm phụ trợ tạo nhãn Badge hiển thị điểm số
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