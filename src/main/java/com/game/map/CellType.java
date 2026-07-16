package com.game.map;

/**
 * Represents the type of a grid cell.
 */
public enum CellType {
    EMPTY,      // Empty cell where towers can be placed
    PATH,       // Part of the enemy path
    OCCUPIED,   // Occupied by a tower
    SPAWN,      // Enemy spawn point
    BASE        // Player base
}
