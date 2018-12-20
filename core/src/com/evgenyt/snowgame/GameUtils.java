package com.evgenyt.snowgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.evgenyt.snowgame.sprites.ScreenObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Game, screen, audio data and procedures
 */

public class GameUtils {

    // Development mode flag
    static final boolean DEVELOPMENT = false;

    public static final Sound snowSound = Gdx.audio.newSound(Gdx.files.internal("snow.wav"));

    // Game window properties used in Desktop test launcher
    public static final int DESKTOP_SCREEN_WIDTH = 1280;
    public static final int DESKTOP_SCREEN_HEIGHT = 720;

    // Random generator
    public static Random random = new Random();

    // Prefs storage
    public static Preferences prefs = Gdx.app.getPreferences("prefs");

    // Prefs keys
    public static final String KEY_PRIZE_SNOWMAN = "PRIZE_SNOWMAN";
    public static final String KEY_PRIZE_SANTA = "PRIZE_SANTA";
    public static final String KEY_PRIZE_SNEGURKA = "PRIZE_SNEGURKA";
    public static final String KEY_PRIZE_DEER = "PRIZE_DEER";
    public static final String KEY_PRIZE_RABBIT = "PRIZE_RABBIT";

    // Textures
    public static final Texture flake_texture =
            new Texture(GameUtils.getTextureDir() + "flake.png");
    public static final Texture back_play =
            new Texture(GameUtils.getTextureDir() + "back_play.png");

    // Maximum pause between gifts
    public static int getGiftPauseMax() {
        if (DEVELOPMENT)
            return 1;
        else
            return 150;
    }

    // Get speed of prize rising
    public static float getPrizeDeltaY() {
        if (DEVELOPMENT)
            return 50;
        else
            return 2 * textureRatio();
    }


    // Destroy sprites in collection and clear it
    public static void clearSpriteArray (ArrayList<ScreenObject> arrayList) {
        for (ScreenObject screenObject : arrayList)
            screenObject.dispose();
        arrayList.clear();
    }

    // Value to adapt textures to actual screen size from default target width=2000
    public static float textureRatio() {
        return getScreenWidth() / 2000;
    }

    // Directory with textures and fonts depends on screen size
    public static String getTextureDir() {
        return "textures/";
    }

    // Returns width of snowfall area
    public static float getPlayWidth() {return getScreenWidth() - 200;}

    public static float getPlayRandomX() {return 50 + random.nextFloat() * (getPlayWidth() - 100);}

    // Gravity gradient depends on screen size
    public static float deltaGravity() {return getScreenHeight() / 500 * 2;}

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
