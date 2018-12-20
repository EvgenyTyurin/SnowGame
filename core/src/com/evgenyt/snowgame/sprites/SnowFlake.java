package com.evgenyt.snowgame.sprites;

import com.evgenyt.snowgame.GameUtils;

/**
 * Snowflake class
 */

public class SnowFlake extends ScreenObject {

    private boolean cached;

    // New snowflake
    public SnowFlake(float x, float y, float gravity) {
        super(GameUtils.flake_texture, x, y);
        setVelocityY(gravity);
        cached = false;
    }

    public boolean isCached() {
        return cached;
    }

    public void setCached() {
        this.cached = true;
    }
}
