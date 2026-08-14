package com.game.system;

/**
 * danh sách các sự kiện diễn ra trong game
 */
public enum GameEvent {
    ENEMY_DIED,           // quái chết
    ENEMY_REACHED_BASE,   // quái vô nhà
    WAVE_STARTED,         // đợt quái mới
    WAVE_COMPLETED,       // đợt quái kết thúc
    TOWER_PLACED,         // đặt tháp mới
    TOWER_REMOVED,        // bán tháp
    PLAYER_GOLD_CHANGED,  // tiền vàng thay đổi
    PLAYER_HP_CHANGED,    // máu người chơi thay đổi
    GAME_OVER,            // thua
    GAME_WON              // thắng
}
