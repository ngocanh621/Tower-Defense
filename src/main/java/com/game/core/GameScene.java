
package com.game.core;

import com.game.util.Constants;
import com.game.util.GameConfig;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


/**
 * Màn chơi chính
 * Dùng để cập nhật game và vẽ game
 */
public class GameScene {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private int[][] mapMatrix;
    private int hoverCol = -1;
    private int hoverRow = -1;

    public GameScene(Canvas canvas, GraphicsContext gc) {
        this.canvas = canvas;
        this.gc = gc;
        // Tải ma trận map1.csv với kích thước 18 hàng, 32 cột
        mapMatrix = com.game.map.MapLoader.loadMapMatrix("map1.csv", GameConfig.GRID_HEIGHT,
                                                                GameConfig.GRID_WIDTH);
        if (mapMatrix != null) {
            System.out.println("Đã tải map thành công! Ô tại hàng 2 cột 12 có giá trị là: " + mapMatrix[2][12]);
        } else {
            System.out.println("LỖI: Không tìm thấy hoặc không đọc được file map1.csv!");
        }
    }

    /**
     * 
     * @param deltaTime 
     */
    public void update(double deltaTime) {
    }

   
    public void render() {
        gc.setFill(Color.web("#111827"));
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int cellSize = GameConfig.GRID_CELL_SIZE;
        // Vẽ từng ô trên màn hình dựa vào dữ liệu đọc từ file map1.csv
        if (mapMatrix != null) {
            for (int r = 0; r < mapMatrix.length; r++) {
                for (int c = 0; c < mapMatrix[r].length; c++) {
                    int cellType = mapMatrix[r][c];

                    // Phân màu chuẩn theo đúng quy ước của file map1.csv
                    switch (cellType) {
                        case 0:
                            // 0 = EMPTY (Đất trống): Màu xanh rêu tối dịu mắt để đặt tháp
                            gc.setFill(Color.web("#1e293b"));
                            break;
                        case 1:
                            // 1 = PATH (Đường đi): Màu nâu đất / cam đất sáng để nổi bần bật đường đi
                            gc.setFill(Color.web("#b45309"));
                            break;
                        case 2:
                            // 2 = SPAWN (Cổng xuất phát): Màu đỏ rực
                            gc.setFill(Color.web("#ef4444"));
                            break;
                        case 3:
                            // 3 = BASE (Căn cứ): Màu xanh dương sáng
                            gc.setFill(Color.web("#3b82f6"));
                            break;
                        default:
                            gc.setFill(Color.web("#0f172a"));
                            break;
                    }

                    // Vẽ ô vuông của bản đồ
                    gc.fillRect(c * cellSize, r * cellSize, cellSize, cellSize);
                }
            }
        }

            gc.setStroke(Color.web(Constants.COLOR_GRID));
            gc.setLineWidth(0.3);

            int rows = GameConfig.GRID_HEIGHT;
            int cols = GameConfig.GRID_WIDTH;

            for (int i = 0; i <= cols; i++) {
                gc.strokeLine(i * cellSize, 0, i * cellSize, canvas.getHeight());
            }
            for (int i = 0; i <= rows; i++) {
                gc.strokeLine(0, i * cellSize, canvas.getWidth(), i * cellSize);
            }


        // Vẽ hiệu ứng hover nếu trỏ chuột nằm trong lưới
        if (hoverCol >= 0 && hoverCol < GameConfig.GRID_WIDTH && hoverRow >= 0 && hoverRow < GameConfig.GRID_HEIGHT) {
            gc.setFill(Color.rgb(255, 255, 255, 0.2)); // Màu trắng mờ độ trong suốt 20%
            gc.fillRect(hoverCol * cellSize, hoverRow * cellSize, cellSize, cellSize);
        }
    }

   
    public void handleKeyPress(KeyEvent event) {
    }


    public void handleMouseClick(MouseEvent event) {
        // Lấy tọa độ pixel của chuột so với Canvas
        double mouseX = event.getX();
        double mouseY = event.getY();

        // Lấy kích thước cấu hình ô từ GameConfig
        int cellSize = GameConfig.GRID_CELL_SIZE;
        int maxCols = GameConfig.GRID_WIDTH;
        int maxRows = GameConfig.GRID_HEIGHT;

        // Chuyển đổi từ pixel sang chỉ số cột và hàng (Col, Row)
        int col = (int) (mouseX / cellSize);
        int row = (int) (mouseY / cellSize);

        // Kiểm tra xem click có nằm trong giới hạn bản đồ không
        if (col >= 0 && col < maxCols && row >= 0 && row < maxRows) {
            if (mapMatrix != null) {
                int cellType = mapMatrix[row][col];

                if (cellType == 0) {
                    // Số 0: Đất trống -> Cho phép đặt tháp!
                    System.out.println("Thành công: Đã đặt tháp tại ô [" + row + "][" + col + "]");

                    // TODO: Gọi sang bên logic của bạn mình để trừ tiền hoặc lưu vị trí tháp thực tế
                } else if (cellType == 1) {
                    // Số 1: Đường đi -> Cấm đặt!
                    System.out.println("Cảnh báo: Không thể đặt tháp lên đường đi!");
                } else if (cellType == 2) {
                    // Số 2: Cổng Spawn -> Cấm đặt!
                    System.out.println("Cảnh báo: Đây là cổng xuất phát của quái vật!");
                } else if (cellType == 3) {
                    // Số 3: Căn cứ -> Cấm đặt!
                    System.out.println("Cảnh báo: Không thể đặt tháp đè lên căn cứ!");
                }
            }
            // TODO: Tại đây, bạn sẽ gọi sang bên logic của bạn mình
            // để kiểm tra xem ô này có được phép đặt tháp hay không.
        }
    }

    public void handleMouseMove(MouseEvent event) {
            int cellSize = GameConfig.GRID_CELL_SIZE;
            hoverCol = (int) (event.getX() / cellSize);
            hoverRow = (int) (event.getY() / cellSize);
    }

    private void drawPlaceholderGrid() {
        int cellSize = GameConfig.GRID_CELL_SIZE;
        int rows = GameConfig.GRID_HEIGHT;
        int cols = GameConfig.GRID_WIDTH;

        gc.setStroke(Color.web(Constants.COLOR_GRID));
        gc.setLineWidth(1);

        // Vẽ đường kẻ dọc
        for (int i = 0; i <= cols; i++) {
            gc.strokeLine(i * cellSize, 0, i * cellSize, canvas.getHeight());
        }
        // Vẽ đường kẻ ngang
        for (int i = 0; i <= rows; i++) {
            gc.strokeLine(0, i * cellSize, canvas.getWidth(), i * cellSize);
        }

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 24));
        gc.fillText("Game Scene (Testing Mode)", 50, 50);
        
        gc.setFont(Font.font("Arial", Constants.HUD_FONT_SIZE));
        gc.fillText("Grid Map: " + cols + "x" + rows + " cells (" + cellSize + "x" + cellSize + "px)", 50, 80);
        gc.fillText("Press ESC to return to Main Menu", 50, 110);
    }
}