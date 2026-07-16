package com.game.util;

/**
 * Game-wide constants.
 */
public class Constants {

    // Colors (as hex strings)
    public static final String COLOR_BACKGROUND = "#0a0e27";
    public static final String COLOR_GRID = "#333333";
    public static final String COLOR_PATH = "#556677";
    public static final String COLOR_EMPTY = "#1a1a2e";
    public static final String COLOR_TOWER_SELECTED = "#ffff00";

    // UI constants
    public static final int HUD_PADDING = 10;
    public static final int HUD_FONT_SIZE = 16;

    // Animation
    public static final int SPRITE_FRAME_TIME = 100; // milliseconds per frame

    // Physics
    public static final float GRAVITY = 0f; // No gravity for 2D top-down view

    // Debugging
    public static final boolean DEBUG_MODE = true;
    public static final boolean DEBUG_GRID = true;
    public static final boolean DEBUG_COLLISION = false;

    private Constants() {
        // Private constructor to prevent instantiation
    }
}
