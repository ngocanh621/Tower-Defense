package com.game.util;

import java.io.InputStream;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class ExplosionEffect {

    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private Image explosionImage;

    private float displayTimer = 0f;
    private final float MAX_LIFETIME = 0.2f; // Thời gian hiển thị 0.2s
    private boolean active = true;

    // Sửa Constructor nhận đường dẫn ảnh nổ riêng cho từng quái
    public ExplosionEffect(float x, float y, float width, float height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        loadImage(imagePath);
    }

    private void loadImage(String imagePath) {
        try (InputStream is = getClass().getResourceAsStream(imagePath)) {
            if (is != null) {
                this.explosionImage = new Image(is);
            }
        } catch (Exception e) {
            // Bỏ qua lỗi nếu không tìm thấy ảnh
        }
    }

    public void update(double deltaTime) {
        if (!active) return;

        displayTimer += deltaTime;
        if (displayTimer >= MAX_LIFETIME) {
            active = false;
        }
    }

    public void render(GraphicsContext gc) {
        if (!active || explosionImage == null) return;
        gc.drawImage(explosionImage, x, y, width, height);
    }

    public boolean isActive() {
        return active;
    }
}