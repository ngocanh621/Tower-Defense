package com.game.model;

import com.game.map.Cell;
import com.game.map.CellType;
import com.game.map.MapModel;
import com.game.system.EventBus;
import com.game.system.GameEvent;
import com.game.util.GameConfig;
import com.game.util.Animation;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


/**
 * quái
 * kế thừa từ Entity và cài đặt Poolable
 * quái di chuyển từ SPAWN tới BASE và có thể nhận sát thương từ tháp
 */
public class Enemy extends Entity implements Poolable {

    private EnemyType type;        // loại quái
    private float hp;              // máu hiện tại
    private float maxHp;           // máu tối đa
    private float speed;           // tốc độ di chuyển 
    private int reward;            // số tiền vàng thưởng cho người chơi khi hạ gục
    private boolean reachedBase;   // cờ đánh dấu quái đã chạm nhà chính hay chưa
    private Animation anim;        // animation

    private final List<Point2D> waypoints = new ArrayList<>(); // danh sách các điểm mốc trên đường đi
    private int currentWaypointIndex = 0;                      // chỉ số điểm mốc mục tiêu hiện tại

    /**
     * mặc định active = false.
     */
    public Enemy() {
        super(0, 0, 0, 0);
        this.active = false; // quái trong kho
    }

    private float slowTimer = 0f;       // thời gian hiệu ứng slow còn lại 
    private float slowFactor = 1.0f;     // hệ số làm chậm (0.5f giảm 50% tốc độ)

    /**
     * khởi tạo các thuộc tính và vị trí xuất hiện của quái trên bản đồ
     */
    public void initialize(EnemyType type, MapModel mapModel) {
        this.type = type;
        this.maxHp = type.getHp();    
        this.hp = maxHp;
        this.speed = type.getSpeed(); 
        this.reward = type.getReward(); 
        this.reachedBase = false;
        this.slowTimer = 0f;
        this.slowFactor = 1.0f;

        Image[] frames = loadSprite(type); 
        this.anim = new Animation(frames, 0.15);

        // tính kích thước quái
        float scale = (type == EnemyType.DRAGON) ? 1.4f : 1.0f;
        float size = GameConfig.GRID_CELL_SIZE * scale;
        this.width = size;
        this.height = size;

        // danh sách các điểm mốc đường đi
        waypoints.clear();
        if (mapModel != null && mapModel.getPathWaypoints() != null) {
            waypoints.addAll(mapModel.getPathWaypoints());
        }

        this.currentWaypointIndex = 0;

        // vị trí xuất phát tại điểm mốc đầu tiên (SPAWN)
        if (!waypoints.isEmpty()) {
            Point2D startWp = waypoints.get(0);
            this.x = (float) startWp.getX() - size / 2f;
            this.y = (float) startWp.getY() - size / 2f;
            this.currentWaypointIndex = 1; // hướng tới điểm mốc thứ 2
        }

        this.active = true; // bật cờ cho phép quái hoạt động và render
    }

    /**
     * hiệu ứng làm chậm quái
     * @param factor tỷ lệ tốc độ còn lại (0.5f giảm 50% tốc độ)
     * @param duration thời gian làm chậm (giây)
     */
    public void applySlow(float factor, float duration) {
        this.slowFactor = factor;
        this.slowTimer = duration;
    }

