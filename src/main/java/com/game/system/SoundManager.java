package com.game.system;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {

    private static SoundManager instance;
    private MediaPlayer bgmPlayer;
    private String currentBgmPath = "";

    // Cache lưu trữ các AudioClip SFX giúp tối ưu hiệu năng và không bị trễ tiếng
    private final Map<String, AudioClip> sfxCache = new HashMap<>();
    private double sfxVolume = 0.6; // Âm lượng mặc định cho hiệu ứng âm thanh (60%)

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
        // Nếu bài nhạc này đang phát thì không khởi tạo lại để tránh ngắt đoạn
        if (soundPath.equals(currentBgmPath) && bgmPlayer != null
                && bgmPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            return;
        }

        stopBGM();

        try {
            URL resource = getClass().getResource(soundPath);
            if (resource != null) {
                Media media = new Media(resource.toExternalForm());
                bgmPlayer = new MediaPlayer(media);
                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Lặp vô tận
                bgmPlayer.setVolume(0.4); // Âm lượng 40%
                bgmPlayer.play();
                currentBgmPath = soundPath;
            }
        } catch (Exception e) {
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
            currentBgmPath = "";
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

    /**
     * Phát hiệu ứng âm thanh ngắn bằng đường dẫn file
     * @param soundPath Đường dẫn đến file âm thanh trong resources (ví dụ: "/audio/goblin_death.mp3")
     */
    public void playSFX(String soundPath) {
        if (soundPath == null || soundPath.isEmpty()) return;

        try {
            // Tận dụng cache nếu file đã từng được nạp
            AudioClip clip = sfxCache.get(soundPath);

            if (clip == null) {
                URL resource = getClass().getResource(soundPath);
                if (resource != null) {
                    clip = new AudioClip(resource.toExternalForm());
                    sfxCache.put(soundPath, clip);
                } else {
                    return;
                }
            }

            clip.play(sfxVolume);
        } catch (Exception e) {
        }
    }

    /**
     * Điều chỉnh âm lượng chung cho các hiệu ứng SFX (từ 0.0 đến 1.0)
     */
    public void setSFXVolume(double volume) {
        this.sfxVolume = volume;
    }
}