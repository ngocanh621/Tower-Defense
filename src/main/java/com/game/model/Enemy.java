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
 * Đại diện cho 1 enemy.
 * Kế thừa từ Entity (quản lý tọa độ, kích thước, va chạm) và cài đặt Poolable (tái chế bộ nhớ).
 * Quái di chuyển từ ô SPAWN tới ô BASE và có thể nhận sát thương từ tháp phòng thủ.
 */
public class Enemy extends Entity implements Poolable {

    private EnemyType type;        // Loại quái (GOBLIN, ORC, DRAGON)
    private float hp;              // Lượng máu hiện tại
    private float maxHp;           // Lượng máu tối đa
    private float speed;           // Tốc độ di chuyển (pixel/giây)
    private int reward;            // Số tiền vàng thưởng cho người chơi khi hạ gục
    private boolean reachedBase;   // Cờ đánh dấu quái đã chạm nhà chính hay chưa
    private Animation anim;        // Khai báo thuộc tính animation

    private final List<Point2D> waypoints = new ArrayList<>(); // Danh sách các điểm mốc trên đường đi
    private int currentWaypointIndex = 0;                      // Chỉ số điểm mốc mục tiêu hiện tại

    /**
     * Constructor mặc định khóa trạng thái active = false.
     * Được dùng khi khởi tạo sẵn trong ObjectPool (Kho tái chế).
     */
    public Enemy() {
        super(0, 0, 0, 0);
        this.active = false; // Mặc định quái "ngủ đông" trong kho
    }

    private float slowTimer = 0f;       // Thời gian hiệu ứng slow còn lại (giây)
    private float slowFactor = 1.0f;     // Hệ số làm chậm (ví dụ 0.5f nghĩa là giảm 50% tốc độ)

    /**
     * Khởi tạo các thuộc tính và vị trí xuất hiện của quái trên bản đồ.
     */
    public void initialize(EnemyType type, MapModel mapModel) {
        this.type = type;
        this.maxHp = type.getHp();    // Lấy HP gốc từ cấu hình
        this.hp = maxHp;
        this.speed = type.getSpeed(); // Lấy tốc độ từ cấu hình
        this.reward = type.getReward(); // Lấy phần thưởng vàng từ cấu hình
        this.reachedBase = false;
        this.slowTimer = 0f;
        this.slowFactor = 1.0f;

        Image[] frames = loadSprite(type); // Khởi tạo animation với 4 frames ảnh
        this.anim = new Animation(frames, 0.15);

        // Tính toán kích thước quái
        float scale = (type == EnemyType.DRAGON) ? 1.4f : 1.0f;
        float size = GameConfig.GRID_CELL_SIZE * scale;
        this.width = size;
        this.height = size;

        // Lấy danh sách các điểm mốc đường đi (Waypoints) từ MapModel
        waypoints.clear();
        if (mapModel != null && mapModel.getPathWaypoints() != null) {
            waypoints.addAll(mapModel.getPathWaypoints());
        }

        this.currentWaypointIndex = 0;

        // Đặt vị trí xuất phát ban đầu tại điểm mốc đầu tiên (SPAWN)
        if (!waypoints.isEmpty()) {
            Point2D startWp = waypoints.get(0);
            this.x = (float) startWp.getX() - size / 2f;
            this.y = (float) startWp.getY() - size / 2f;
            this.currentWaypointIndex = 1; // Hướng tới điểm mốc thứ 2
        }

        this.active = true; // Bật cờ cho phép quái hoạt động và vẽ lên màn hình
    }

    /**
     * Gây hiệu ứng làm chậm lên quái
     * @param factor Tỷ lệ tốc độ còn lại (vd: 0.5f nghĩa là còn 50% tốc độ)
     * @param duration Thời gian làm chậm (giây)
     */
    public void applySlow(float factor, float duration) {
        this.slowFactor = factor;
        this.slowTimer = duration;
    }

