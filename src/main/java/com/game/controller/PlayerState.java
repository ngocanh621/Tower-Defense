package com.game.controller;

import com.game.system.EventBus;
import com.game.system.GameEvent;
import com.game.util.GameConfig;

/**
 * quản lý tài nguyên của người chơi
 */
public class PlayerState {

    private int health;
    private int gold;
    private int score;
    private final int maxHealth;

    /**
     * lấy máu và vàng ban đầu từ GameConfig
     */
    public PlayerState() {
        this(GameConfig.STARTING_HEALTH, GameConfig.STARTING_GOLD);
    }

    /**
     * thông số ban đầu
     */
    public PlayerState(int initialHealth, int initialGold) {
        this.health = initialHealth;
        this.maxHealth = initialHealth;
        this.gold = initialGold;
        this.score = 0;
    }

    /**
     * trừ máu khi quái vô nhà
     * @param damage số máu bị trừ
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
        // bắn sự kiện thông báo máu đã thay đổi để HUD cập nhật
        EventBus.getInstance().publish(GameEvent.PLAYER_HP_CHANGED, this.health);

        // bắn sự kiện Game Over nếu máu về 0
        if (this.health <= 0) {
            EventBus.getInstance().publish(GameEvent.GAME_OVER);
        }
    }

    /**
     * cộng thêm vàng 
     * @param amount số vàng nhận được
     */
    public void addGold(int amount) {
        this.gold += amount;
        EventBus.getInstance().publish(GameEvent.PLAYER_GOLD_CHANGED, this.gold);
    }

    /**
     * trừ vàng khi mua tháp hoặc nâng cấp
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

    /**
     * cộng điểm
     * @param points điểm thưởng
     */
    public void addScore(int points) {
        this.score += points;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

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
     * kiểm tra người chơi sống k
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