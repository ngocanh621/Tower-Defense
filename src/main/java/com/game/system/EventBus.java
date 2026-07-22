package com.game.system;

import java.util.*;
import java.util.function.Consumer;

/**
 * Trạm phát/nhận tin nhắn trung tâm (Event Bus / Observer Pattern) áp dụng Singleton.
 * Giúp giao tiếp giữa các hệ thống trong game mà không bị phụ thuộc chặt chẽ (Loose Coupling).
 */
public class EventBus {

    private static final EventBus INSTANCE = new EventBus();
    private final Map<GameEvent, List<Consumer<Object>>> listeners = new EnumMap<>(GameEvent.class);

    private EventBus() {
        // Khóa constructor để ngăn việc tạo thêm thể hiện (Singleton)
    }

    /**
     * Lấy thể hiện duy nhất của EventBus.
     */
    public static EventBus getInstance() {
        return INSTANCE;
    }

    /**
     * Đăng ký nhận thông báo (Subscribe) khi một sự kiện xảy ra.
     *
     * @param event    Loại sự kiện muốn lắng nghe
     * @param listener Hàm xử lý (Callback) khi nhận tin
     */
    public void subscribe(GameEvent event, Consumer<Object> listener) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Hủy đăng ký lắng nghe một sự kiện.
     */
    public void unsubscribe(GameEvent event, Consumer<Object> listener) {
        List<Consumer<Object>> subs = listeners.get(event);
        if (subs != null) {
            subs.remove(listener);
        }
    }

    /**
     * Bắn đi một sự kiện (Publish) để tất cả các bên đăng ký cùng nhận dữ liệu.
     *
     * @param event Loại sự kiện phát ra
     * @param data  Dữ liệu kèm theo sự kiện (có thể là null)
     */
    public void publish(GameEvent event, Object data) {
        List<Consumer<Object>> subs = listeners.getOrDefault(event, Collections.emptyList());
        subs.forEach(listener -> {
            try {
                listener.accept(data);
            } catch (Exception e) {
                System.err.println("Error in event listener for " + event + ": " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Phát một sự kiện không mang theo dữ liệu.
     */
    public void publish(GameEvent event) {
        publish(event, null);
    }

    /**
     * Xóa toàn bộ listener (dùng khi reset game hoặc test).
     */
    public void clear() {
        listeners.clear();
    }

    /**
     * Xóa các listener của một sự kiện cụ thể.
     */
    public void clear(GameEvent event) {
        listeners.remove(event);
    }
}