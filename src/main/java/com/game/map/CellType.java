package com.game.map;

/**
 * định nghĩa các ô grid
 */
public enum CellType {
    EMPTY,      // Ô đất trống (Cho phép người chơi mua và đặt tháp)
    PATH,       // Đường đi dành cho quái vật (Không cho phép đặt tháp)
    OCCUPIED,   // Ô đã được xây dựng tháp phòng thủ
    SPAWN,      // Cổng xuất phát của quái vật 
    BASE        // Căn cứ 
}