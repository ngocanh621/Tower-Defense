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
    
    private final List<Enemy> enemies = new ArrayList<>(); // Quan ly enemy
    private final List<Tower> towers = new ArrayList<>(); // Quan ly tower
    private final List<Projectile> projectiles = new ArrayList<>(); // Quản lý Đạn
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
    private int leakedInCurrentWave = 0;

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
        EventBus.getInstance().subscribe(GameEvent.WAVE_STARTED, data -> {
            this.leakedInCurrentWave = 0; // Reset đếm quái lọt lưới khi đợt sóng mới bắt đầu
        });

        EventBus.getInstance().subscribe(GameEvent.ENEMY_DIED, data -> {
            if (data instanceof Enemy enemy) {
                playerState.addGold(enemy.getReward());
                playerState.addScore(enemy.getReward() * 10); // Thưởng điểm số dựa trên loại quái

                // Phát âm thanh theo tên loại quái bằng switch-case đơn giản
                switch (enemy.getType()) { // Hoặc enemy.getEnemyType()
                    case GOBLIN -> SoundManager.getInstance().playSFX("/audio/quai1_Explode.mp3");
                    case ORC -> SoundManager.getInstance().playSFX("/audio/quai2_Explode.mp3");
                    case DRAGON -> SoundManager.getInstance().playSFX("/audio/boss_Explode.mp3");
                }

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
            if (data instanceof Enemy enemy) {
                this.leakedInCurrentWave++;
                playerState.takeDamage(enemy.getPlayerDamage()); // Trừ máu dựa theo độ nguy hiểm từng loại quái
            }
        });

        EventBus.getInstance().subscribe(GameEvent.WAVE_COMPLETED, data -> {
            if (data instanceof Integer waveNum) {
                // Chỉ thưởng điểm nếu hoàn thành Wave hoàn hảo (không để quái lọt lưới)
                if (leakedInCurrentWave == 0) {
                    playerState.addScore(waveNum * 100); // Thưởng điểm hoàn thành đợt sóng xuất sắc
                }
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
        if (mapImage != null && !mapImage.isError()) {
            gc.drawImage(mapImage, 0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        }

        // Step 2: Render các Tháp đã đặt
        renderTowers();

        // Step 3: Render Quái vật
        renderEnemies();

        // Step 4: Render Viên đạn
        renderProjectiles();

        //STEP 4': Render hiệu ứng nổ
        renderExplosions();


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
    private void renderGameOverOverlay() {}

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


    public void handleKeyPress(KeyEvent event) {
    }

    /**
     * Xử lý sự kiện click chuột để đặt Tháp hoặc mở Menu chọn/quản lý Tháp
     */
    public void handleMouseClick(MouseEvent event) {
        if (isGameOver) return; // Khóa tương tác chuột khi thua
        if (event.getButton() != MouseButton.PRIMARY) return;

        double mouseX = event.getX();
        double mouseY = event.getY();

        int col = (int) (mouseX / GameConfig.GRID_CELL_SIZE);
        int row = (int) (mouseY / GameConfig.GRID_CELL_SIZE);

        // 1. NẾU MENU ĐANG MỞ TẠI Ô CỜ HIỆN TẠI: Kiểm tra click vào các nút bấm trong Menu
        if (selectedCol != -1 && selectedRow != -1) {
            Tower existingTower = towers.stream()
                .filter(t -> t.getGridCol() == selectedCol && t.getGridRow() == selectedRow)
                .findFirst().orElse(null);

            if (existingTower != null) {
                // Xử lý click trên Menu Quản Lý Tháp Đã Đặt (Upgrade / Sell)
                int actionResult = checkManagementMenuOptionClick(mouseX, mouseY, existingTower);
                if (actionResult == 1) { // Click UPGRADE
                    if (existingTower.canUpgrade()) {
                        int cost = existingTower.getUpgradeCost();
                        if (playerState.spendGold(cost)) {
                            existingTower.upgrade();
                            System.out.println(">>> Đã nâng cấp tháp lên Level " + existingTower.getLevel());
                        } else {
                            System.out.println(">>> Không đủ vàng nâng cấp tháp (Cần " + cost + " Gold)");
                        }
                    }
                    return;
                } else if (actionResult == 2) { // Click SELL
                    int refund = existingTower.getSellValue();
                    playerState.addGold(refund);
                    towers.remove(existingTower);
                    Cell cell = mapModel.getCell(selectedRow, selectedCol);
                    if (cell != null) {
                        cell.setType(CellType.EMPTY);
                    }
                    System.out.println(">>> Đã bán tháp thu hồi " + refund + " Gold");
                    selectedCol = -1;
                    selectedRow = -1;
                    return;
                }
            } else {
                // Xử lý click trên Menu Mua Tháp Mới
                TowerType selectedType = checkMenuOptionClick(mouseX, mouseY);
                if (selectedType != null) {
                    buildTowerAtSelectedCell(selectedType);
                    selectedCol = -1;
                    selectedRow = -1;
                    return;
                }
            }
        }

        // 2. TÍNH TOÁN VÀ KIỂM TRA Ô CỜ VỪA ĐƯỢC CLICK
        Cell cell = mapModel.getCell(row, col);
        if (cell != null) {
            Tower towerAtCell = towers.stream()
                .filter(t -> t.getGridCol() == col && t.getGridRow() == row)
                .findFirst().orElse(null);

            if (cell.canPlaceTower() || towerAtCell != null) {
                // Mở Menu tại ô cờ (Mua tháp mới HOẶC Quản lý tháp đã có)
                this.selectedCol = col;
                this.selectedRow = row;
            } else {
                this.selectedCol = -1;
                this.selectedRow = -1;
            }
        } else {
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
            boolean hasTower = towers.stream().anyMatch(t -> t.getGridCol() == selectedCol && t.getGridRow() == selectedRow);
            if (hasTower) {
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
     * Kiểm tra click vào nút bấm trong Menu Quản Lý Tháp (1: UPGRADE, 2: SELL, 0: không trúng)
     */
    private int checkManagementMenuOptionClick(double mouseX, double mouseY, Tower tower) {
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
            return 1; // UPGRADE
        }
        if (mouseX >= btn2X && mouseX <= btn2X + btnW && mouseY >= btn2Y && mouseY <= btn2Y + btnH) {
            return 2; // SELL
        }
        return 0;
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
     * Vẽ Menu Popup lựa chọn mua tháp hoặc Nâng cấp / Bán tháp (Style Gỗ Cổ Điển)
     */
    private void renderBuildMenu() {
        if (isGameOver) return;
        if (selectedCol == -1 || selectedRow == -1) return;

        double cellSize = GameConfig.GRID_CELL_SIZE;

        // Highlight ô cờ đang chọn (Viền Vàng Hoàng Gia)
        gc.setStroke(Color.web("#ffd700"));
        gc.setLineWidth(2.5);
        gc.strokeRect(selectedCol * cellSize, selectedRow * cellSize, cellSize, cellSize);

        // Tính toán vị trí Menu
        double menuX = selectedCol * cellSize + cellSize / 2 - menuWidth / 2;
        double menuY = selectedRow * cellSize - menuHeight - 12;

        // Chống tràn màn hình
        if (menuX < 10) menuX = 10;
        if (menuX + menuWidth > GameConfig.WINDOW_WIDTH - 10) menuX = GameConfig.WINDOW_WIDTH - menuWidth - 10;
        if (menuY < 10) menuY = selectedRow * cellSize + cellSize + 12;

        // Kiểm tra xem tại ô cờ đang chọn đã có tháp hay chưa
        Tower existingTower = towers.stream()
                .filter(t -> t.getGridCol() == selectedCol && t.getGridRow() == selectedRow)
                .findFirst().orElse(null);

        // BẬT VÒNG TRÒN BAN KÍNH TẦM BẮN NẾU ĐÃ CÓ THÁP
        if (existingTower != null) {
            float centerX = existingTower.getX() + existingTower.getWidth() / 2f;
            float centerY = existingTower.getY() + existingTower.getHeight() / 2f;
            float range = existingTower.getRange();

            gc.setStroke(Color.rgb(0, 220, 255, 0.85));
            gc.setLineWidth(1.8);
            gc.strokeOval(centerX - range, centerY - range, range * 2, range * 2);
            gc.setFill(Color.rgb(0, 220, 255, 0.12));
            gc.fillOval(centerX - range, centerY - range, range * 2, range * 2);
        }

        // 1. VẼ KHUNG NỀN BẢNG GỖ (WOODEN PANEL)
        gc.setFill(Color.web("#2b1810")); // Màu gỗ đậm
        gc.fillRoundRect(menuX, menuY, menuWidth, menuHeight, 10, 10);

        // Viền kép giả kim loại vàng đồng
        gc.setStroke(Color.web("#8b5a2b"));
        gc.setLineWidth(3);
        gc.strokeRoundRect(menuX, menuY, menuWidth, menuHeight, 10, 10);

        gc.setStroke(Color.web("#d4af37")); // Viền sáng bên trong
        gc.setLineWidth(1);
        gc.strokeRoundRect(menuX + 2, menuY + 2, menuWidth - 4, menuHeight - 4, 8, 8);

        // Căn giữa văn bản cho tiêu đề và các nút
        gc.setTextAlign(TextAlignment.CENTER);

        double btn1X = menuX + 8;
        double btn1Y = menuY + 25;
        double btn2X = menuX + 72;
        double btn2Y = menuY + 25;
        double btnW = 60;
        double btnH = 38;

        if (existingTower == null) {
            // === TRƯỜNG HỢP 1: CHỌN MUA THÁP MỚI ===
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            gc.setFill(Color.web("#fff8dc"));
            gc.fillText("SELECT TOWER", menuX + menuWidth / 2, menuY + 18);

            // --- NÚT 1: GUN TOWER ---
            boolean canAffordGun = playerState.getGold() >= TowerType.GUN.getCost();
            drawWoodenButton(btn1X, btn1Y, btnW, btnH, "⚡ GUN", (int) TowerType.GUN.getCost() + "G", canAffordGun, "#8b4513");

            // --- NÚT 2: SLOW TOWER ---
            boolean canAffordSlow = playerState.getGold() >= TowerType.SLOW.getCost();
            drawWoodenButton(btn2X, btn2Y, btnW, btnH, "❄ SLOW", (int) TowerType.SLOW.getCost() + "G", canAffordSlow, "#1e3d59");

        } else {
            // === TRƯỜNG HỢP 2: NÂNG CẤP HOẶC BÁN THÁP ===
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            gc.setFill(Color.web("#ffd700"));
            gc.fillText("TOWER Lv." + existingTower.getLevel(), menuX + menuWidth / 2, menuY + 18);

            boolean canUpgrade = existingTower.canUpgrade();
            int upgradeCost = existingTower.getUpgradeCost();
            int sellRefund = existingTower.getSellValue();
            boolean canAffordUpgrade = canUpgrade && playerState.getGold() >= upgradeCost;

            // --- NÚT 1: UPGRADE ---
            String costLabel = canUpgrade ? upgradeCost + "G" : "MAX";
            drawWoodenButton(btn1X, btn1Y, btnW, btnH, "▲ UP", costLabel, canAffordUpgrade, "#2e5a27");

            // --- NÚT 2: SELL ---
            drawWoodenButton(btn2X, btn2Y, btnW, btnH, "💰 SELL", "+" + sellRefund + "G", true, "#8b0000");
        }

        // Khôi phục thuộc tính căn lề mặc định
        gc.setTextAlign(TextAlignment.LEFT);
    }

    /**
     * Hàm phụ trợ vẽ Nút Bấm phong cách Gỗ 3D
     */
    private void drawWoodenButton(double x, double y, double w, double h, String title, String cost, boolean enabled, String baseColorHex) {
        if (enabled) {
            // Nền nút bấm
            gc.setFill(Color.web(baseColorHex));
            gc.fillRoundRect(x, y, w, h, 6, 6);

            // Viền nổi 3D sáng góc trên/trái
            gc.setStroke(Color.web("#ffffff", 0.3));
            gc.setLineWidth(1.5);
            gc.strokeRoundRect(x, y, w, h, 6, 6);

            gc.setFill(Color.web("#fff8dc"));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            gc.fillText(title, x + w / 2, y + 16);

            gc.setFill(Color.web("#ffd700")); // Vàng ánh kim cho chi phí Gold
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            gc.fillText(cost, x + w / 2, y + 30);
        } else {
            // Nút bị vô hiệu hóa (Không đủ tiền hoặc đã max)
            gc.setFill(Color.web("#3a3a3a", 0.8));
            gc.fillRoundRect(x, y, w, h, 6, 6);

            gc.setStroke(Color.web("#555555"));
            gc.setLineWidth(1);
            gc.strokeRoundRect(x, y, w, h, 6, 6);

            gc.setFill(Color.web("#888888"));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 11));
            gc.fillText(title, x + w / 2, y + 16);

            gc.setFill(Color.web("#aa5555")); // Màu đỏ nhạt báo thiếu vàng
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            gc.fillText(cost, x + w / 2, y + 30);
        }
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