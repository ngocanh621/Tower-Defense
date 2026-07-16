package com.game.model;

/**
 * Base interface for objects that can be pooled.
 */
public interface Poolable {

    /**
     * Reset the object to its initial state for reuse.
     */
    void reset();

    /**
     * Check if this object is currently in use.
     */
    boolean isActive();

    /**
     * Set the active state of this object.
     */
    void setActive(boolean active);
}
