package com.game.system;

import com.game.model.Poolable;
import java.util.*;
import java.util.function.Supplier;

/**
 * Generic object pool for managing reusable objects.
 * Reduces garbage collection pressure by recycling objects.
 *
 * @param <T> The type of objects to pool (must implement Poolable)
 */
public class ObjectPool<T extends Poolable> {

    private final Queue<T> available;
    private final Set<T> inUse;
    private final Supplier<T> factory;
    private final int initialSize;

    public ObjectPool(Supplier<T> factory, int initialSize) {
        this.factory = factory;
        this.initialSize = initialSize;
        this.available = new LinkedList<>();
        this.inUse = new HashSet<>();

        // Pre-allocate objects
        for (int i = 0; i < initialSize; i++) {
            available.offer(factory.get());
        }
    }

    /**
     * Acquire an object from the pool.
     * If no objects are available, a new one is created.
     */
    public T acquire() {
        T obj = available.poll();
        if (obj == null) {
            obj = factory.get();
        }
        obj.setActive(true);
        inUse.add(obj);
        return obj;
    }

    /**
     * Release an object back to the pool.
     */
    public void release(T obj) {
        if (inUse.remove(obj)) {
            obj.reset();
            obj.setActive(false);
            available.offer(obj);
        }
    }

    /**
     * Get the number of available objects in the pool.
     */
    public int getAvailableCount() {
        return available.size();
    }

    /**
     * Get the number of objects currently in use.
     */
    public int getInUseCount() {
        return inUse.size();
    }

    /**
     * Get the total number of objects in the pool (available + in use).
     */
    public int getTotalCount() {
        return available.size() + inUse.size();
    }

    /**
     * Expand the pool by creating new objects.
     */
    public void expand(int count) {
        for (int i = 0; i < count; i++) {
            available.offer(factory.get());
        }
    }

    /**
     * Clear the entire pool.
     */
    public void clear() {
        available.clear();
        inUse.clear();
    }
}
