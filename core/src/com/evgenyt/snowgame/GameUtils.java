package com.evgenyt.snowgame;

import com.badlogic.gdx.Gdx;

import java.util.Random;

/**
 * Screen data and procedures
 */

public class GameUtils {

    // Game window properties used in Desktop test launcher
    public static final int DESKTOP_SCREEN_WIDTH = 1280;
    public static final int DESKTOP_SCREEN_HEIGHT = 720;

    // Random generator
    public static Random random = new Random();

    // Directory with textures and fonts depends on screen size
    public static String getTextureDir() {
        if (getScreenHeight() > 1000)
            return "big/";
        return "normal/";
    }


    // Returns width of snowfall area
    public static float getPlayWidth() {return getScreenWidth() - 200;}

    public static float getPlayRandomX() {return 50 + random.nextFloat() * (getPlayWidth() - 100);}

    // Gravity gradient depends on screen size
    public static float deltaGravity() {return getScreenHeight() / 500;}

    // Initial gravity depends of screen size
    public static float getStartGravity() {
        return -70 * getScreenHeight() / 500;
    }

    // Get center of the screen y-coordinate
    public static float getCenterY() {return getScreenHeight() / 2;}

    // Get center of the screen x-coordinate
    public static float getCenterX() {
        return getScreenWidth() / 2;
    }

    // Random x-coordinate with padding
    public static float getRandomX() {
        return 50 + random.nextFloat() * (getScreenWidth() - 100);
    }

    // Convert Y screen coordinate upside down
    public static float flipY(float y) {
        return getScreenHeight() - 1 - y;
    }

    public static float getScreenWidth() {
        return Gdx.graphics.getWidth();
    }

    public static float getScreenHeight() {
        return Gdx.graphics.getHeight();
    }

}
