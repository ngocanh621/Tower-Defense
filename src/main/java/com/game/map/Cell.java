package com.game.map;

/**
 * Represents a single cell in the game grid.
 */
public class Cell {

    private final int row;
    private final int col;
    private CellType type;

    public Cell(int row, int col, CellType type) {
        this.row = row;
        this.col = col;
        this.type = type;
    }

    // Getters and setters
    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    /**
     * Check if towers can be placed on this cell.
     */
    public boolean canPlaceTower() {
        return type == CellType.EMPTY;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", col=" + col +
                ", type=" + type +
                '}';
    }
}
