package com.game.model;

/**
 * Base class for all game entities (towers, enemies, projectiles).
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
     * Update entity logic for the current frame.
     */
    public abstract void update(double deltaTime);

    /**
     * Check if this entity is active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set the active state of this entity.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    // Getters and setters
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
     * Get distance to another entity.
     */
    public float distanceTo(Entity other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Check if this entity overlaps with another.
     */
    public boolean overlaps(Entity other) {
        return this.x < other.x + other.width &&
               this.x + this.width > other.x &&
               this.y < other.y + other.height &&
               this.y + this.height > other.y;
    }
}
