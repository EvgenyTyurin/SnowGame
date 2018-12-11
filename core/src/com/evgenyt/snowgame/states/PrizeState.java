package com.evgenyt.snowgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evgenyt.snowgame.GameUtils;
import com.evgenyt.snowgame.sprites.PrizeSnowMan;
import com.evgenyt.snowgame.sprites.ScreenObject;

import java.util.ArrayList;

/**
 * Screen with game prizes
 */

public class PrizeState extends GameState {

    ArrayList<ScreenObject> prizes;

    public PrizeState(GameStateManager manager) {
        super(manager);
        prizes = new ArrayList<>();
        int snowman_count = GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SNOWMAN, 0);
        for (int i = 1; i <= snowman_count; i++) {
            prizes.add(new PrizeSnowMan(50 * i, 10));
        }
    }

    @Override
    protected void handleInput() {
        // If nothing pressed - exit
        if (!Gdx.input.justTouched())
            return;
        getStateManager().pop();
    }

    @Override
    public void update(float deltaTime) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        for (ScreenObject prize : prizes)
            prize.draw(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void dispose() {

    }
}
