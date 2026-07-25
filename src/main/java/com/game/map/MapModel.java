package com.game.map;

import com.game.util.GameConfig;

/**
 * Mô hình bản đồ đơn giản cho màn chơi.
 * Cung cấp lưới ô và các loại ô để GameScene vẽ và xử lý logic.
 */
public class MapModel {

    private final int rows;
    private final int cols;
    private final Cell[][] grid;

    public MapModel() {
        this.rows = GameConfig.GRID_HEIGHT;
        this.cols = GameConfig.GRID_WIDTH;
        this.grid = new Cell[rows][cols];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell(r, c, CellType.EMPTY);
            }
        }

        // Tạo một đường đi đơn giản từ trái sang phải ở giữa màn hình.
        int pathRow = rows / 2;
        for (int c = 0; c < cols; c++) {
            grid[pathRow][c].setType(CellType.PATH);
        }

        grid[pathRow][0].setType(CellType.SPAWN);
        grid[pathRow][cols - 1].setType(CellType.BASE);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return null;
        }
        return grid[row][col];
    }

    public void setCellType(int row, int col, CellType type) {
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            grid[row][col].setType(type);
        }
    }
}
