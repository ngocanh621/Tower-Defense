package com.game.model;

import com.game.util.Constants;
import com.game.util.GameConfig;
import java.io.InputStream;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Đại diện cho Tháp phòng thủ trong game Tower Defense.
 * Tháp tự động tìm kiếm quái vật gần nhất trong tầm bắn và xả đạn.
 */
public class Tower extends Entity {

    public enum TowerType {
        GUN(GameConfig.TOWER_GUN_COST, GameConfig.TOWER_GUN_RANGE, GameConfig.TOWER_GUN_FIRE_RATE, Color.CYAN, "/assets/gun_tower.png"),
        SLOW(GameConfig.TOWER_SLOW_COST, GameConfig.TOWER_SLOW_RANGE, GameConfig.TOWER_SLOW_FIRE_RATE, Color.PURPLE, "/assets/slow_tower.png");

        private final float cost;
        private final float range;
        private final float fireRate; // Số phát bắn mỗi giây
        private final Color color;
        private final String imagePath;

        TowerType(float cost, float range, float fireRate, Color color, String imagePath) {
            this.cost = cost;
            this.range = range;
            this.fireRate = fireRate;
            this.color = color;
            this.imagePath = imagePath;
        }

        public float getCost() { return cost; }
        public float getRange() { return range; }
        public float getFireRate() { return fireRate; }
        public Color getColor() { return color; }
        public String getImagePath() { return imagePath; }
    }

    private final int gridCol;
    private final int gridRow;
    private final TowerType type;
    private float range;
    private float fireRate;
    private float cooldownTimer;
    private Image sprite;

    private int level = 1;
    private static final int MAX_LEVEL = 3;
    private int totalInvestedGold;

    /**
     * Khởi tạo Tháp phòng thủ tại tọa độ ô vuông
     * @param gridCol Cột trên bản đồ
     * @param gridRow Hàng trên bản đồ
     * @param type Loại tháp (GUN hoặc SLOW)
     */
    public Tower(int gridCol, int gridRow, TowerType type) {
        // Căn chỉnh tháp vừa vặn chính giữa ô cờ dựa trên GameConfig.TOWER_SIZE
        super(
            gridCol * GameConfig.GRID_CELL_SIZE + (GameConfig.GRID_CELL_SIZE - GameConfig.TOWER_SIZE) / 2f,
            gridRow * GameConfig.GRID_CELL_SIZE + (GameConfig.GRID_CELL_SIZE - GameConfig.TOWER_SIZE) / 2f,
            GameConfig.TOWER_SIZE,
            GameConfig.TOWER_SIZE
        );
        this.gridCol = gridCol;
        this.gridRow = gridRow;
        this.type = type;
        this.range = type.getRange();
        this.fireRate = type.getFireRate();
        this.cooldownTimer = 0f;
        this.level = 1;
        this.totalInvestedGold = (int) type.getCost();
        
        loadSprite();
    }

    public int getGridCol() {
        return gridCol;
    }

    public int getGridRow() {
        return gridRow;
    }

    public TowerType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public float getRange() {
        return range;
    }

    public boolean canUpgrade() {
        return level < MAX_LEVEL;
    }

    /**
     * Giá nâng cấp = 1.5 * giá gốc cho Lv2, 2.25 * giá gốc cho Lv3
     */
    public int getUpgradeCost() {
        if (!canUpgrade()) return 0;
        return (int) (type.getCost() * (1.5f * level));
    }

    /**
     * Giá bán tháp = 70% tổng số vàng đã đầu tư
     */
    public int getSellValue() {
        return (int) (totalInvestedGold * 0.7f);
    }

    /**
     * Thực hiện nâng cấp tháp
     */
    public boolean upgrade() {
        if (!canUpgrade()) return false;
        int cost = getUpgradeCost();
        totalInvestedGold += cost;
        level++;
        // Tăng tầm bắn 15% mỗi cấp
        this.range = type.getRange() * (1.0f + (level - 1) * 0.15f);
        // Tăng tốc độ bắn 10% mỗi cấp
        this.fireRate = type.getFireRate() * (1.0f + (level - 1) * 0.10f);
        return true;
    }

