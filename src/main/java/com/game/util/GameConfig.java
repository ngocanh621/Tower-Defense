package com.game.util;

/**
 * Game configuration constants.
 */
public class GameConfig {

    // Window settings
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final String WINDOW_TITLE = "Tower Defense 2D MVP";

    // Game settings
    public static final double TARGET_FPS = 60.0;
    public static final double FRAME_TIME = 1.0 / TARGET_FPS;

    // Grid settings
    public static final int GRID_CELL_SIZE = 40;
    public static final int GRID_WIDTH = WINDOW_WIDTH / GRID_CELL_SIZE;
    public static final int GRID_HEIGHT = WINDOW_HEIGHT / GRID_CELL_SIZE;

    // Player settings
    public static final int STARTING_HEALTH = 20;
    public static final int STARTING_GOLD = 100;

    // Tower settings
    public static final float TOWER_GUN_COST = 100f;
    public static final float TOWER_SLOW_COST = 150f;

    public static final float TOWER_GUN_RANGE = 150f;
    public static final float TOWER_GUN_FIRE_RATE = 1.0f; // shots per second

    public static final float TOWER_SLOW_RANGE = 200f;
    public static final float TOWER_SLOW_FIRE_RATE = 0.5f;

    // Enemy settings
    public static final float ENEMY_GOBLIN_HP = 50f;
    public static final float ENEMY_GOBLIN_SPEED = 120f;
    public static final int ENEMY_GOBLIN_REWARD = 10;

    public static final float ENEMY_ORC_HP = 200f;
    public static final float ENEMY_ORC_SPEED = 60f;
    public static final int ENEMY_ORC_REWARD = 25;

    public static final float ENEMY_DRAGON_HP = 500f;
    public static final float ENEMY_DRAGON_SPEED = 40f;
    public static final int ENEMY_DRAGON_REWARD = 60;

    // Projectile settings
    public static final float PROJECTILE_SPEED = 500f;
    public static final int PROJECTILE_DAMAGE = 10;

    // Wave settings
    public static final float WAVE_SPAWN_INTERVAL = 0.5f; // seconds between enemy spawns
    public static final float WAVE_DELAY = 10f; // seconds between waves

    private GameConfig() {
        // Private constructor to prevent instantiation
    }
}
