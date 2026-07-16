package com.game.system;

import java.util.*;
import java.util.function.Consumer;

/**
 * Singleton Event Bus for inter-system communication.
 * Allows loose coupling between game systems.
 */
public class EventBus {

    private static final EventBus INSTANCE = new EventBus();
    private final Map<GameEvent, List<Consumer<Object>>> listeners = new EnumMap<>(GameEvent.class);

    private EventBus() {
        // Private constructor for singleton
    }

    /**
     * Get the singleton instance.
     */
    public static EventBus getInstance() {
        return INSTANCE;
    }

    /**
     * Subscribe to a game event.
     *
     * @param event    The event to subscribe to
     * @param listener The listener callback
     */
    public void subscribe(GameEvent event, Consumer<Object> listener) {
        listeners.computeIfAbsent(event, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Unsubscribe from a game event.
     *
     * @param event    The event to unsubscribe from
     * @param listener The listener to remove
     */
    public void unsubscribe(GameEvent event, Consumer<Object> listener) {
        List<Consumer<Object>> subs = listeners.get(event);
        if (subs != null) {
            subs.remove(listener);
        }
    }

    /**
     * Publish an event to all subscribers.
     *
     * @param event The event to publish
     * @param data  The event data (can be null)
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
     * Publish an event without data.
     *
     * @param event The event to publish
     */
    public void publish(GameEvent event) {
        publish(event, null);
    }

    /**
     * Clear all listeners (useful for testing or cleanup).
     */
    public void clear() {
        listeners.clear();
    }

    /**
     * Clear listeners for a specific event.
     *
     * @param event The event to clear listeners for
     */
    public void clear(GameEvent event) {
        listeners.remove(event);
    }
}
