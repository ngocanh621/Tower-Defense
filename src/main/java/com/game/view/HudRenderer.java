package com.game.view;

import com.game.controller.PlayerState;
import com.game.controller.WaveManager;
import com.game.core.SceneManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Phụ trách vẽ giao diện thông tin người chơi (HUD - Heads Up Display) lên Canvas.
 * Hiển thị Máu (HP), Tiền vàng (Gold), Điểm số (Score), Kỷ lục (Best Score) và Đợt sóng quái (Wave).
 */
public class HudRenderer {

    /**
     * Vẽ thanh HUD lên góc trên màn hình.
     *
     * @param gc Đối tượng đồ họa GraphicsContext
     * @param playerState Trạng thái người chơi (máu, vàng, điểm số)
     * @param waveManager Trạng thái đợt sóng quái vật
     */
    public void render(GraphicsContext gc, PlayerState playerState, WaveManager waveManager) {
        if (playerState == null || waveManager == null) {
            return;
        }

        // Tọa độ và kích thước khung HUD rộng rãi hơn
        double x = 15;
        double y = 15;
        double width = 680;
        double height = 45;

        // 1. Vẽ nền khung mờ (Glassmorphism effect)
        gc.setFill(Color.rgb(15, 23, 42, 0.85)); // Màu xanh đen mờ
        gc.fillRoundRect(x, y, width, height, 16, 16);

        // 2. Vẽ viền sáng cho khung HUD
        gc.setStroke(Color.rgb(56, 189, 248, 0.6)); // Màu xanh Cyan nhạt
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(x, y, width, height, 16, 16);

        // Đặt font chữ chung cho HUD
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // 3. Hiển thị Máu (HP)
        gc.setFill(Color.web("#ef4444")); // Màu đỏ
        String hpText = "❤️ HP: " + playerState.getHealth() + "/" + playerState.getMaxHealth();
        gc.fillText(hpText, x + 20, y + 27);

        // 4. Hiển thị Tiền vàng (GOLD)
        gc.setFill(Color.web("#f59e0b")); // Màu vàng cam
        String goldText = "💰 GOLD: " + playerState.getGold();
        gc.fillText(goldText, x + 140, y + 27);

        // 5. Hiển thị Điểm hiện tại (SCORE)
        gc.setFill(Color.web("#22c55e")); // Màu xanh lá
        String scoreText = "⭐ SCORE: " + playerState.getScore();
        gc.fillText(scoreText, x + 270, y + 27);

        // 6. Hiển thị Kỷ lục cao nhất (BEST SCORE)
        int bestScore = Math.max(playerState.getScore(), SceneManager.loadHighScore());
        gc.setFill(Color.web("#eab308")); // Màu vàng kim
        String bestText = "🏆 BEST: " + bestScore;
        gc.fillText(bestText, x + 410, y + 27);

        // 7. Hiển thị Đợt sóng vô tận (WAVE)
        gc.setFill(Color.web("#38bdf8")); // Màu xanh ngọc
        String waveText = "🌊 WAVE " + waveManager.getCurrentWaveNumber();
        gc.fillText(waveText, x + 560, y + 27);

        // 8. Hiển thị thông báo đếm ngược đợt sóng tiếp theo
        if (!waveManager.isWaveInProgress()) {
            float timeLeft = waveManager.getTimeUntilNextWave();
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            gc.setFill(Color.web("#a855f7")); // Màu tím
            String nextWaveText = String.format("Sóng tiếp theo trong: %.1fs", timeLeft);
            gc.fillText(nextWaveText, x + 20, y + 72);
        }
    }
}
