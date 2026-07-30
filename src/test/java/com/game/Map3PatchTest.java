package com.game;

import org.junit.jupiter.api.Test;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class Map3PatchTest {

    @Test
    public void findYellowPatchesInMap3() throws Exception {
        File f3 = new File("src/main/resources/assets/map3.png");
        BufferedImage raw3 = ImageIO.read(f3);

        int targetW = 1280;
        int targetH = 720;
        int cellSize = 40;
        int cols = targetW / cellSize; // 32
        int rows = targetH / cellSize; // 18

        BufferedImage img3 = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_RGB);
        Graphics2D gContext3 = img3.createGraphics();
        gContext3.drawImage(raw3, 0, 0, targetW, targetH, null);
        gContext3.dispose();

        boolean[][] isYellowGrid = new boolean[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int yellowPixels = 0;
                int totalPixels = 0;

                for (int y = r * cellSize; y < (r + 1) * cellSize; y++) {
                    for (int x = c * cellSize; x < (c + 1) * cellSize; x++) {
                        totalPixels++;
                        int rgb = img3.getRGB(x, y);
                        int red = (rgb >> 16) & 0xFF;
                        int green = (rgb >> 8) & 0xFF;
                        int blue = rgb & 0xFF;

                        // Sand / yellow color filter: High Red & Green, lower Blue (e.g. sand patch)
                        // Sand patch: R ~ 200-240, G ~ 170-220, B ~ 100-150
                        if (red > 180 && green > 150 && blue < 140 && (red - blue > 40)) {
                            yellowPixels++;
                        }
                    }
                }

                if ((double) yellowPixels / totalPixels > 0.25) {
                    isYellowGrid[r][c] = true;
                }
            }
        }

        System.out.println("--- MAP3 YELLOW COLOR PIXELS (32x18 GRID) ---");
        for (int r = 0; r < rows; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < cols; c++) {
                sb.append(isYellowGrid[r][c] ? "Y " : ". ");
            }
            System.out.println(String.format("%2d: %s", r, sb.toString()));
        }
    }
}
