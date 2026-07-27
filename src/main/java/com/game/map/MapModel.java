package com.game.map;

import com.game.util.GameConfig;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

/**
 * Mô hình quản lý bản đồ trò chơi.
 * Xây dựng danh sách đường đi ô cờ (PATH) uốn lượn và danh sách điểm mốc (Waypoints).
 */
public class MapModel {

    private final int rows;
    private final int cols;
    private final Cell[][] grid;
    private final List<Point2D> pathWaypoints = new ArrayList<>();

    public MapModel() {
        this.rows = GameConfig.GRID_HEIGHT;
        this.cols = GameConfig.GRID_WIDTH;
        this.grid = new Cell[rows][cols];
        initializeGrid();
    }

    /**
     * Khởi tạo lưới ô cờ và đường đi uốn lượn (Waypoints).
     */
    private void initializeGrid() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell(r, c, CellType.EMPTY);
            }
        }

        // Danh sách các điểm mốc uốn lượn (Hàng, Cột)
        int[][] waypointsGrid = {
            {4, 0},   // SPAWN
            {4, 8},   // Khúc rẽ 1
            {13, 8},  // Khúc rẽ 2
            {13, 18}, // Khúc rẽ 3
            {5, 18},  // Khúc rẽ 4
            {5, 26},  // Khúc rẽ 5
            {11, 26}, // Khúc rẽ 6
            {11, cols - 1} // BASE
        };

        pathWaypoints.clear();
        double cellSize = GameConfig.GRID_CELL_SIZE;

        // Nối các điểm mốc và đánh dấu loại ô PATH trên lưới
        for (int i = 0; i < waypointsGrid.length - 1; i++) {
            int startRow = waypointsGrid[i][0];
            int startCol = waypointsGrid[i][1];
            int endRow = waypointsGrid[i + 1][0];
            int endCol = waypointsGrid[i + 1][1];

            int r = startRow;
            int c = startCol;

            int dr = Integer.compare(endRow, startRow);
            int dc = Integer.compare(endCol, startCol);

            while (true) {
                grid[r][c].setType(CellType.PATH);
                if (r == endRow && c == endCol) {
                    break;
                }
                r += dr;
                c += dc;
            }
        }

        // Đặt ô xuất phát (SPAWN) và nhà chính (BASE)
        int[] spawn = waypointsGrid[0];
        int[] base = waypointsGrid[waypointsGrid.length - 1];

        grid[spawn[0]][spawn[1]].setType(CellType.SPAWN);
        grid[base[0]][base[1]].setType(CellType.BASE);

        // Chuyển đổi danh sách điểm mốc sang tọa độ Pixel (tâm ô vuông)
        for (int[] wp : waypointsGrid) {
            double px = wp[1] * cellSize + cellSize / 2.0;
            double py = wp[0] * cellSize + cellSize / 2.0;
            pathWaypoints.add(new Point2D(px, py));
        }
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

    public List<Point2D> getPathWaypoints() {
        return pathWaypoints;
    }
}
