package com.game.system;

import java.util.*;
import java.util.function.Consumer;

/**
 * trạm phát tin nhắn trung tâm giữa các lớp trong game (Singleton pattern)
 * giúp các lớp truyền tin cho nhau mà k cần gọi trực tiếp nhau
 */
public class EventBus {

    private static final EventBus INSTANCE = new EventBus();
    private final Map<GameEvent, List<Consumer<Object>>> listeners = new EnumMap<>(GameEvent.class);

    private EventBus() {
    }

    /**
     * lấy instance duy nhất của EventBus
     */
    public static EventBus getInstance() {
        return INSTANCE;
    }

    /**
     * đăng ký nhận thông báo khi có sự kiện xảy ra
     *
     * @param event    loại sự kiện cần lắng nghe
     * @param listener hàm xử lý khi nhận tin
     */
    public void subscribe(GameEvent event, Consumer<Object> listener) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }

    /**
     * hủy đăng ký nhận thông báo sự kiện
     */
    public void unsubscribe(GameEvent event, Consumer<Object> listener) {
        List<Consumer<Object>> subs = listeners.get(event);
        if (subs != null) {
            subs.remove(listener);
        }
    }

    /**
     * phát tin nhắn sự kiện cho tất cả các bên đã đăng ký lắng nghe
     *
     * @param event loại sự kiện phát ra
     * @param data  dữ liệu kèm theo nếu có
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
     * phát tin nhắn sự kiện k kèm dữ liệu
     */
    public void publish(GameEvent event) {
        publish(event, null);
    }

    /**
     * xóa toàn bộ bên lắng nghe
     */
    public void clear() {
        listeners.clear();
    }

    /**
     * xóa bên lắng nghe của 1 sự kiện cụ thể
     */
    public void clear(GameEvent event) {
        listeners.remove(event);
    }
}