package com.game.system;

/**
 * Game events that can be published and subscribed to.
 */
public enum GameEvent {
    ENEMY_DIED,
    ENEMY_REACHED_BASE,
    WAVE_STARTED,
    WAVE_COMPLETED,
    TOWER_PLACED,
    TOWER_REMOVED,
    PLAYER_GOLD_CHANGED,
    PLAYER_HP_CHANGED,
    GAME_OVER,
    GAME_WON
}