    /**
     * Nạp ảnh Sprite của tháp từ thư mục tài nguyên resources
     */
    private void loadSprite() {
        try (InputStream is = getClass().getResourceAsStream(type.getImagePath())) {
            if (is != null) {
                Image img = new Image(is);
                if (!img.isError()) {
                    this.sprite = img;
                }
            }
        } catch (Exception e) {
            // Không nạp được ảnh thì sprite sẽ bằng null (chuyển sang chế độ vẽ khối màu)
            this.sprite = null;
        }
    }

    @Override
    public void update(double deltaTime) {
        if (!active) return;

        // Giảm thời gian nạp đạn theo thời gian thực (deltaTime)
        if (cooldownTimer > 0) {
            cooldownTimer -= deltaTime;
        }
    }

    /**
     * Kiểm tra xem tháp đã nạp đạn xong để bắn phát tiếp theo chưa
     */
    public boolean canFire() {
        return cooldownTimer <= 0;
    }

    /**
     * Đặt lại bộ đếm thời gian hồi chiêu sau khi bắn đạn
     */
    public void resetCooldown() {
        if (fireRate > 0) {
            this.cooldownTimer = 1.0f / fireRate;
        }
    }

    /**
     * Bắn đạn hướng tới quái vật trong tầm bắn (Sát thương tăng +30% theo từng Level)
     */
    public Projectile fire(Enemy target) {
        if (!canFire() || target == null || !isEnemyInRange(target)) {
            return null;
        }
        resetCooldown();
        float startX = x + width / 2f - 5f;
        float startY = y + height / 2f - 5f;
        Projectile projectile = new Projectile();
        boolean isSlow = (type == TowerType.SLOW);

        // Tính sát thương dựa trên level (Tăng 30% mỗi cấp)
        int damage = (int) (GameConfig.PROJECTILE_DAMAGE * (1.0f + (level - 1) * 0.30f));
        projectile.initialize(startX, startY, target, damage, GameConfig.PROJECTILE_SPEED, isSlow);
        return projectile;
    }

    /**
     * Kiểm tra xem con quái (Enemy) có nằm trong tầm bắn của tháp hay không
     */
    public boolean isEnemyInRange(Enemy enemy) {
        if (enemy == null || !enemy.isActive()) return false;
        
        // Tính khoảng cách giữa tâm tháp và tâm quái
        float towerCenterX = x + width / 2f;
        float towerCenterY = y + height / 2f;
        float enemyCenterX = enemy.getX() + enemy.getWidth() / 2f;
        float enemyCenterY = enemy.getY() + enemy.getHeight() / 2f;

        float dx = towerCenterX - enemyCenterX;
        float dy = towerCenterY - enemyCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return distance <= range;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) return;

        // 1. RENDER HÌNH ẢNH SPIRTE NẾU CÓ
        if (sprite != null) {
            gc.drawImage(sprite, x, y, width, height);
        } else {
            // FALLBACK: Vẽ khối màu đơn giản nếu chưa có ảnh trong thư mục resources
            gc.setFill(type.getColor());
            gc.fillRect(x, y, width, height);
            
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1.5);
            gc.strokeRect(x, y, width, height);
        }

        // 2. VẼ NHÃN CẤP ĐỘ (LEVEL BADGE) PHÍA TRÊN THÁP
        gc.setFill(Color.rgb(15, 23, 42, 0.85));
        gc.fillRoundRect(x + width / 2f - 18, y - 6, 36, 15, 6, 6);
        gc.setStroke(level == MAX_LEVEL ? Color.GOLD : Color.CYAN);
        gc.setLineWidth(1.0);
        gc.strokeRoundRect(x + width / 2f - 18, y - 6, 36, 15, 6, 6);

        gc.setFont(javafx.scene.text.Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 10));
        gc.setFill(level == MAX_LEVEL ? Color.GOLD : Color.WHITE);
        String levelStr = "Lv." + level;
        gc.fillText(levelStr, x + width / 2f - 11, y + 5);

        // 3. DEBUG MODE: Vẽ vòng tròn đỏ thể hiện tầm bắn (Range)
        if (Constants.DEBUG_COLLISION) {
            gc.setStroke(Color.rgb(255, 0, 0, 0.4));
            gc.setLineWidth(1);
            float towerCenterX = x + width / 2f;
            float towerCenterY = y + height / 2f;
            gc.strokeOval(towerCenterX - range, towerCenterY - range, range * 2, range * 2);
        }
    }
}