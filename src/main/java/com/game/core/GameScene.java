package com.game.core;

import com.game.map.Cell;
import com.game.map.CellType;
import com.game.map.MapModel;
import com.game.util.Constants;
import com.game.util.GameConfig;
import java.io.InputStream;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameScene {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final MapModel mapModel;
    private Image mapImage;

    public GameScene(Canvas canvas, GraphicsContext gc) {
        this.canvas = canvas;
        this.gc = gc;
        this.mapModel = new MapModel();
        this.mapImage = loadMapImage();
    }

    private Image loadMapImage() {
        String[] candidates = {"/assets/map.jpg", "/assets/map.png", "/assets/map.jpeg"};

        for (String path : candidates) {
            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is != null) {
                    Image image = new Image(is);
                    if (!image.isError()) {
                        System.out.println(">>> Load ảnh " + path + " thành công!");
                        return image;
                    }
                }
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi load ảnh " + path + ": " + e.getMessage());
            }
        }

        System.out.println("⚠️ Không tìm thấy ảnh map trong resources/assets, dùng màu nền mặc định.");
        return null;
    }

    public void update(double deltaTime) {
        // Logic cập nhật đạn, quái, tháp...
    }

    public void render() {
        if (mapImage != null && !mapImage.isError()) {
            gc.drawImage(mapImage, 0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        } else {
            gc.setFill(Color.web(Constants.COLOR_BACKGROUND));
            gc.fillRect(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
            drawMapFallback();
        }

        drawGridOverlay();
    }

    private void drawGridOverlay() {
        double cellSize = GameConfig.GRID_CELL_SIZE;
        gc.setStroke(Color.rgb(255, 255, 255, 0.15));
        gc.setLineWidth(0.5);

        for (int r = 0; r < mapModel.getRows(); r++) {
            for (int c = 0; c < mapModel.getCols(); c++) {
                gc.strokeRect(c * cellSize, r * cellSize, cellSize, cellSize);
            }
        }
    }

    private void drawMapFallback() {
        Cell[][] grid = mapModel.getGrid();
        double cellSize = GameConfig.GRID_CELL_SIZE;

        for (int r = 0; r < mapModel.getRows(); r++) {
            for (int c = 0; c < mapModel.getCols(); c++) {
                Cell cell = grid[r][c];
                if (cell.getType() == CellType.PATH) {
                    gc.setFill(Color.web(Constants.COLOR_PATH));
                    gc.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    public void handleKeyPress(KeyEvent event) {
    }

    public void handleMouseClick(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        int col = (int) (mouseX / GameConfig.GRID_CELL_SIZE);
        int row = (int) (mouseY / GameConfig.GRID_CELL_SIZE);

        System.out.println(">>> Click ô Row: " + row + ", Col: " + col);
    }

    public void handleMouseMove(MouseEvent event) {
    }
}