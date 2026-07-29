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

        public float getRange() { return range; }
        public float getFireRate() { return fireRate; }
        public Color getColor() { return color; }
        public String getImagePath() { return imagePath; }
    }

    private final TowerType type;
    private final float range;
    private final float fireRate;
    private float cooldownTimer; // Bộ đếm thời gian nạp đạn
    private Image sprite;        // Hình ảnh sprite đại diện cho tháp

    /**
     * Khởi tạo Tháp phòng thủ tại tọa độ ô vuông
     * @param gridCol Cột trên bản đồ
     * @param gridRow Hàng trên bản đồ
     * @param type Loại tháp (GUN hoặc SLOW)
     */
    public Tower(int gridCol, int gridRow, TowerType type) {
        // Tính toán tọa độ pixel thực tế ở giữa ô lưới
        super(
            gridCol * GameConfig.GRID_CELL_SIZE + (GameConfig.GRID_CELL_SIZE - GameConfig.GRID_CELL_SIZE * 1.5f) / 2f,
            gridRow * GameConfig.GRID_CELL_SIZE + (GameConfig.GRID_CELL_SIZE - GameConfig.GRID_CELL_SIZE * 1.5f) / 2f,
                GameConfig.GRID_CELL_SIZE * 1.5f,
                GameConfig.GRID_CELL_SIZE * 1.5f
        );
        this.type = type;
        this.range = type.getRange();
        this.fireRate = type.getFireRate();
        this.cooldownTimer = 0f;
        
        loadSprite();
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
     * Bắn đạn hướng tới quái vật trong tầm bắn
     */
    public Projectile fire(Enemy target) {
        if (!canFire() || target == null || !isEnemyInRange(target)) {
            return null;
        }
        resetCooldown();
        float startX = x + width / 2f - 5f;
        float startY = y + height / 2f - 5f;
        Projectile projectile = new Projectile();
        projectile.initialize(startX, startY, target, GameConfig.PROJECTILE_DAMAGE, GameConfig.PROJECTILE_SPEED);
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

        // 2. DEBUG MODE: Vẽ vòng tròn đỏ thể hiện tầm bắn (Range)
        if (Constants.DEBUG_COLLISION) {
            gc.setStroke(Color.rgb(255, 0, 0, 0.4));
            gc.setLineWidth(1);
            float towerCenterX = x + width / 2f;
            float towerCenterY = y + height / 2f;
            gc.strokeOval(towerCenterX - range, towerCenterY - range, range * 2, range * 2);
        }
    }
}