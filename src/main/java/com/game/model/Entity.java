package com.game.model;

import javafx.scene.canvas.GraphicsContext;

/**
 * Lớp cha trừu tượng (Abstract Base Class) đại diện cho mọi thực thể trong game 
 * tháp, quái, đạn
 */
public abstract class Entity {

    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected boolean active;

    public Entity(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.active = true;
    }

    /**
     * Cập nhật logic thực thể theo thời gian thực.
     * @param deltaTime Khoảng thời gian trôi qua giữa 2 khung hình (tính bằng giây)
     */
    public abstract void update(double deltaTime);

    /**
     * render thực thể
     */
    public abstract void render(GraphicsContext gc);

    /**
     * kiểm tra thực thể có đang active trên màn hình hay k
     */
    public boolean isActive() {
        return active;
    }

    /**
     * thiết lập cờ hoạt động
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * tính khoảng cách Euclid từ tâm thực thể này đến thực thể khác
     * dùng để tính tầm bắn của Tháp
     */
    public float distanceTo(Entity other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * kiểm tra va chạm hình hộp giữa 2 thực thể
     * để kiểm tra đạn trúng quái
     */
    public boolean overlaps(Entity other) {
        return this.x < other.x + other.width &&
               this.x + this.width > other.x &&
               this.y < other.y + other.height &&
               this.y + this.height > other.y;
    }
}