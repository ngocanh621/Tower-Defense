package com.game.map;

/**
 * Đại diện cho một ô đơn lẻ trên lưới bản đồ game (Grid Cell).
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

    // Getters và Setters
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
     * Kiểm tra xem tháp phòng thủ có thể đặt lên ô này được hay không.
     * @return true nếu ô đang là đất trống (EMPTY)
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