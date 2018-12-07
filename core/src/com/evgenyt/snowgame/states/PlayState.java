package com.evgenyt.snowgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evgenyt.snowgame.GameUtils;
import com.evgenyt.snowgame.sprites.Glove;
import com.evgenyt.snowgame.sprites.ScreenObject;
import com.evgenyt.snowgame.sprites.SnowFlake;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Main play loop screen
 */

public class PlayState extends GameState {

    private static float GRAVITY = -100;
    private static float SNOW_DENSITY = 0.99f;

    private static ArrayList<ScreenObject> screenObjects;
    private static Glove playerGlove;

    // New game
    public PlayState(GameStateManager manager) {
        super(manager);
        playerGlove = new Glove(GameUtils.getCenterX(), 0);
        screenObjects = new ArrayList<>();
    }

    // Handle user input actions
    @Override
    protected void handleInput() {
        // If nothing pressed - exit
        if (!Gdx.input.justTouched())
            return;

        // Get X and Y of user click
        float touchX = Gdx.input.getX();
        float touchY = GameUtils.flipY(Gdx.input.getY());

        playerGlove.setX(touchX);

    }

    // Update screen
    @Override
    public void update(float deltaTime) {
        // Manage screen objects
        Iterator<ScreenObject> objectIterator = screenObjects.iterator();
        while (objectIterator.hasNext()) {
            ScreenObject screenObject = objectIterator.next();
            screenObject.update(deltaTime);
            if (screenObject.getY() < -1 * screenObject.getHeight()) {
                screenObject.dispose();
                objectIterator.remove();
            } else
            if (screenObject.collides(playerGlove)) {
                screenObject.dispose();
                objectIterator.remove();
            }
        }
        // Make more snow
        if (GameUtils.random.nextFloat() > SNOW_DENSITY) {
            screenObjects.add(new SnowFlake(GameUtils.getRandomX(),
                    GameUtils.getScreenHeight(), GRAVITY));
        }
        handleInput();
    }

    // Render screen
    @Override
    public void render(SpriteBatch spriteBatch) {
        // Sprite tool draw cycle begin
        spriteBatch.begin();
        for (ScreenObject screenObject : screenObjects)
            screenObject.draw(spriteBatch);
        playerGlove.draw(spriteBatch);
        // Sprite tool draw cycle end
        spriteBatch.end();
    }

    // Exit game
    @Override
    public void dispose() {
        for (ScreenObject screenObject : screenObjects)
            screenObject.dispose();

    }

}
