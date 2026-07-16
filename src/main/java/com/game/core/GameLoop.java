package com.game.core;

import javafx.animation.AnimationTimer;

/**
 * Main game loop using JavaFX's AnimationTimer.
 * Runs at approximately 60 FPS and manages update/render cycle.
 */
public class GameLoop {

    private static final double TARGET_FPS = 60.0;
    private static final double FRAME_TIME = 1.0 / TARGET_FPS; 

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
                // Calculate delta time in seconds
                long currentFrameTime = System.nanoTime();
                double deltaTime;

                if (lastFrameTime == 0) {
                    deltaTime = FRAME_TIME;
                } else {
                    deltaTime = (currentFrameTime - lastFrameTime) / 1_000_000_000.0;
                }
                lastFrameTime = currentFrameTime;

                // Cap delta time to prevent large jumps (e.g., when debugging)
                if (deltaTime > 0.05) {
                    deltaTime = 0.05;
                }

                // Update game state
                update(deltaTime);

                // Render (JavaFX handles rendering automatically through Scene graph)
                render();
            }
        };
    }

    /**
     * Update game logic for the current frame.
     *
     * @param deltaTime Time elapsed since last frame in seconds
     */
    private void update(double deltaTime) {
        // Delegate to scene manager to update current scene
        if (sceneManager.getCurrentGame() != null) {
            sceneManager.getCurrentGame().update(deltaTime);
        }
    }

    /**
     * Render frame (delegated to JavaFX Scene graph).
     */
    private void render() {
        if (sceneManager.getCurrentGame() != null) {
            sceneManager.getCurrentGame().render();
        }
    }

    /**
     * Start the game loop.
     */
    public void start() {
        if (animationTimer != null) {
            animationTimer.start();
            System.out.println("GameLoop started");
        }
    }

    /**
     * Stop the game loop.
     */
    public void stop() {
        if (animationTimer != null) {
            animationTimer.stop();
            System.out.println("GameLoop stopped");
        }
    }
}
