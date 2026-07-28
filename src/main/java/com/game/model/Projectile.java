package com.game.model;

import com.game.util.GameConfig;
import java.io.InputStream;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Đại diện cho viên đạn bắn ra từ Tháp phòng thủ.
 * Kế thừa từ Entity và cài đặt Poolable để quản lý tái sử dụng bộ nhớ.
 */
public class Projectile extends Entity implements Poolable {

    private Enemy target;
    private float damage;
    private float speed;
    private Image sprite;

    /**
     * Constructor mặc định với trạng thái active = false.
     * Thường dùng khi khởi tạo sẵn trong ObjectPool.
     */
    public Projectile() {
        super(0, 0, 10f, 10f);
        this.active = false;
    }

    /**
     * Khởi tạo thông số đạn khi được bắn ra.
     * 
     * @param startX Vị trí xuất phát X
     * @param startY Vị trí xuất phát Y
     * @param target Quái vật mục tiêu
     * @param damage Sát thương gây ra
     * @param speed Tốc độ bay (pixel/giây)
     */
    public void initialize(float startX, float startY, Enemy target, float damage, float speed) {
        this.x = startX;
        this.y = startY;
        this.width = 10f;
        this.height = 10f;
        this.target = target;
        this.damage = damage;
        this.speed = speed;
        this.active = true;
        this.sprite = loadSprite();
    }

    private Image loadSprite() {
        try (InputStream is = getClass().getResourceAsStream("/assets/projectile.png")) {
            if (is != null) {
                Image img = new Image(is);
                if (!img.isError()) {
                    return img;
                }
            }
        } catch (Exception e) {
            // Chuyển sang vẽ khối màu fallback nếu không tải được ảnh
        }
        return null;
    }

    @Override
    public void update(double deltaTime) {
        if (!active) {
            return;
        }

        // Nếu mục tiêu bị hủy hoặc không còn hoạt động, biến mất đạn
        if (target == null || !target.isActive()) {
            active = false;
            return;
        }

        // Tính toán khoảng cách tới vị trí mục tiêu
        float bulletCenterX = x + width / 2f;
        float bulletCenterY = y + height / 2f;
        float targetCenterX = target.getX() + target.getWidth() / 2f;
        float targetCenterY = target.getY() + target.getHeight() / 2f;

        float dx = targetCenterX - bulletCenterX;
        float dy = targetCenterY - bulletCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float stepDistance = (float) (speed * deltaTime);

        // Nếu đạn chạm tới mục tiêu hoặc va chạm hình hộp
        if (distance <= stepDistance || overlaps(target)) {
            target.takeDamage(damage);
            active = false;
        } else {
            // Tịnh tiến đạn về phía mục tiêu
            x += (dx / distance) * stepDistance;
            y += (dy / distance) * stepDistance;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) {
            return;
        }

        if (sprite != null) {
            gc.drawImage(sprite, x, y, width, height);
        } else {
            // Fallback vẽ đạn dạng hình tròn màu vàng
            gc.setFill(Color.YELLOW);
            gc.fillOval(x, y, width, height);
            gc.setStroke(Color.ORANGE);
            gc.setLineWidth(1);
            gc.strokeOval(x, y, width, height);
        }
    }

    @Override
    public void reset() {
        this.target = null;
        this.damage = 0;
        this.speed = 0;
        this.active = false;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.sprite = null;
    }

    public Enemy getTarget() {
        return target;
    }

    public float getDamage() {
        return damage;
    }

    public float getSpeed() {
        return speed;
    }
}
