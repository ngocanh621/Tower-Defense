package com.game.view;

import com.game.controller.PlayerState;
import com.game.controller.WaveManager;
import com.game.core.SceneManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * vẽ HUD
 * HP, Gold, Score, Best Score và Wave
 */
public class HudRenderer {

    /**
     * vẽ thanh HUD
     */
    public void render(GraphicsContext gc, PlayerState playerState, WaveManager waveManager) {
        if (playerState == null || waveManager == null) {
            return;
        }

        double x = 15;
        double y = 15;
        double width = 680;
        double height = 45;

        gc.setFill(Color.rgb(15, 23, 42, 0.85)); 
        gc.fillRoundRect(x, y, width, height, 16, 16);

        gc.setStroke(Color.web("#f59e0b", 0.8)); 
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(x, y, width, height, 16, 16);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // HP
        gc.setFill(Color.web("#ef4444")); 
        String hpText = "\u2764 HP: " + playerState.getHealth() + "/" + playerState.getMaxHealth();
        gc.fillText(hpText, x + 20, y + 27);

        // GOLD
        gc.setFill(Color.web("#f59e0b")); 
        String goldText = "💰 GOLD: " + playerState.getGold();
        gc.fillText(goldText, x + 140, y + 27);

        // SCORE
        gc.setFill(Color.web("#22c55e")); 
        String scoreText = "⭐ SCORE: " + playerState.getScore();
        gc.fillText(scoreText, x + 270, y + 27);

        // BEST SCORE
        int bestScore = Math.max(playerState.getScore(), SceneManager.loadHighScore());
        gc.setFill(Color.web("#eab308")); 
        String bestText = "🏆 BEST: " + bestScore;
        gc.fillText(bestText, x + 410, y + 27);

        // WAVE
        gc.setFill(Color.web("#38bdf8")); 
        String waveText = "🌊 WAVE " + waveManager.getCurrentWaveNumber();
        gc.fillText(waveText, x + 560, y + 27);

        // thông báo đếm ngược đợt sóng tiếp theo
        if (!waveManager.isWaveInProgress()) {
            float timeLeft = waveManager.getTimeUntilNextWave();

            double bannerX = x;
            double bannerY = y + 55;
            double bannerW = 230;
            double bannerH = 32;

            gc.setFill(Color.rgb(15, 23, 42, 0.9));
            gc.fillRoundRect(bannerX, bannerY, bannerW, bannerH, 10, 10);

            gc.setStroke(Color.web("#f59e0b"));
            gc.setLineWidth(1.5);
            gc.strokeRoundRect(bannerX, bannerY, bannerW, bannerH, 10, 10);

            gc.setFont(Font.font("Arial", FontWeight.BOLD, 13));
            gc.setFill(Color.web("#f59e0b"));
            String nextWaveText = String.format("⏱ Quái sẽ đến sau: %.1fs", timeLeft);
            gc.fillText(nextWaveText, bannerX + 15, bannerY + 21);
        }
    }
}
