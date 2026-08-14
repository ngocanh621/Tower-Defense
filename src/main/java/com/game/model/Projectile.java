package com.game.model;

import com.game.util.GameConfig;
import java.io.InputStream;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * đạn
 * kế thừa Entity và cài đặt Poolable
 */
public class Projectile extends Entity implements Poolable {

    private Enemy target;
    private float damage;
    private float speed;
    private boolean isSlow;
    private Image sprite;

    public Projectile() {
        super(0, 0, 10f, 10f);
        this.active = false;
    }

    /**
     * khởi tạo thông số đạn
     */
    public void initialize(float startX, float startY, Enemy target, float damage, float speed, boolean isSlow) {
        this.x = startX;
        this.y = startY;
        this.width = 10f;
        this.height = 10f;
        this.target = target;
        this.damage = damage;
        this.speed = speed;
        this.isSlow = isSlow;
        this.active = true;
        this.sprite = loadSprite();
    }

    public void initialize(float startX, float startY, Enemy target, float damage, float speed) {
        initialize(startX, startY, target, damage, speed, false);
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
        }
        return null;
    }

    @Override
    public void update(double deltaTime) {
        if (!active) {
            return;
        }

        if (target == null || !target.isActive()) {
            active = false;
            return;
        }

        // tính toán khoảng cách tới vị trí mục tiêu
        float bulletCenterX = x + width / 2f;
        float bulletCenterY = y + height / 2f;
        float targetCenterX = target.getX() + target.getWidth() / 2f;
        float targetCenterY = target.getY() + target.getHeight() / 2f;

        float dx = targetCenterX - bulletCenterX;
        float dy = targetCenterY - bulletCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float stepDistance = (float) (speed * deltaTime);

        // nếu đạn chạm tới mục tiêu hoặc va chạm hình hộp
        if (distance <= stepDistance || overlaps(target)) {
            target.takeDamage(damage);
            if (isSlow) {
                // làm chậm quái còn 50% tốc độ trong 2.5 giây
                target.applySlow(0.5f, 2.5f);
            }
            active = false;
        } else {
            // tịnh tiến đạn về phía mục tiêu
            x += (dx / distance) * stepDistance;
            y += (dy / distance) * stepDistance;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!active) {
            return;
        }

        if (sprite != null && !isSlow) {
            gc.drawImage(sprite, x, y, width, height);
        } else {
            // đạn slow màu xanh, đạn thường màu vàng
            gc.setFill(isSlow ? Color.CYAN : Color.YELLOW);
            gc.fillOval(x, y, width, height);
            gc.setStroke(isSlow ? Color.BLUE : Color.ORANGE);
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
