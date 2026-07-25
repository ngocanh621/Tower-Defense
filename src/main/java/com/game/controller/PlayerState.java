package com.game.controller;

import com.game.system.EventBus;
import com.game.system.GameEvent;
import com.game.util.GameConfig;

/**
 * Quản lý tài nguyên của người chơi bao gồm Máu (Health) và Tiền (Gold).
 */
public class PlayerState {

    private int health;
    private int gold;
    private final int maxHealth;

    /**
     * Constructor mặc định: Tự động lấy Máu và Vàng ban đầu từ GameConfig.
     */
    public PlayerState() {
        this(GameConfig.STARTING_HEALTH, GameConfig.STARTING_GOLD);
    }

    /**
     * Constructor tùy chỉnh thông số ban đầu.
     */
    public PlayerState(int initialHealth, int initialGold) {
        this.health = initialHealth;
        this.maxHealth = initialHealth;
        this.gold = initialGold;
    }

    /**
     * Trừ máu người chơi khi quái xâm nhập căn cứ.
     * @param damage Số lượng máu bị trừ
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
        // Bắn sự kiện thông báo máu đã thay đổi để Giao diện (HUD) tự cập nhật
        EventBus.getInstance().publish(GameEvent.PLAYER_HP_CHANGED, this.health);

        // Bắn sự kiện Game Over nếu máu về 0
        if (this.health <= 0) {
            EventBus.getInstance().publish(GameEvent.GAME_OVER);
        }
    }

    /**
     * Cộng thêm vàng (ví dụ: khi tiêu diệt quái vật).
     * @param amount Số vàng nhận được
     */
    public void addGold(int amount) {
        this.gold += amount;
        EventBus.getInstance().publish(GameEvent.PLAYER_GOLD_CHANGED, this.gold);
    }

    /**
     * Trừ vàng khi mua tháp phòng thủ hoặc nâng cấp.
     * @param amount Số vàng cần chi trả
     * @return true nếu đủ tiền giao dịch, false nếu không đủ tiền
     */
    public boolean spendGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            EventBus.getInstance().publish(GameEvent.PLAYER_GOLD_CHANGED, this.gold);
            return true;
        }
        return false;
    }

    // --- GETTERS & SETTERS ---

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getGold() {
        return gold;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Kiểm tra người chơi còn sống hay không.
     */
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public String toString() {
        return "PlayerState{" +
                "health=" + health +
                ", gold=" + gold +
                '}';
    }
}