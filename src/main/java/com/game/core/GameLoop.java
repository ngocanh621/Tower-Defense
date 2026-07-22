package com.game.core;

import com.game.util.GameConfig;
import javafx.animation.AnimationTimer;

/**
 * Vòng lặp game chính.
 * Quản lý chu kỳ update và render.
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
     * @param deltaTime Khoảng thời gian trôi qua giữa 2 khung hình.
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

    public void start() {
        if (animationTimer != null) {
            animationTimer.start();
            System.out.println(">>> GameLoop đã kích hoạt!");
        }
    }

    public void stop() {
        if (animationTimer != null) {
            animationTimer.stop();
            System.out.println(">>> GameLoop đã tạm dừng!");
        }
    }
}