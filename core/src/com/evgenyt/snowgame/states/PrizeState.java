package com.evgenyt.snowgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evgenyt.snowgame.GameUtils;
import com.evgenyt.snowgame.sprites.PrizeDeer;
import com.evgenyt.snowgame.sprites.PrizeRabbit;
import com.evgenyt.snowgame.sprites.PrizeSanta;
import com.evgenyt.snowgame.sprites.PrizeSnegurka;
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
    private ScreenObject backButton;

    PrizeState(GameStateManager manager) {
        super(manager);
        background = new ScreenObject("prize_back.png", 0, 0);
        backButton = new ScreenObject("button_close.png", 0, GameUtils.getScreenHeight());
        backButton.setY(GameUtils.getScreenHeight() - backButton.getHeight());
        prizes = new ArrayList<>();
        int snowman_count = GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SNOWMAN, 0);
        float prizeY = 10f;
        float prizeX = 10f;
        for (int i = 1; i <= snowman_count; i++) {
            prizes.add( new PrizeSnowMan(prizeX, prizeY));
            prizeX += GameUtils.PRIZE_SNOWMAN_TEXTURE.getWidth() * GameUtils.textureRatio() * 1.2f;
        }
        for (int i = 1; i <= GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_RABBIT, 0); i++) {
            prizes.add( new PrizeRabbit(prizeX, prizeY));
            prizeX += GameUtils.PRIZE_RABBIT_TEXTURE.getWidth() * GameUtils.textureRatio()  * 1.2f;
        }
        for (int i = 1; i <= GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SNEGURKA, 0); i++) {
            prizes.add( new PrizeSnegurka(prizeX, prizeY));
            prizeX += GameUtils.PRIZE_SNEGURKA_TEXTURE.getWidth() * GameUtils.textureRatio() * 1.2f;
        }
        for (int i = 1; i <= GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SANTA, 0); i++) {
            prizes.add( new PrizeSanta(prizeX, prizeY));
            prizeX += GameUtils.PRIZE_SANTA_TEXTURE.getWidth() * GameUtils.textureRatio() * 1.2f;
        }
        for (int i = 1; i <= GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_DEER, 0); i++) {
            prizes.add( new PrizeDeer(prizeX, prizeY));
            prizeX += GameUtils.PRIZE_DEER_TEXTURE.getWidth() * GameUtils.textureRatio() * 1.2f;
        }
    }

    @Override
    protected void handleInput() {
        // If nothing pressed - exit
        if (!Gdx.input.justTouched())
            return;
        // Get X and Y of user click
        float touchX = Gdx.input.getX();
        float touchY = GameUtils.flipY(Gdx.input.getY());
        // Close prize window
        if (backButton.getBounds().contains(touchX, touchY)) {
            dispose();
            getStateManager().pop();
        }
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
        backButton.draw(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        backButton.dispose();
    }
}
