package com.game.core;

import com.game.util.GameConfig;
import javafx.animation.AnimationTimer;

/**
<<<<<<< Updated upstream
 * Vòng lặp game chính - Main Game Loop
 * Quản lý chu kỳ update và render
=======
 * Game Loop vận hành thời gian thực.
 * Quản lý chu kỳ Update, Render.
>>>>>>> Stashed changes
 */
public class GameLoop {

    private final SceneManager sceneManager;
    private AnimationTimer animationTimer;
    private long lastFrameTime = 0;

    public GameLoop(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
        initializeGameLoop();
    }

    private void initializeGameLoop() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long currentFrameTime = System.nanoTime();
                double deltaTime;

                if (lastFrameTime == 0) {
                    deltaTime = GameConfig.FRAME_TIME;
                } else {
                    deltaTime = (currentFrameTime - lastFrameTime) / 1_000_000_000.0;
                }
                lastFrameTime = currentFrameTime;

                if (deltaTime > 0.05) {
                    deltaTime = 0.05;
                }

                update(deltaTime);

                render();
            }
        };
    }

    /**
     * Cập nhật logic game cho khung hình hiện tại.
     *
<<<<<<< Updated upstream
     * @param deltaTime Thời gian trôi qua kể từ khung hình trước 
=======
     * @param deltaTime Khoảng thời gian trôi qua giữa 2 khung hình
>>>>>>> Stashed changes
     */
    private void update(double deltaTime) {
        if (sceneManager.getCurrentGame() != null) {
            sceneManager.getCurrentGame().update(deltaTime);
        }
    }

    private void render() {
        if (sceneManager.getCurrentGame() != null) {
            sceneManager.getCurrentGame().render();
        }
    }
<<<<<<< Updated upstream
  
    public void start() {
        if (animationTimer != null) {
            animationTimer.start();
            System.out.println("GameLoop started at TARGET FPS: " + GameConfig.TARGET_FPS);
=======

    public void start() {
        if (animationTimer != null) {
            animationTimer.start();
            System.out.println(">>> GameLoop đã kích hoạt!");
>>>>>>> Stashed changes
        }
    }

    public void stop() {
        if (animationTimer != null) {
            animationTimer.stop();
            System.out.println(">>> GameLoop đã tạm dừng!");
        }
    }
}