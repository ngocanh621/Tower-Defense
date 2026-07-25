package com.game.map;

/**
 * Định nghĩa các loại ô cờ (Grid Cell) trên bản đồ game.
 */
public enum CellType {
    EMPTY,      // Ô đất trống (Cho phép người chơi mua và đặt tháp)
    PATH,       // Đường đi dành cho quái vật (Không cho phép đặt tháp)
    OCCUPIED,   // Ô đã được xây dựng tháp phòng thủ
    SPAWN,      // Cổng xuất phát của quái vật (Enemy Spawn Point)
    BASE        // Căn cứ / Nhà chính của người chơi (Player Base)
}