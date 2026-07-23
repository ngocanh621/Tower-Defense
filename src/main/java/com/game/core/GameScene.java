package com.game.core;

import com.game.map.Cell;
import com.game.map.CellType;
import com.game.map.MapModel;
import com.game.model.Enemy;
import com.game.model.Enemy.EnemyType;
import com.game.util.Constants;
import com.game.util.GameConfig;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    private final List<Enemy> enemies = new ArrayList<>();
    private Image mapImage;

    public GameScene(Canvas canvas, GraphicsContext gc) {
        this.canvas = canvas;
        this.gc = gc;
        this.mapModel = new MapModel();
        this.mapImage = loadMapImage();
        initializeEnemies();
    }

    private Image loadMapImage() {
        String[] candidates = {"/assets/map.jpg", "/assets/map.png", "/assets/map.jpeg"};

        for (String path : candidates) {
            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is != null) {
                    Image image = new Image(is);
                    if (!image.isError()) {
                        return image;
                    }
                }
            } catch (Exception e) {
            }
        }

        return null;
    }

    public void update(double deltaTime) {
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(deltaTime);
            if (!enemy.isActive()) {
                iterator.remove();
            }
        }
    }

    public void render() {
        if (mapImage != null && !mapImage.isError()) {
            gc.drawImage(mapImage, 0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        } else {
            gc.setFill(Color.web(Constants.COLOR_BACKGROUND));
            gc.fillRect(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
            drawMapFallback();
        }

        renderEnemies();
        drawGridOverlay();
    }

    private void renderEnemies() {
        for (Enemy enemy : enemies) {
            enemy.render(gc);
        }
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

    private void initializeEnemies() {
        Enemy goblin = new Enemy();
        goblin.initialize(EnemyType.GOBLIN, mapModel);
        goblin.setY(goblin.getY() - 10f);
        enemies.add(goblin);

        Enemy orc = new Enemy();
        orc.initialize(EnemyType.ORC, mapModel);
        orc.setY(orc.getY() + 10f);
        enemies.add(orc);

        Enemy dragon = new Enemy();
        dragon.initialize(EnemyType.DRAGON, mapModel);
        dragon.setY(dragon.getY() + 20f);
        enemies.add(dragon);
    }
}