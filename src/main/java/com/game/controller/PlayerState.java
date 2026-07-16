package com.game.controller;

import com.game.system.EventBus;
import com.game.system.GameEvent;

/**
 * Manages player state including health and gold.
 */
public class PlayerState {

    private int health;
    private int gold;
    private final int maxHealth;

    public PlayerState(int initialHealth, int initialGold) {
        this.health = initialHealth;
        this.maxHealth = initialHealth;
        this.gold = initialGold;
    }

    /**
     * Take damage to the player.
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) {
            this.health = 0;
        }
        EventBus.getInstance().publish(GameEvent.PLAYER_HP_CHANGED, this.health);

        if (this.health <= 0) {
            EventBus.getInstance().publish(GameEvent.GAME_OVER);
        }
    }

    /**
     * Add gold to the player.
     */
    public void addGold(int amount) {
        this.gold += amount;
        EventBus.getInstance().publish(GameEvent.PLAYER_GOLD_CHANGED, this.gold);
    }

    /**
     * Spend gold (e.g., buying a tower).
     */
    public boolean spendGold(int amount) {
        if (this.gold >= amount) {
            this.gold -= amount;
            EventBus.getInstance().publish(GameEvent.PLAYER_GOLD_CHANGED, this.gold);
            return true;
        }
        return false;
    }

    // Getters and setters
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
     * Check if the player is alive.
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
