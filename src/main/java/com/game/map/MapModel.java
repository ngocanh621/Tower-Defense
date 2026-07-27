package com.game.map;

import com.game.util.GameConfig;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

/**
 * Mô hình bản đồ game khớp chuẩn 100% với con đường vàng trên hình ảnh map.png.
 * Quản lý danh sách ô cờ (grid) và tọa độ các điểm mốc (Waypoints).
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
     * Khởi tạo bản đồ khớp vệt đường vàng trên ảnh map.png (1280x720).
     */
    private void initializeGrid() {
        // 1. Khởi tạo toàn bộ các ô ban đầu là EMPTY (đất trống/cỏ xanh)
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                grid[r][c] = new Cell(r, c, CellType.EMPTY);
            }
        }

        // 2. Danh sách mốc tọa độ chính xác của con đường đất vàng trên map.png
        double[][] rawControlPoints = {
            {0, 300},      // Cổng xuất phát bên trái
            {120, 300},
            {260, 390},    // Khúc uốn cua xuống
            {420, 370},
            {560, 260},
            {630, 190},    // Đỉnh cua phía trên
            {730, 220},
            {770, 360},    // Vòng cua xuống phải
            {680, 520},
            {450, 530},    // Vòng lặp giữa kéo về trái
            {220, 540},
            {150, 620},    // Góc dưới bên trái
            {240, 665},
            {480, 665},    // Đoạn đáy dưới cùng
            {720, 665},
            {840, 630},
            {730, 500},    // Cua ngoặt dốc
            {800, 450},
            {980, 450},    // Đoạn ngang bên phải
            {1150, 410},
            {1180, 310},   // Vòng lượn quanh đầm lầy
            {1080, 230},
            {960, 230},
            {990, 150},    // Lối vào lều xanh
            {1080, 150}    // Đích căn cứ Lều Xanh (BASE)
        };

        // 3. Nội suy mượt mà giữa các điểm mốc (Subdivide/Interpolate)
        pathWaypoints.clear();
        for (int i = 0; i < rawControlPoints.length - 1; i++) {
            double p1x = rawControlPoints[i][0];
            double p1y = rawControlPoints[i][1];
            double p2x = rawControlPoints[i + 1][0];
            double p2y = rawControlPoints[i + 1][1];

            double dist = Math.hypot(p2x - p1x, p2y - p1y);
            int steps = Math.max(1, (int) (dist / 15.0)); // Mỗi nấc cách nhau khoảng 15px để quái đi cực mượt

            for (int s = 0; s < steps; s++) {
                double t = (double) s / steps;
                double ix = p1x + t * (p2x - p1x);
                double iy = p1y + t * (p2y - p1y);
                pathWaypoints.add(new Point2D(ix, iy));
            }
        }
        // Thêm điểm mốc cuối cùng
        double[] last = rawControlPoints[rawControlPoints.length - 1];
        pathWaypoints.add(new Point2D(last[0], last[1]));

        // 4. Đánh dấu các ô nằm trên vệt đường vàng thành CellType.PATH
        double cellSize = GameConfig.GRID_CELL_SIZE;
        for (Point2D wp : pathWaypoints) {
            int col = (int) (wp.getX() / cellSize);
            int row = (int) (wp.getY() / cellSize);

            if (row >= 0 && row < rows && col >= 0 && col < cols) {
                grid[row][col].setType(CellType.PATH);
            }
        }

        // 5. Đánh dấu ô SPAWN và BASE
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
