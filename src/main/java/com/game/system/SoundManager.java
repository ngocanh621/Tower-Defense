package com.game.system;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private static SoundManager instance;
    private MediaPlayer bgmPlayer;

    private SoundManager() {}

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Phát nhạc nền phát lặp lại (Loop)
     * @param soundPath Đường dẫn tệp audio trong resources (ví dụ: "/audio/mainMenu.mp3")
     */
    public void playBGM(String soundPath) {
        // Nếu đang phát cùng một bài nhạc thì giữ nguyên
        stopBGM();

        try {
            URL resource = getClass().getResource(soundPath);
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                bgmPlayer = new MediaPlayer(media);
                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp vô tận
                bgmPlayer.setVolume(0.4); // Âm lượng 40%
                bgmPlayer.play();
            } else {
                System.err.println("Không tìm thấy tệp âm thanh: " + soundPath);
            }
        } catch (Exception e) {
            System.err.println("Lỗi phát BGM: " + e.getMessage());
        }
    }

    /**
     * Dừng nhạc nền hiện tại
     */
    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
            bgmPlayer = null;
        }
    }

    /**
     * Tùy chỉnh âm lượng BGM (từ 0.0 đến 1.0)
     */
    public void setBGMVolume(double volume) {
        if (bgmPlayer != null) {
            bgmPlayer.setVolume(volume);
        }
    }
}