    /**
     * cập nhật logic di chuyển của quái theo từng khung hình
     */
    @Override
    public void update(double deltaTime) {
        if (!active || reachedBase) {
            return; // nếu quái chưa được kích hoạt hoặc đã chạm đích thì bỏ qua
        }

        // cập nhật đếm ngược hiệu ứng slow
        if (slowTimer > 0) {
            slowTimer -= deltaTime;
            if (slowTimer <= 0) {
                slowTimer = 0;
                slowFactor = 1.0f; // hết slow, khôi phục tốc độ bình thường
            }
        }

        // cập nhật frames animation
        if (anim != null) {
            anim.update(deltaTime);
        }

        if (waypoints.isEmpty() || currentWaypointIndex >= waypoints.size()) {
            reachBase();
            return;
        }

        // tính tốc độ thực tế (đã áp dụng slowFactor)
        float currentSpeed = speed * slowFactor;
        float remainingStep = (float) (currentSpeed * deltaTime);

        // tịnh tiến mượt qua nhiều điểm mốc
        while (remainingStep > 0 && currentWaypointIndex < waypoints.size()) {
            float centerX = x + width / 2f;
            float centerY = y + height / 2f;

            Point2D targetWp = waypoints.get(currentWaypointIndex);
            float targetX = (float) targetWp.getX();
            float targetY = (float) targetWp.getY();

            float dx = targetX - centerX;
            float dy = targetY - centerY;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance <= remainingStep) {
                // đã chạm hoặc vượt mốc, cập nhật tọa độ chính xác mốc này và chuyển sang mốc kế tiếp
                this.x = targetX - width / 2f;
                this.y = targetY - height / 2f;
                remainingStep -= distance;
                currentWaypointIndex++;
                if (currentWaypointIndex >= waypoints.size()) {
                    reachBase(); // đã chạm căn cứ lều xanh
                    return;
                }
            } else {
                // tịnh tiến vị trí quái theo hướng mốc hiện tại
                this.x += (dx / distance) * remainingStep;
                this.y += (dy / distance) * remainingStep;
                remainingStep = 0;
            }
        }
    }

    /**
     * render
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!active) {
            return; 
        }

        Image currentImg = (anim != null) ? anim.getCurrentFrame() : null; 

        if (currentImg != null && !currentImg.isError()) {
            gc.drawImage(currentImg, x, y, width, height);
        }

        // vẽ hiệu ứng đóng băng lên quái khi bị Slow
        if (slowTimer > 0) {
            gc.setFill(Color.rgb(56, 189, 248, 0.4));
            gc.fillOval(x - 2, y - 2, width + 4, height + 4);
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(1.5);
            gc.strokeOval(x - 2, y - 2, width + 4, height + 4);
        }

        // vẽ thanh máu
        renderHealthBar(gc);
    }

    /**
     * vẽ thanh máu
     */
    private void renderHealthBar(GraphicsContext gc) {
        double barWidth = width;
        double barHeight = 5;
        double barX = x;
        double barY = y - 8; 
        
        // tỷ lệ máu còn lại
        double healthRatio = Math.max(0, hp / maxHp);

        // vẽ nền màu đen xám mờ cho thanh máu
        gc.setFill(Color.web("#000000", 0.6));
        gc.fillRect(barX, barY, barWidth, barHeight);

        // vẽ lượng máu hiện tại bằng màu xanh lá co rút theo tỷ lệ hp/maxHp
        gc.setFill(Color.LIME);
        gc.fillRect(barX, barY, barWidth * healthRatio, barHeight);

        // vẽ khung viền trắng bao quanh thanh máu
        gc.setStroke(Color.WHITE);
        gc.strokeRect(barX, barY, barWidth, barHeight);
    }

    /**
     * trừ máu quái khi bị đạn trúng
     * @param amount lượng sát thương nhận vào
     */
    public void takeDamage(float amount) {
        if (!active) {
            return;
        }

        hp -= amount; 
        if (hp <= 0) {
            die(); 
        }
    }

    /**
     * xử lý khi quái hết máu
     */
    private void die() {
        active = false; 
        // phát sự kiện ENEMY_DIED qua EventBus để PlayerState cộng vàng
        EventBus.getInstance().publish(GameEvent.ENEMY_DIED, this);
    }

    /**
     * xử lý khi quái chạm được vào nhà chính
     */
    private void reachBase() {
        active = false;
        reachedBase = true;
        // phát sự kiện ENEMY_REACHED_BASE để PlayerState trừ máu người chơi
        EventBus.getInstance().publish(GameEvent.ENEMY_REACHED_BASE, this);
    }

    public boolean isReachedBase() { return reachedBase; }
    public float getHp() { return hp; }
    public float getMaxHp() { return maxHp; }
    public int getReward() { return reward; }
    public EnemyType getType() { return type; }
    public String getExplosionImagePath() {
        return (type != null) ? type.getExplosionImagePath() : null;
    }

    /**
     * reset
     */
    @Override
    public void reset() {
        this.type = null;
        this.hp = 0;
        this.maxHp = 0;
        this.speed = 0;
        this.reward = 0;
        this.reachedBase = false;
        if (anim != null) {
            anim.reset();
        }
        this.anim = null;
        this.active = false;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.waypoints.clear();
        this.currentWaypointIndex = 0;
    }

    private Image[] loadSprite(EnemyType type) {
        if (type == null) {
            return null;
        }

        Image[] frames = new Image[4];
        String prefix = type.getSpritePrefix();

        for (int i = 1; i <= 4; i++) {
            String path = prefix + i + ".png";
            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is != null) {
                    frames[i - 1] = new Image(is); // i=1 -> frames[0]
                }
            } catch (Exception e) {
            }
        }
        return frames;
    }

    private Cell findCell(MapModel mapModel, CellType type) {
        for (int r = 0; r < mapModel.getRows(); r++) {
            for (int c = 0; c < mapModel.getCols(); c++) {
                Cell cell = mapModel.getCell(r, c);
                if (cell != null && cell.getType() == type) {
                    return cell;
                }
            }
        }
        return null;
    }

    private Color getColorForType() {
        if (type == null) {
            return Color.GRAY;
        }
        return switch (type) {
            case GOBLIN -> Color.FORESTGREEN;
            case ORC -> Color.DARKGREEN;
            case DRAGON -> Color.DARKRED;
        };
    }

    public int getPlayerDamage() {
        return (type != null) ? type.getPlayerDamage() : 1;
    }

    public enum EnemyType {
        GOBLIN(GameConfig.ENEMY_GOBLIN_HP, GameConfig.ENEMY_GOBLIN_SPEED, GameConfig.ENEMY_GOBLIN_REWARD, 1, "/assets/quai1.", "/assets/explosion_quai1.png"),
        ORC(GameConfig.ENEMY_ORC_HP, GameConfig.ENEMY_ORC_SPEED, GameConfig.ENEMY_ORC_REWARD, 2, "/assets/quai2.", "/assets/explosion_quai2.png"),
        DRAGON(GameConfig.ENEMY_DRAGON_HP, GameConfig.ENEMY_DRAGON_SPEED, GameConfig.ENEMY_DRAGON_REWARD, 5, "/assets/quaiVua", "/assets/explosion_quaiVua.png");

        private final float hp;
        private final float speed;
        private final int reward;
        private final int playerDamage;
        private final String spritePrefix;
        private final String explosionImagePath;

        EnemyType(float hp, float speed, int reward, int playerDamage, String spritePrefix, String explosionImagePath) {
            this.hp = hp;
            this.speed = speed;
            this.reward = reward;
            this.playerDamage = playerDamage;
            this.spritePrefix = spritePrefix;
            this.explosionImagePath = explosionImagePath;
        }

        public float getHp() {
            return hp;
        }

        public float getSpeed() {
            return speed;
        }

        public int getReward() {
            return reward;
        }

        public int getPlayerDamage() {
            return playerDamage;
        }

        public String getSpritePrefix() {
            return spritePrefix;
        }

        public String getExplosionImagePath() {
            return explosionImagePath;
        }
    }
}
