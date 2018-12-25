package com.evgenyt.snowgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.evgenyt.snowgame.GameUtils;
import com.evgenyt.snowgame.sprites.Prize;
import com.evgenyt.snowgame.sprites.PrizeDeer;
import com.evgenyt.snowgame.sprites.PrizeRabbit;
import com.evgenyt.snowgame.sprites.PrizeSanta;
import com.evgenyt.snowgame.sprites.PrizeSnegurka;
import com.evgenyt.snowgame.sprites.PrizeSnowMan;
import com.evgenyt.snowgame.sprites.ScreenObject;

import java.util.ArrayList;

/**
 * Screen with game prizes
 */

public class PrizeState extends GameState {

    ShapeRenderer shapeRenderer;
    private ArrayList<Prize> prizes;
    private ScreenObject background;
    private ScreenObject backButton;
    private ScreenObject pickedPrize;

    private int snowman_count;
    private int rabbit_count;
    private int snegurka_count;
    private int santa_count;
    private int deer_count;

    PrizeState(GameStateManager manager) {
        super(manager);
        shapeRenderer = new ShapeRenderer();
        background = new ScreenObject("prize_back.png", 0, 0);
        backButton = new ScreenObject("button_close.png", 0, GameUtils.getScreenHeight());
        backButton.setY(GameUtils.getScreenHeight() - backButton.getHeight());
        prizes = new ArrayList<>();
        snowman_count = GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SNOWMAN, 0);
        rabbit_count = GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_RABBIT, 0);
        snegurka_count = GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SNEGURKA, 0);
        santa_count = GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SANTA, 0);
        deer_count = GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_DEER, 0);
        loadPrizes();
        float prizeY = 10f;
        float prizeX = 10f;
        for (int i = 1; i <= snowman_count; i++) {
            prizes.add( new PrizeSnowMan(prizeX, prizeY));
            prizeX += GameUtils.PRIZE_SNOWMAN_TEXTURE.getWidth() * GameUtils.textureRatio() * 1.2f;
        }
        for (int i = 1; i <= rabbit_count; i++) {
            prizes.add( new PrizeRabbit(prizeX, prizeY));
            prizeX += GameUtils.PRIZE_RABBIT_TEXTURE.getWidth() * GameUtils.textureRatio()  * 1.2f;
        }
        for (int i = 1; i <= snegurka_count; i++) {
            prizes.add( new PrizeSnegurka(prizeX, prizeY));
            prizeX += GameUtils.PRIZE_SNEGURKA_TEXTURE.getWidth() * GameUtils.textureRatio() * 1.2f;
        }
        for (int i = 1; i <= santa_count; i++) {
            prizes.add( new PrizeSanta(prizeX, prizeY));
            prizeX += GameUtils.PRIZE_SANTA_TEXTURE.getWidth() * GameUtils.textureRatio() * 1.2f;
        }
        for (int i = 1; i <= deer_count; i++) {
            prizes.add( new PrizeDeer(prizeX, prizeY));
            prizeX += GameUtils.PRIZE_DEER_TEXTURE.getWidth() * GameUtils.textureRatio() * 1.2f;
        }
    }


    private void loadPrizes() {
        String strTypes = GameUtils.prefs.getString(GameUtils.KEY_PRIZE_TYPE, "");
        if (strTypes.equals(""))
            return;
        String[] prizeTypes = strTypes.split(",");
        String strX = GameUtils.prefs.getString(GameUtils.KEY_PRIZE_X, "");
        String[] prizeX = strX.split(",");
        String strY = GameUtils.prefs.getString(GameUtils.KEY_PRIZE_Y, "");
        String[] prizeY = strY.split(",");
        for (int idx = 0; idx < prizeTypes.length; idx++) {
            String type = prizeTypes[idx];
            float x = Float.valueOf(prizeX[idx]);
            float y = Float.valueOf(prizeY[idx]);
            switch (type) {
                case "DEER":
                    prizes.add(new PrizeDeer(x, y));
                    deer_count--;
                    break;
                case "SANTA":
                    prizes.add(new PrizeSanta(x, y));
                    santa_count--;
                    break;
                case "SNOWMAN":
                    prizes.add(new PrizeSnowMan(x, y));
                    snowman_count--;
                    break;
                case "SNEGURKA":
                    prizes.add(new PrizeSnegurka(x, y));
                    snegurka_count--;
                    break;
                case "RABBIT":
                    prizes.add(new PrizeRabbit(x, y));
                    rabbit_count--;
                    break;
            }
        }
    }

    // Save prizes positions
    private void savePrizes() {
        String prizeTypes = "";
        String prizeX = "";
        String prizeY = "";
        for (Prize prize : prizes) {
            prizeTypes += prize.getType().toString() + ",";
            prizeX += prize.getX() + ",";
            prizeY += prize.getY() + ",";
        }
        GameUtils.prefs.putString(GameUtils.KEY_PRIZE_TYPE, prizeTypes);
        GameUtils.prefs.putString(GameUtils.KEY_PRIZE_X, prizeX);
        GameUtils.prefs.putString(GameUtils.KEY_PRIZE_Y, prizeY);
        GameUtils.prefs.flush();
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
            savePrizes();
            dispose();
            getStateManager().pop();
        }
        // Move prize
        if (pickedPrize == null) {
            for (ScreenObject prize : prizes) {
                if (prize.getBounds().contains(touchX, touchY)) {
                    pickedPrize = prize;
                }
            }
        } else {
            pickedPrize.setPos(touchX, touchY);
            pickedPrize = null;
        }
    }

    @Override
    public void update(float deltaTime) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        float selX = 0;
        float selY = 0;
        float selW = 0;
        float selH = 0;
        spriteBatch.begin();
        background.draw(spriteBatch, GameUtils.getScreenWidth(), GameUtils.getScreenHeight());
        for (ScreenObject prize : prizes) {
            prize.draw(spriteBatch);
            if (prize == pickedPrize) {
                selX = prize.getX();
                selY = prize.getY();
                selW = prize.getWidth();
                selH = prize.getHeight();
            }

        }
        backButton.draw(spriteBatch);
        spriteBatch.end();
        if (pickedPrize != null) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(selX, selY, selW, selH);
            shapeRenderer.end();
        }
    }

    @Override
    public void dispose() {
        background.dispose();
        backButton.dispose();
        // shapeRenderer.dispose();
    }
}