    /**
     * Cập nhật logic di chuyển của quái theo từng khung hình (Update loop).
     */
    @Override
    public void update(double deltaTime) {
        if (!active || reachedBase) {
            return; // Nếu quái chưa được kích hoạt hoặc đã chạm đích thì bỏ qua
        }

        // Cập nhật đếm ngược hiệu ứng slow
        if (slowTimer > 0) {
            slowTimer -= deltaTime;
            if (slowTimer <= 0) {
                slowTimer = 0;
                slowFactor = 1.0f; // Hết slow, khôi phục tốc độ bình thường
            }
        }

        // Cập nhập frames animation
        if (anim != null) {
            anim.update(deltaTime);
        }

        if (waypoints.isEmpty() || currentWaypointIndex >= waypoints.size()) {
            reachBase();
            return;
        }

        // Tính tốc độ thực tế (đã áp dụng hệ số làm chậm slowFactor)
        float currentSpeed = speed * slowFactor;
        float remainingStep = (float) (currentSpeed * deltaTime);

        // Vòng lặp tịnh tiến mượt qua nhiều điểm mốc nếu tốc độ cao
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
                // Đã chạm hoặc vượt mốc, cập nhật tọa độ chính xác mốc này và chuyển sang mốc kế tiếp
                this.x = targetX - width / 2f;
                this.y = targetY - height / 2f;
                remainingStep -= distance;
                currentWaypointIndex++;
                if (currentWaypointIndex >= waypoints.size()) {
                    reachBase(); // Đã chạm căn cứ lều xanh
                    return;
                }
            } else {
                // Tịnh tiến vị trí quái theo hướng mốc hiện tại
                this.x += (dx / distance) * remainingStep;
                this.y += (dy / distance) * remainingStep;
                remainingStep = 0;
            }
        }
    }

    /**
     * Vẽ hình ảnh con quái lên màn hình Canvas (Render loop).
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!active) {
            return; // Ẩn quái nếu trạng thái không hoạt động
        }

        Image currentImg = (anim != null) ? anim.getCurrentFrame() : null; // Lấy Frames ảnh từ animation

        if (currentImg != null && !currentImg.isError()) {
            gc.drawImage(currentImg, x, y, width, height);
        } else {
            // Tô màu và vẽ hình tròn đại diện cho quái nếu không có ảnh
            gc.setFill(getColorForType());
            gc.fillOval(x, y, width, height);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1);
            gc.strokeOval(x, y, width, height);
        }

        // Vẽ hiệu ứng đóng băng / làm chậm màu xanh dương mờ lên quái khi bị Slow
        if (slowTimer > 0) {
            gc.setFill(Color.rgb(56, 189, 248, 0.4));
            gc.fillOval(x - 2, y - 2, width + 4, height + 4);
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(1.5);
            gc.strokeOval(x - 2, y - 2, width + 4, height + 4);
        }

        // Vẽ thanh máu ngay trên đầu quái
        renderHealthBar(gc);
    }

    /**
     * Phương thức phụ trách vẽ thanh máu (Health Bar) linh hoạt trên đầu quái.
     */
    private void renderHealthBar(GraphicsContext gc) {
        double barWidth = width;
        double barHeight = 5;
        double barX = x;
        double barY = y - 8; // Đặt thanh máu nhích lên trên đầu quái 8px
        
        // Tỷ lệ máu còn lại (từ 0.0 đến 1.0)
        double healthRatio = Math.max(0, hp / maxHp);

        // 1. Vẽ nền màu đen xám mờ cho thanh máu
        gc.setFill(Color.web("#000000", 0.6));
        gc.fillRect(barX, barY, barWidth, barHeight);

        // 2. Vẽ lượng máu hiện tại bằng màu xanh lá (LIME) co rút theo tỷ lệ hp/maxHp
        gc.setFill(Color.LIME);
        gc.fillRect(barX, barY, barWidth * healthRatio, barHeight);

        // 3. Vẽ khung viền trắng bao quanh thanh máu
        gc.setStroke(Color.WHITE);
        gc.strokeRect(barX, barY, barWidth, barHeight);
    }

    /**
     * Trừ máu quái khi bị đạn trúng.
     * * @param amount Lượng sát thương nhận vào
     */
    public void takeDamage(float amount) {
        if (!active) {
            return;
        }

        hp -= amount; // Trừ máu
        if (hp <= 0) {
            die(); // Quái chết nếu máu <= 0
        }
    }

    /**
     * Xử lý khi quái hết máu (Bị tiêu diệt).
     */
    private void die() {
        active = false; // Tắt trạng thái hoạt động để cất lại vào ObjectPool
        // Phát sự kiện ENEMY_DIED qua EventBus để PlayerState cộng vàng
        EventBus.getInstance().publish(GameEvent.ENEMY_DIED, this);
    }

    /**
     * Xử lý khi quái chạm được vào nhà chính (BASE).
     */
    private void reachBase() {
        active = false;
        reachedBase = true;
        // Phát sự kiện ENEMY_REACHED_BASE để PlayerState trừ máu người chơi
        EventBus.getInstance().publish(GameEvent.ENEMY_REACHED_BASE, this);
    }

    // === CÁC HÀM GETTER / SETTER ===
    public boolean isReachedBase() { return reachedBase; }
    public float getHp() { return hp; }
    public float getMaxHp() { return maxHp; }
    public int getReward() { return reward; }
    public EnemyType getType() { return type; }

    /**
     * Tẩy sạch thông số cũ để đưa đối tượng về trạng thái mới tinh trước khi tái sử dụng (Interface Poolable).
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
                System.err.println("Không thể load sprite enemy: " + e.getMessage());
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

    public enum EnemyType {
        GOBLIN(GameConfig.ENEMY_GOBLIN_HP, GameConfig.ENEMY_GOBLIN_SPEED, GameConfig.ENEMY_GOBLIN_REWARD, "/assets/quai1."),
        ORC(GameConfig.ENEMY_ORC_HP, GameConfig.ENEMY_ORC_SPEED, GameConfig.ENEMY_ORC_REWARD, "/assets/quai2."),
        DRAGON(GameConfig.ENEMY_DRAGON_HP, GameConfig.ENEMY_DRAGON_SPEED, GameConfig.ENEMY_DRAGON_REWARD, "/assets/Rong");

        private final float hp;
        private final float speed;
        private final int reward;
        private final String spritePrefix;

        EnemyType(float hp, float speed, int reward, String spritePrefix) {
            this.hp = hp;
            this.speed = speed;
            this.reward = reward;
            this.spritePrefix = spritePrefix;
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

        public String getSpritePrefix() {
            return spritePrefix;
        }
    }
}
