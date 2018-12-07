package com.evgenyt.snowgame.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Root class for game states classes
 */

public abstract class GameState {

    private GameStateManager stateManager ;

    GameState(GameStateManager manager) {
        stateManager = manager;
    }

    // Handle user input
    protected abstract void handleInput();

    // Update screen
    public abstract void update(float deltaTime);

    // Draw screen
    public abstract void render(SpriteBatch spriteBatch);

    // Destroy state
    public abstract void dispose();

    public GameStateManager getStateManager() {
        return stateManager;
    }

}
