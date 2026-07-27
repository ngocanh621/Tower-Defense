package com.game.core;

import com.game.controller.PlayerState;
import com.game.controller.WaveManager;
import com.game.map.Cell;
import com.game.map.CellType;
import com.game.map.MapModel;
import com.game.model.Enemy;
import com.game.model.Enemy.EnemyType;
import com.game.model.Projectile;
import com.game.model.Tower;
import com.game.model.Tower.TowerType;
import com.game.system.EventBus;
import com.game.system.GameEvent;
import com.game.util.Constants;
import com.game.util.GameConfig;
import com.game.view.HudRenderer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameScene {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final MapModel mapModel;
    
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Tower> towers = new ArrayList<>(); // 1. Danh sách quản lý Tháp
    private final List<Projectile> projectiles = new ArrayList<>(); // Danh sách quản lý Đạn
    private final WaveManager waveManager; // Quản lý sóng quái vật
    private final PlayerState playerState; // Quản lý máu và vàng của người chơi
    private final HudRenderer hudRenderer; // Hiển thị giao diện HUD

    private Image mapImage;

    public GameScene(Canvas canvas, GraphicsContext gc) {
        this.canvas = canvas;
        this.gc = gc;
        this.mapModel = new MapModel();
        this.mapImage = loadMapImage();
        this.waveManager = new WaveManager();
        this.playerState = new PlayerState();
        this.hudRenderer = new HudRenderer();

        setupEventListeners();
    }

    /**
     * Đăng ký nhận sự kiện từ EventBus để cập nhật điểm/máu và lưu kỷ lục người chơi.
     */
    private void setupEventListeners() {
        EventBus.getInstance().subscribe(GameEvent.ENEMY_DIED, data -> {
            if (data instanceof Enemy enemy) {
                playerState.addGold(enemy.getReward());
                playerState.addScore(enemy.getReward() * 10); // Thưởng điểm số dựa trên loại quái
            }
        });

        EventBus.getInstance().subscribe(GameEvent.ENEMY_REACHED_BASE, data -> {
            if (data instanceof Enemy) {
                playerState.takeDamage(1);
            }
        });

        EventBus.getInstance().subscribe(GameEvent.WAVE_COMPLETED, data -> {
            if (data instanceof Integer waveNum) {
                playerState.addScore(waveNum * 100); // Thưởng điểm khi hoàn thành đợt sóng
            }
        });

        EventBus.getInstance().subscribe(GameEvent.GAME_OVER, data -> {
            // Tự động lưu Kỷ lục Best Score khi người chơi bị thua
            SceneManager.saveHighScore(playerState.getScore());
        });
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
                // Bỏ qua lỗi load ảnh
            }
        }

        return null;
    }

    public void update(double deltaTime) {
        // Cập nhật WaveManager để sinh quái vật theo sóng
        waveManager.update(deltaTime, mapModel, enemies);

        // Cập nhật quái vật (xóa quái khi active = false)
        Iterator<Enemy> iterator = enemies.iterator();
        while (iterator.hasNext()) {
            Enemy enemy = iterator.next();
            enemy.update(deltaTime);
            if (!enemy.isActive()) {
                iterator.remove();
            }
        }

        // 2. Cập nhật cooldown / nạp đạn cho tất cả các tháp và bắn đạn
        for (Tower tower : towers) {
            tower.update(deltaTime);
            if (tower.canFire()) {
                for (Enemy enemy : enemies) {
                    if (enemy.isActive() && tower.isEnemyInRange(enemy)) {
                        Projectile p = tower.fire(enemy);
                        if (p != null) {
                            projectiles.add(p);
                        }
                        break;
                    }
                }
            }
        }

        // 3. Cập nhật đạn (xóa đạn khi active = false)
        Iterator<Projectile> projIterator = projectiles.iterator();
        while (projIterator.hasNext()) {
            Projectile projectile = projIterator.next();
            projectile.update(deltaTime);
            if (!projectile.isActive()) {
                projIterator.remove();
            }
        }
    }

    public void render() {
        // Step 1: Vẽ ảnh nền Map (hoặc fallback màu sắc)
        if (mapImage != null && !mapImage.isError()) {
            gc.drawImage(mapImage, 0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        } else {
            gc.setFill(Color.web(Constants.COLOR_BACKGROUND));
            gc.fillRect(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
            drawMapFallback();
        }

        // Step 2: Render các Tháp đã đặt
        renderTowers();

        // Step 3: Render Quái vật
        renderEnemies();

        // Step 4: Render Viên đạn
        renderProjectiles();

        // Step 5: Vẽ lưới ô cờ (Overlay)
        drawGridOverlay();

        // Step 6: Hiển thị giao diện HUD (Máu, Vàng, Wave)
        hudRenderer.render(gc, playerState, waveManager);
    }

    /**
     * Duyệt danh sách và vẽ từng tháp lên Canvas
     */
    private void renderTowers() {
        for (Tower tower : towers) {
            tower.render(gc);
        }
    }

    private void renderEnemies() {
        for (Enemy enemy : enemies) {
            enemy.render(gc);
        }
    }

    private void renderProjectiles() {
        for (Projectile projectile : projectiles) {
            projectile.render(gc);
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
                if (cell != null && cell.getType() == CellType.PATH) {
                    gc.setFill(Color.web(Constants.COLOR_PATH));
                    gc.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
            }
        }
    }
    


    public void handleKeyPress(KeyEvent event) {
    }

    /**
     * Xử lý sự kiện click chuột để đặt Tháp
     */
    public void handleMouseClick(MouseEvent event) {
        // Chỉ xử lý click chuột trái
        if (event.getButton() != MouseButton.PRIMARY) return;

        double mouseX = event.getX();
        double mouseY = event.getY();
        int col = (int) (mouseX / GameConfig.GRID_CELL_SIZE);
        int row = (int) (mouseY / GameConfig.GRID_CELL_SIZE);

        Cell cell = mapModel.getCell(row, col);

        // Kiểm tra xem vị trí ô có hợp lệ và là ô đất trống không
        if (cell != null && cell.canPlaceTower()) {
            // 1. Khởi tạo tháp mới (ở đây mặc định tạo TowerType.GUN)
            Tower tower = new Tower(col, row, TowerType.GUN);
            towers.add(tower);

            // 2. Cập nhật loại ô thành OCCUPIED để không đè tháp khác lên
            cell.setType(CellType.OCCUPIED);

            System.out.println(">>> Đã đặt Tháp tại Row: " + row + ", Col: " + col);
        } else {
            System.out.println(">>> Không thể đặt tháp tại Row: " + row + ", Col: " + col);
        }
    }

    public void handleMouseMove(MouseEvent event) {
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }
}