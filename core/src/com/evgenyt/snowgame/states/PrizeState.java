package com.evgenyt.snowgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
    private Pixmap pixMapBack;

    PrizeState(GameStateManager manager) {
        super(manager);
        background = new ScreenObject("prize_back.png", 0, 0);
        if (!background.getTexture().getTextureData().isPrepared())
            background.getTexture().getTextureData().prepare();
        pixMapBack = background.getTexture().getTextureData().consumePixmap();
        prizes = new ArrayList<>();
        int snowman_count = GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SNOWMAN, 0);
        for (int i = 1; i <= snowman_count; i++) {
            Vector2 posAtTree = getPosAtTree();
            prizes.add(new PrizeSnowMan(posAtTree.x, posAtTree.y));
        }
        for (int i = 1; i <= GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_RABBIT, 0); i++) {
            Vector2 posAtTree = getPosAtTree();
            prizes.add(new PrizeRabbit(posAtTree.x, posAtTree.y));
        }
        for (int i = 1; i <= GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SNEGURKA, 0); i++) {
            Vector2 posAtTree = getPosAtTree();
            prizes.add(new PrizeSnegurka(posAtTree.x, posAtTree.y));
        }
        for (int i = 1; i <= GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SANTA, 0); i++) {
            Vector2 posAtTree = getPosAtTree();
            prizes.add(new PrizeSanta(posAtTree.x, posAtTree.y));
        }
        for (int i = 1; i <= GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_DEER, 0); i++) {
            Vector2 posAtTree = getPosAtTree();
            prizes.add(new PrizeDeer(posAtTree.x, posAtTree.y));
        }
    }

    // Search random point at christmas tree
    private Vector2 getPosAtTree() {
        float prizeX;
        float prizeY;
        Color color;
        do {
            prizeX = GameUtils.random.nextFloat() * GameUtils.getScreenWidth();
            prizeY = GameUtils.random.nextFloat() * GameUtils.getScreenHeight();
            color = new Color(pixMapBack.getPixel((int) (prizeX / GameUtils.textureRatio()),
                    (int) (prizeY / GameUtils.textureRatio())));
        }
        while (color.r > 0.11f || color.r <= 0);
        return new Vector2(prizeX, prizeY);
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
