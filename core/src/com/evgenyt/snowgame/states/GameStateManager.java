package com.evgenyt.snowgame.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Stack;

/**
 * Game states manage responsible for game screens switching
 */

public class GameStateManager {

    // Loaded game screens
    private Stack<GameState> states;

    // New manager
    public GameStateManager() {
        states = new Stack();
    }

    // Add new game screen
    public void push(GameState state) {
        states.push(state);
    }

    // Delete last loaded game screen
    public void pop() {
        states.pop().dispose();
    }

    // Replace active game screen with new
    public void set(GameState state) {
        states.pop().dispose();
        states.push(state);
    }

    // Active game screen processing
    public void update(float dt) {
        states.peek().update(dt);
    }

    // Active game screen render
    public void render(SpriteBatch sb) {
        states.peek().render(sb);
    }

}

