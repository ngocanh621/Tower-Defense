package com.game.core;

import com.game.system.SoundManager;
import com.game.util.GameConfig;
import com.game.view.CutsceneNode;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class IntroScene {

    private final SceneManager sceneManager;
    private final StackPane root;
    private final ImageView bgImageView;

    // BỎ từ khóa 'final' ở 2 biến này để tránh lỗi "Cannot assign a value to final variable"
    private Label speakerLabel;
    private Label dialogueLabel;

    private final List<CutsceneNode> storyList = new ArrayList<>();
    private int currentStep = 0;

    private Timeline typingTimeline;
    private int charIndex = 0;

    // Truyền SceneManager vào Constructor để gọi hàm switchToGameScene() khi xong Intro
    public IntroScene(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        this.root = new StackPane();

        // 1. Ảnh nền Cutscene lấy theo kích thước GameConfig
        bgImageView = new ImageView();
        bgImageView.setFitWidth(GameConfig.WINDOW_WIDTH);
        bgImageView.setFitHeight(GameConfig.WINDOW_HEIGHT);
        bgImageView.setPreserveRatio(false);

        // 2. Nạp danh sách cốt truyện (Dùng đường dẫn /assets/ chuẩn của bạn)
        initStoryData();

        // 3. Khung chứa lời thoại ở dưới màn hình
        VBox dialogueBox = createDialogueBox();
        StackPane.setAlignment(dialogueBox, Pos.BOTTOM_CENTER);
        StackPane.setMargin(dialogueBox, new Insets(0, 40, 30, 40));

        root.getChildren().addAll(bgImageView, dialogueBox);

        // 4. Phát nhạc nền Intro bằng SoundManager của bạn
        SoundManager.getInstance().playBGM("/audio/mainMenuMusic.mp3");

        // 5. Load cảnh đầu tiên
        loadStep(0);
    }

    private void initStoryData() {
        // Thay đường dẫn ảnh chuẩn trong thư mục /assets/ của bạn vào đây
        storyList.add(new CutsceneNode(
                "/assets/MainScreen.png",
                "📜 DẪN CHUYỆN",
                "Nằm sâu trong khu rừng Eldoria, căn gác xép cổ là nơi cất giữ Kho Báu Hoàng Gia ngập tràn vàng ma thuật và dược thủy quý..."
        ));
        storyList.add(new CutsceneNode(
                "/assets/MainScreen.png", // Đổi thành ảnh quái nếu bạn có
                "👑 VUA ĐÁ LÔ CÔ",
                "Khè khè! Vàng kìa các con! Hãy tràn vào gác xép và cướp sạch mọi đồng tiền vàng cho ta!"
        ));
        storyList.add(new CutsceneNode(
                "/assets/MainScreen.png", // Đổi thành ảnh map nếu bạn có
                "🛡 TƯỚNG CHỈ HUY",
                "Bảo vệ Doanh Trại! Bố trí các Tháp Phòng Thủ dọc con đường rừng, tuyệt đối không cho chúng chạm vào kho báu!"
        ));
    }

    private VBox createDialogueBox() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15, 25, 15, 25));
        box.setStyle(
                "-fx-background-color: rgba(20, 15, 10, 0.85);" +
                        "-fx-border-color: #d4af37;" +
                        "-fx-border-width: 3px;" +
                        "-fx-border-radius: 10px;" +
                        "-fx-background-radius: 10px;"
        );

        speakerLabel = new Label();
        speakerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        speakerLabel.setTextFill(Color.web("#ffd700"));

        dialogueLabel = new Label();
        dialogueLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        dialogueLabel.setTextFill(Color.WHITE);
        dialogueLabel.setWrapText(true);
        dialogueLabel.setMinHeight(50);

        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button btnNext = new Button("Tiếp theo ➔");
        Button btnSkip = new Button("Bỏ qua ⏩");

        String btnStyle = "-fx-background-color: #5c3a21; -fx-text-fill: #fff8dc; " +
                "-fx-font-weight: bold; -fx-border-color: #8b5a2b; -fx-border-radius: 5px; -fx-cursor: hand;";
        btnNext.setStyle(btnStyle);
        btnSkip.setStyle(btnStyle);

        btnNext.setOnAction(e -> nextStep());
        btnSkip.setOnAction(e -> finishIntro());

        buttonBox.getChildren().addAll(btnNext, btnSkip);
        box.getChildren().addAll(speakerLabel, dialogueLabel, buttonBox);

        return box;
    }

    private void loadStep(int step) {
        CutsceneNode node = storyList.get(step);

        try {
            Image img = new Image(getClass().getResourceAsStream(node.getImagePath()));
            bgImageView.setImage(img);
        } catch (Exception e) {
            System.err.println("Không load được ảnh nền Intro: " + node.getImagePath());
        }

        speakerLabel.setText(node.getSpeaker());
        startTypingAnimation(node.getText());
    }

    private void startTypingAnimation(String fullText) {
        if (typingTimeline != null) {
            typingTimeline.stop();
        }

        dialogueLabel.setText("");
        charIndex = 0;

        typingTimeline = new Timeline(new KeyFrame(Duration.millis(30), e -> {
            if (charIndex < fullText.length()) {
                dialogueLabel.setText(dialogueLabel.getText() + fullText.charAt(charIndex));
                charIndex++;
            } else {
                typingTimeline.stop();
            }
        }));
        typingTimeline.setCycleCount(fullText.length());
        typingTimeline.play();
    }

    private void nextStep() {
        currentStep++;
        if (currentStep < storyList.size()) {
            loadStep(currentStep);
        } else {
            finishIntro();
        }
    }

    private void finishIntro() {
        if (typingTimeline != null) typingTimeline.stop();

        // Chuyển thẳng sang GameScene thông qua hàm sẵn có của SceneManager!
        sceneManager.switchToGameScene();
    }

    public Scene getScene() {
        return new Scene(root, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
    }
}