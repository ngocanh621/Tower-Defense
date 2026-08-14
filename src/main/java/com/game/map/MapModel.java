package com.game.map;

import com.game.util.GameConfig;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

/**
 * mô hình bản đồ
 * quản lý danh sách ô grid và tọa độ các điểm mốc Waypoints
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
     * khởi tạo bản đồ
     */
    private void initializeGrid() {
        // khởi tạo toàn bộ các ô ban đầu là PATH (ko cho phép đặt tháp ở bãi cỏ)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell(r, c, CellType.PATH);
            }
        }

        // Danh sách mốc tọa độ
        double[][] rawControlPoints = {
            {0, 210},
            {120, 245},
            {220, 285},
            {350, 300},
            {480, 280},
            {570, 245},
            {630, 225},
            {700, 240},
            {730, 270},
            {730, 350},
            {680, 380},
            {615, 400},
            {500, 405},
            {350, 400},
            {230, 420},
            {175, 465},
            {170, 520},
            {170, 570},
            {215, 600},
            {290, 590},
            {375, 575},
            {550, 550},
            {745, 610},
            {830, 630},
            {870, 545},
            {780, 470},
            {800, 430},
            {905, 405},
            {1045, 445},
            {1150, 410},
            {1150, 345},
            {970, 300},
            {930, 250},
            {960, 200},
            {1055, 145}
        };

        // nội suy mượt đường quái đi
        pathWaypoints.clear();
        for (int i = 0; i < rawControlPoints.length - 1; i++) {
            double p1x = rawControlPoints[i][0];
            double p1y = rawControlPoints[i][1];
            double p2x = rawControlPoints[i + 1][0];
            double p2y = rawControlPoints[i + 1][1];

            double dist = Math.hypot(p2x - p1x, p2y - p1y);
            int steps = Math.max(1, (int) (dist / 15.0)); 

            for (int s = 0; s < steps; s++) {
                double t = (double) s / steps;
                double ix = p1x + t * (p2x - p1x);
                double iy = p1y + t * (p2y - p1y);
                pathWaypoints.add(new Point2D(ix, iy));
            }
        }
        // thêm điểm cuối cùng
        double[] last = rawControlPoints[rawControlPoints.length - 1];
        pathWaypoints.add(new Point2D(last[0], last[1]));

        // đánh dấu các ô nằm trên con đường di chuyển của quái thành CellType.PATH
        double cellSize = GameConfig.GRID_CELL_SIZE;
        for (Point2D wp : pathWaypoints) {
            int col = (int) (wp.getX() / cellSize);
            int row = (int) (wp.getY() / cellSize);

            if (row >= 0 && row < rows && col >= 0 && col < cols) {
                grid[row][col].setType(CellType.PATH);
            }
        }

        // đánh dấu ô SPAWN và BASE
        Point2D spawnWp = pathWaypoints.get(0);
        Point2D baseWp = pathWaypoints.get(pathWaypoints.size() - 1);

        int spawnCol = (int) (spawnWp.getX() / cellSize);
        int spawnRow = (int) (spawnWp.getY() / cellSize);
        int baseCol = (int) (baseWp.getX() / cellSize);
        int baseRow = (int) (baseWp.getY() / cellSize);

        if (spawnRow >= 0 && spawnRow < rows && spawnCol >= 0 && spawnCol < cols) {
            grid[spawnRow][spawnCol].setType(CellType.SPAWN);
        }

        if (baseRow >= 0 && baseRow < rows && baseCol >= 0 && baseCol < cols) {
            grid[baseRow][baseCol].setType(CellType.BASE);
        }

        // danh sách ô được đặt tháp 
        int[][] yellowBuildPatches = {
            {8, 5},
            {7, 15},
            {15, 14},
            {11, 16},
            {12, 7},
            {11, 22},
            {9, 26}
        };

        for (int[] cellPos : yellowBuildPatches) {
            int r = cellPos[0];
            int c = cellPos[1];
            if (r >= 0 && r < rows && c >= 0 && c < cols) {
                grid[r][c].setType(CellType.EMPTY); // ô vàng này là ô cho phép đặt tháp 
            }
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
