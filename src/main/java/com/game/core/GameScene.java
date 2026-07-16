package com.game.core;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Màn chơi chính
 * Dùng để cập nhật game và vẽ game
 */
public class GameScene {

    private final Canvas canvas;
    private final GraphicsContext gc;

    public GameScene(Canvas canvas, GraphicsContext gc) {
        this.canvas = canvas;
        this.gc = gc;
    }

    /**
     * 
     * @param deltaTime thời gian giữa hai khung hình
     */
    public void update(double deltaTime) {
        
    }

    
    public void render() {
        gc.setFill(Color.web("#0a0e27"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawPlaceholderGrid();

    }

    public void handleKeyPress(KeyEvent event) {

    }

    public void handleMouseClick(MouseEvent event) {

    }

    
    public void handleMouseMove(MouseEvent event) {

    }

    private void drawPlaceholderGrid() {
        int cellSize = 40;
        int rows = (int) canvas.getHeight() / cellSize;
        int cols = (int) canvas.getWidth() / cellSize;

        gc.setStroke(Color.web("#333333"));
        gc.setLineWidth(1);

        for (int i = 0; i <= cols; i++) {
            gc.strokeLine(i * cellSize, 0, i * cellSize, canvas.getHeight());
        }
        for (int i = 0; i <= rows; i++) {
            gc.strokeLine(0, i * cellSize, canvas.getWidth(), i * cellSize);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Arial", 24));
        gc.fillText("Game Scene (Placeholder)", 50, 50);
        gc.setFont(javafx.scene.text.Font.font("Arial", 16));
        gc.fillText("Grid: " + cols + "x" + rows + " cells", 50, 80);
        gc.fillText("Press ESC to return to menu", 50, 110);
    }
}
