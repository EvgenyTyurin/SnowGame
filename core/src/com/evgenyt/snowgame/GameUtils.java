package com.evgenyt.snowgame;

import com.badlogic.gdx.Gdx;

import java.util.Random;

/**
 * Screen data and procedures
 */

public class GameUtils {

    // Game window properties used in Desktop test launcher
    private static final int DESKTOP_SCREEN_WIDTH = 1000;
    private static final int DESKTOP_SCREEN_HEIGHT = 500;

    // Random generator
    public static Random random = new Random();

    public static float getCenterX() {
        return getScreenWidth() / 2;
    }

    // Random x-coordinate
    public static float getRandomX() {
        return random.nextFloat() * getScreenWidth();
    }

    // Convert Y screen coordinate upside down
    public static float flipY(float y) {
        return getScreenHeight() - 1 - y;
    }

    public static int getDesktopScreenWidth() {
        return DESKTOP_SCREEN_WIDTH;
    }

    public static int getDesktopScreenHeight() {
        return DESKTOP_SCREEN_HEIGHT;
    }

    public static float getScreenWidth() {
        return Gdx.graphics.getWidth();
    }

    public static float getScreenHeight() {
        return Gdx.graphics.getHeight();
    }

}
