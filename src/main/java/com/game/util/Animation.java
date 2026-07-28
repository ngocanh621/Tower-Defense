package com.game.util;
import javafx.scene.image.Image;

/**
 * Lớp quản lý hiệu ứng chuyển động (Animation) dựa trên mảng các ảnh.
 */

public class Animation {
    private Image[] frames;         // Mảng các khung hình
    private int currentFrame;       // Chỉ số khung hình hiện tại (0, 1, 2...)
    private final double frameDuration;   // Thời gian hiển thị mỗi khung hình (tính bằng giây)
    private double frameTimer;      // Biến tích lũy thời gian
    private final boolean loop;           // Cờ cho phép lặp lại (true) hay chạy 1 lần (false)
    private boolean finished;       // Đánh dấu animation đã chạy xong chưa (dùng cho hiệu ứng nổ/đánh)

    /**
     * Khởi tạo một Animation cơ bản với cờ lặp lại mặc định là true.
     * @param frames Mảng chứa các Image của animation
     * @param frameDuration Thời gian chuyển cảnh giữa các frame (ví dụ: 0.15s)
     */
    public Animation(Image[] frames, double frameDuration) {
        this(frames, frameDuration, true);
    }

    /**
     * Khởi tạo Animation đầy đủ tùy chọn lặp lại.
     */
    public Animation(Image[] frames, double frameDuration, boolean loop) {
        this.frames = frames;
        this.frameDuration = frameDuration;
        this.loop = loop;
        this.currentFrame = 0;
        this.frameTimer = 0;
        this.finished = false;
    }

    /**
     * Cập nhật logic thời gian để chuyển đổi frame.
     * @param deltaTime Khoảng thời gian giữa 2 khung hình render (giây)
     */
    public void update(double deltaTime) {
        if (frames == null || frames.length == 0 || finished) {
            return;
        }

        frameTimer += deltaTime;
        if (frameTimer >= frameDuration) {
            frameTimer -= frameDuration;

            if (currentFrame < frames.length - 1) {
                currentFrame++;
            } else {
                if (loop) {
                    currentFrame = 0; // Lặp lại từ đầu
                } else {
                    finished = true;  // Dừng lại ở frame cuối cùng
                }
            }
        }
    }

    /**
     * Lấy ra Image của khung hình hiện tại để vẽ lên Canvas.
     */
    public Image getCurrentFrame() {
        if (frames == null || frames.length == 0) {
            return null;
        }
        return frames[currentFrame];
    }

    /**
     * Resets lại animation về frame đầu tiên (Dùng khi tái sử dụng Object từ Pool).
     */
    public void reset() {
        this.currentFrame = 0;
        this.frameTimer = 0;
        this.finished = false;
    }

    public boolean isFinished() { return finished; }
    public void setFrames(Image[] frames) {
        this.frames = frames;
        reset();
    }
}
