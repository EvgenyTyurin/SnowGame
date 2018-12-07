package com.evgenyt.snowgame.sprites;

/**
 * Snowflake class
 */

public class SnowFlake extends ScreenObject {

    // New snowflake
    public SnowFlake(float x, float y, float gravity) {
        super("flake.png", x, y);
        setVelocityY(gravity);
    }

}
