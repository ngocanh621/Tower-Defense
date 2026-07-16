
package com.game.core;

import com.game.util.Constants;
import com.game.util.GameConfig;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

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
     * @param deltaTime 
     */
    public void update(double deltaTime) {
    }

   
    public void render() {
        gc.setFill(Color.web(Constants.COLOR_BACKGROUND));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (Constants.DEBUG_GRID) {
            drawPlaceholderGrid();
        }
    }

   
    public void handleKeyPress(KeyEvent event) {
    }


    public void handleMouseClick(MouseEvent event) {
    }

    public void handleMouseMove(MouseEvent event) {
    }

    private void drawPlaceholderGrid() {
        int cellSize = GameConfig.GRID_CELL_SIZE;
        int rows = GameConfig.GRID_HEIGHT;
        int cols = GameConfig.GRID_WIDTH;

        gc.setStroke(Color.web(Constants.COLOR_GRID));
        gc.setLineWidth(1);

        // Vẽ đường kẻ dọc
        for (int i = 0; i <= cols; i++) {
            gc.strokeLine(i * cellSize, 0, i * cellSize, canvas.getHeight());
        }
        // Vẽ đường kẻ ngang
        for (int i = 0; i <= rows; i++) {
            gc.strokeLine(0, i * cellSize, canvas.getWidth(), i * cellSize);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 24));
        gc.fillText("Game Scene (Testing Mode)", 50, 50);
        
        gc.setFont(Font.font("Arial", Constants.HUD_FONT_SIZE));
        gc.fillText("Grid Map: " + cols + "x" + rows + " cells (" + cellSize + "x" + cellSize + "px)", 50, 80);
        gc.fillText("Press ESC to return to Main Menu", 50, 110);
    }
}