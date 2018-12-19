package com.evgenyt.snowgame.sprites;

/**
 * Snowflake class
 */

public class SnowFlake extends ScreenObject {

    private boolean cached;

    // New snowflake
    public SnowFlake(float x, float y, float gravity) {
        super("flake.png", x, y);
        setVelocityY(gravity);
        cached = false;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached(boolean cached) {
        this.cached = cached;
    }
}
