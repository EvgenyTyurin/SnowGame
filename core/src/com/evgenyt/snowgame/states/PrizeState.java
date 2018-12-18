package com.evgenyt.snowgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evgenyt.snowgame.GameUtils;
import com.evgenyt.snowgame.sprites.PrizeSnowMan;
import com.evgenyt.snowgame.sprites.ScreenObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Screen with game prizes
 */

public class PrizeState extends GameState {

    private ArrayList<ScreenObject> prizes;
    private ScreenObject background;

    PrizeState(GameStateManager manager) {
        super(manager);
        prizes = new ArrayList<>();
        int snowman_count = GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SNOWMAN, 0);
        for (int i = 1; i <= snowman_count; i++) {
            ScreenObject prize = new PrizeSnowMan(0, 0);
            prize.setX(GameUtils.random.nextFloat() * GameUtils.getScreenWidth());
            prize.setY(GameUtils.random.nextFloat() * GameUtils.getScreenHeight() / 2);
            prizes.add(prize);
        }
        background = new ScreenObject("prize_back.png", 0, 0);
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
        background.draw(spriteBatch, GameUtils.getScreenWidth(), GameUtils.getScreenHeight());
        for (ScreenObject prize : prizes)
            prize.draw(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        Iterator<ScreenObject> objectIterator = prizes.iterator();
        while (objectIterator.hasNext()) {
            ScreenObject screenObject = objectIterator.next();
            screenObject.dispose();
            objectIterator.remove();
        }
    }
}
