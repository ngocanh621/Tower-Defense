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
import com.game.system.SoundManager;
import com.game.util.Constants;
import com.game.util.ExplosionEffect;
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

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameScene {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private final MapModel mapModel;
    
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Tower> towers = new ArrayList<>(); // 1. Danh sách quản lý Tháp
    private final List<Projectile> projectiles = new ArrayList<>(); // Danh sách quản lý Đạn
    private final List<ExplosionEffect> explosions = new ArrayList<>();
    private final WaveManager waveManager; // Quản lý sóng quái vật
    private final PlayerState playerState; // Quản lý máu và vàng của người chơi
    private final HudRenderer hudRenderer; // Hiển thị giao diện HUD

    private Image mapImage;

    // Thêm hiệu ứng hover để hiện vị trí đặt được tháp
    private int hoverCol = -1; // -1 nghĩa là chuột đang ở ngoài Canvas
    private int hoverRow = -1;

    // Vị trí ô đang mở Menu xây dựng tháp (-1 nếu chưa mở)
    private int selectedCol = -1;
    private int selectedRow = -1;

    // Kích thước menu chọn tháp (Popup)
    private final double menuWidth = 140;
    private final double menuHeight = 70;

    private boolean isGameOver = false;
    private boolean isNewRecord = false;
    private int finalScore = 0;

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

                // Kiểm tra xem quái này có ảnh hiệu ứng vỡ/nổ khi chết hay không
                String expPath = enemy.getExplosionImagePath();
                if (expPath != null) {
                    explosions.add(new ExplosionEffect(
                            enemy.getX(),
                            enemy.getY(),
                            enemy.getWidth(),
                            enemy.getHeight(),
                            expPath
                    ));
                }
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

        String[] candidates = {"/assets/map3.jpg", "/assets/map3.png", "/assets/map3.jpeg"};

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
        // === KHÔNG CẬP NHẬT TRẠNG THÁI GAME KHI ĐÃ GAME OVER ===
        if (isGameOver) {
            return;
        }

        // === KIỂM TRA ĐIỀU KIỆN THUA (HẾT MẠNG) ===
        if (playerState.getHealth() <= 0) {
            triggerGameOver();
            return;
        }

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

        //4. CẬP NHẬT HIỆU ỨNG NỔ (Xóa hiệu ứng khi active = false)
        Iterator<ExplosionEffect> expIterator = explosions.iterator();
        while (expIterator.hasNext()) {
            ExplosionEffect exp = expIterator.next();
            exp.update(deltaTime);
            if (!exp.isActive()) {
                expIterator.remove();
            }
        }
    }

    /**
     * Kích hoạt trạng thái Game Over, tính điểm và lưu kỷ lục.
     */
    private void triggerGameOver() {
        this.isGameOver = true;
        this.finalScore = playerState.getScore();

        int currentBest = SceneManager.loadHighScore();
        if (finalScore > currentBest) {
            this.isNewRecord = true;
            SceneManager.saveHighScore(finalScore); // Lưu kỷ lục mới
        }

        // Dừng nhạc nền gameplay
        SoundManager.getInstance().stopBGM();
        EventBus.getInstance().publish(GameEvent.GAME_OVER, finalScore);
    }

    /**
     * Vẽ hiệu ứng tô sáng (Highlight) ô cờ đang được con trỏ chuột hover.
     */
    private void renderTileHover() {
        // Kiểm tra vị trí ô hợp lệ trong phạm vi ma trận bản đồ
        if (hoverRow < 0 || hoverRow >= mapModel.getRows() ||
                hoverCol < 0 || hoverCol >= mapModel.getCols()) {
            return;
        }

        double cellSize = GameConfig.GRID_CELL_SIZE;
        double x = hoverCol * cellSize;
        double y = hoverRow * cellSize;

        Cell cell = mapModel.getCell(hoverRow, hoverCol);

        // kiểm tra xem ô này có cho phép đặt tháp hay không
        boolean canPlace = (cell != null && cell.canPlaceTower());

        if (canPlace) {
            // ĐẶT ĐƯỢC: Tô màu xanh lá trong suốt + viền xanh sáng
            gc.setFill(Color.rgb(0, 255, 0, 0.25));
            gc.fillRect(x, y, cellSize, cellSize);

            gc.setStroke(Color.LIME);
            gc.setLineWidth(2);
            gc.strokeRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
        } else {
            // KHÔNG ĐẶT ĐƯỢC: Tô màu đỏ trong suốt + viền đỏ
            gc.setFill(Color.rgb(255, 0, 0, 0.25));
            gc.fillRect(x, y, cellSize, cellSize);

            gc.setStroke(Color.RED);
            gc.setLineWidth(2);
            gc.strokeRect(x + 1, y + 1, cellSize - 2, cellSize - 2);
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

        //STEP 4': Render hiệu ứng nổ
        renderExplosions();

        // Step 5: Vẽ lưới ô cờ (Overlay)
        drawGridOverlay();

        // Step 5': Hiển thị hiệu ứng hover tại vị trí chuột
        renderTileHover();

        // Step 6: Hiển thị Menu chọn tháp nếu đang chọn ô
        renderBuildMenu();

        // Step 7: Hiển thị giao diện HUD (Máu, Vàng, Wave)
        hudRenderer.render(gc, playerState, waveManager);

        // Step 8: Vẽ bảng thông báo GAME OVER đè lên trên nếu thua
        if (isGameOver) {
            renderGameOverOverlay();
        }
    }

    /**
     * Vẽ bảng pop-up Game Over chuyên nghiệp
     */
    private void renderGameOverOverlay() {
        double width = GameConfig.WINDOW_WIDTH;
        double height = GameConfig.WINDOW_HEIGHT;

        // 1. Lớp phủ đen mờ toàn màn hình
        gc.setFill(Color.rgb(0, 0, 0, 0.75));
        gc.fillRect(0, 0, width, height);

        // 2. Kích thước & Vị trí khung bảng điểm
        double boxW = 440;
        double boxH = 290;
        double boxX = (width - boxW) / 2;
        double boxY = (height - boxH) / 2;

        // Nền khung bảng
        gc.setFill(Color.web("#1e293b"));
        gc.fillRoundRect(boxX, boxY, boxW, boxH, 20, 20);

        // Viền khung: Vàng đồng nếu lập kỷ lục, Xám sẫm nếu bình thường
        gc.setStroke(isNewRecord ? Color.web("#f59e0b") : Color.web("#475569"));
        gc.setLineWidth(3.5);
        gc.strokeRoundRect(boxX, boxY, boxW, boxH, 20, 20);

        // 3. Tiêu đề "GAME OVER"
        gc.setFill(Color.web("#ef4444"));
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 38));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("GAME OVER", width / 2, boxY + 55);

        // 4. Nếu đạt kỷ lục mới -> Dòng thông báo chúc mừng đặc biệt
        if (isNewRecord) {
            gc.setFill(Color.web("#f59e0b"));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 19));
            gc.fillText("🎉 CHÚC MỪNG KỶ LỤC MỚI! 🎉", width / 2, boxY + 95);
        } else {
            gc.setFill(Color.web("#94a3b8"));
            gc.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
            gc.fillText("Tháp của bạn đã bị tiêu diệt!", width / 2, boxY + 95);
        }

        // 5. Hiển thị Điểm số hiện tại & Best Score
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + finalScore, width / 2, boxY + 145);

        int bestScore = SceneManager.loadHighScore();
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gc.setFill(Color.web("#f59e0b"));
        gc.fillText("🏆 Best Score: " + bestScore, width / 2, boxY + 180);

        // 6. Hướng dẫn nút bấm
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setFill(Color.web("#cbd5e1"));
        gc.fillText("Nhấn [ ESC ] để trở về Menu chính", width / 2, boxY + 245);

        // Reset alignment về LEFT mặc định để tránh ảnh hưởng đoạn render khác
        gc.setTextAlign(TextAlignment.LEFT);
    }

    /**
     * Duyệt danh sách và vẽ các hiệu ứng nổ
     */
    private void renderExplosions() {
        for (ExplosionEffect exp : explosions) {
            exp.render(gc);
        }
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
     * Xử lý sự kiện click chuột để đặt Tháp hoặc mở Menu chọn Tháp
     */
    public void handleMouseClick(MouseEvent event) {
        if (isGameOver) return; // Khóa tương tác chuột khi thua
        if (event.getButton() != MouseButton.PRIMARY) return;

        double mouseX = event.getX();
        double mouseY = event.getY();

        // 1. NẾU MENU ĐANG MỞ: Kiểm tra click chọn nút trong Menu
        if (selectedCol != -1 && selectedRow != -1) {
            TowerType selectedType = checkMenuOptionClick(mouseX, mouseY);
            if (selectedType != null) {
                buildTowerAtSelectedCell(selectedType);
                selectedCol = -1;
                selectedRow = -1;
                return;
            }
        }

        // 2. TÍNH TOÁN Ô CỜ ĐƯỢC CLICK
        int col = (int) (mouseX / GameConfig.GRID_CELL_SIZE);
        int row = (int) (mouseY / GameConfig.GRID_CELL_SIZE);

        Cell cell = mapModel.getCell(row, col);

        // 3. NẾU CLICK VÀO Ô ĐẶT ĐƯỢC THÁP -> MỞ MENU TẠI Ô ĐÓ
        if (cell != null && cell.canPlaceTower()) {
            this.selectedCol = col;
            this.selectedRow = row;
        } else {
            // Click ra vị trí không hợp lệ -> Đóng menu
            this.selectedCol = -1;
            this.selectedRow = -1;
        }
    }

    /**
     * Xử lý mua tháp khi người chơi chọn trong Menu
     */
    private void buildTowerAtSelectedCell(TowerType type) {
        Cell cell = mapModel.getCell(selectedRow, selectedCol);
        if (cell != null && cell.canPlaceTower()) {
            // Kiểm tra chắc chắn chưa có tháp nào tại ô cờ này
            boolean hasTower = towers.stream().anyMatch(t -> t.getGridCol() == selectedCol && t.getGridRow() == selectedRow);
            if (hasTower) {
                System.out.println(">>> Ô cờ này đã có tháp rồi!");
                return;
            }

            int cost = (int) type.getCost();
            if (playerState.spendGold(cost)) {
                Tower tower = new Tower(selectedCol, selectedRow, type);
                towers.add(tower);
                cell.setType(CellType.OCCUPIED);
                System.out.println(">>> Đã mua tháp " + type + " tại Row: " + selectedRow + ", Col: " + selectedCol);
            } else {
                System.out.println(">>> Không đủ vàng mua tháp " + type + " (Cần " + cost + " Gold)");
            }
        }
    }

    /**
     * Kiểm tra xem click chuột có trúng nút bấm nào trong Menu chọn tháp không
     */
    private TowerType checkMenuOptionClick(double mouseX, double mouseY) {
        double cellSize = GameConfig.GRID_CELL_SIZE;
        double menuX = selectedCol * cellSize + cellSize / 2 - menuWidth / 2;
        double menuY = selectedRow * cellSize - menuHeight - 10;

        if (menuX < 10) menuX = 10;
        if (menuX + menuWidth > GameConfig.WINDOW_WIDTH - 10) menuX = GameConfig.WINDOW_WIDTH - menuWidth - 10;
        if (menuY < 10) menuY = selectedRow * cellSize + cellSize + 10;

        double btn1X = menuX + 8;
        double btn1Y = menuY + 24;
        double btn2X = menuX + 72;
        double btn2Y = menuY + 24;
        double btnW = 60;
        double btnH = 38;

        if (mouseX >= btn1X && mouseX <= btn1X + btnW && mouseY >= btn1Y && mouseY <= btn1Y + btnH) {
            return TowerType.GUN;
        }
        if (mouseX >= btn2X && mouseX <= btn2X + btnW && mouseY >= btn2Y && mouseY <= btn2Y + btnH) {
            return TowerType.SLOW;
        }

        return null;
    }

    /**
     * Vẽ Menu Popup lựa chọn mua tháp
     */
   private void renderBuildMenu() {
        if (selectedCol == -1 || selectedRow == -1) return;

        double cellSize = GameConfig.GRID_CELL_SIZE;

        // Highlight ô cờ đang chọn (Viền Vàng)
        gc.setStroke(Color.rgb(184, 134, 11, 0.8));
        gc.setLineWidth(3);
        gc.strokeRect(selectedCol * cellSize, selectedRow * cellSize, cellSize, cellSize);

        // Vị trí Menu
        double menuX = selectedCol * cellSize + cellSize / 2 - menuWidth / 2;
        double menuY = selectedRow * cellSize - menuHeight - 10;

        if (menuX < 10) menuX = 10;
        if (menuX + menuWidth > GameConfig.WINDOW_WIDTH - 10) menuX = GameConfig.WINDOW_WIDTH - menuWidth - 10;
        if (menuY < 10) menuY = selectedRow * cellSize + cellSize + 10;

        // Khung nền Menu Popup mờ kính
        gc.setFill(Color.rgb(62, 39, 25, 0.95));
        gc.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 6, 6);

        gc.setStroke(Color.rgb(139, 90, 43));
        gc.setLineWidth(1.5);
        gc.strokeRoundRect(menuX, menuY, menuWidth, menuHeight, 6, 6);

        // Tiêu đề Menu
        gc.setFont(Font.font("Georgia", FontWeight.BOLD, 11));
        gc.setFill(Color.rgb(255, 248, 220));
        gc.fillText("SELECT TOWER", menuX + 20, menuY + 16);

        // Nút 1: GUN TOWER (100G)
        double btn1X = menuX + 8;
        double btn1Y = menuY + 24;
        double btnW = 60;
        double btnH = 38;

        boolean canAffordGun = playerState.getGold() >= TowerType.GUN.getCost();
        gc.setFill(canAffordGun ? Color.rgb(160, 82, 45) : Color.rgb(80, 80, 80, 0.7));
        gc.fillRoundRect(btn1X, btn1Y, btnW, btnH, 5, 5);
        gc.setStroke(canAffordGun ? Color.rgb(218, 165, 32) : Color.GRAY);
        gc.setLineWidth(1);
        gc.strokeRoundRect(btn1X, btn1Y, btnW, btnH, 5, 5);

        gc.setFont(Font.font("Georgia", FontWeight.BOLD, 11));
        gc.setFill(Color.rgb(255, 248, 220));
        gc.fillText("🔫 GUN", btn1X + 8, btn1Y + 16);
        gc.setFont(Font.font("Georgia", FontWeight.NORMAL, 10));
        gc.setFill(canAffordGun ? Color.rgb(255, 215, 0) : Color.LIGHTGRAY);
        gc.fillText((int)TowerType.GUN.getCost() + "G", btn1X + 16, btn1Y + 30);

        // Nút 2: SLOW TOWER
        double btn2X = menuX + 72;
        double btn2Y = menuY + 24;

        boolean canAffordSlow = playerState.getGold() >= TowerType.SLOW.getCost();
        gc.setFill(canAffordSlow ? Color.rgb(25, 25, 112) : Color.rgb(80, 80, 80, 0.7));
        gc.fillRoundRect(btn2X, btn2Y, btnW, btnH, 5, 5);
        gc.setStroke(canAffordSlow ? Color.rgb(100, 149, 237) : Color.GRAY);
        gc.strokeRoundRect(btn2X, btn2Y, btnW, btnH, 5, 5);

        gc.setFont(Font.font("Georgia", FontWeight.BOLD, 11));
        gc.setFill(Color.rgb(255, 248, 220));
        gc.fillText("❄️ SLOW", btn2X + 5, btn2Y + 16);
        gc.setFont(Font.font("Georgia", FontWeight.NORMAL, 10));
        gc.setFill(canAffordSlow ? Color.rgb(255, 215, 0) : Color.LIGHTGRAY);
        gc.fillText((int)TowerType.SLOW.getCost() + "G", btn2X + 16, btn2Y + 30);
    }

    /**
     * Cập nhật vị trí con trỏ chuột khi di chuyển trên Canvas
     */
    public void handleMouseMove(MouseEvent event) {
            double mouseX = event.getX();
            double mouseY = event.getY();

            // Tự động tính toán ra ô Col và Row tương ứng
            this.hoverCol = (int) (mouseX / GameConfig.GRID_CELL_SIZE);
            this.hoverRow = (int) (mouseY / GameConfig.GRID_CELL_SIZE);
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public boolean isGameOver() {return isGameOver;}
}