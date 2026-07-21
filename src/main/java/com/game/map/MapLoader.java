package com.game.map;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MapLoader {
    // Đọc file CSV trả về mảng 2 chiều các số nguyên đại diện cho loại ô
    public static int[][] loadMapMatrix(String filePath, int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                MapLoader.class.getResourceAsStream("/maps/" + filePath)))) {

            String line;
            int r = 0;
            while ((line = br.readLine()) != null) {
                // Bỏ qua các dòng trống hoặc dòng chú thích bắt đầu bằng dấu #
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                if (r < rows) {
                    String[] values = line.split(",");
                    for (int c = 0; c < values.length && c < cols; c++) {
                        matrix[r][c] = Integer.parseInt(values[c].trim());
                    }
                    r++;
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi đọc file map: " + e.getMessage());
        }
        return matrix;
    }
}