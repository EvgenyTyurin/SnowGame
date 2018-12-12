package com.evgenyt.snowgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evgenyt.snowgame.GameUtils;
import com.evgenyt.snowgame.sprites.Button;
import com.evgenyt.snowgame.sprites.ScreenObject;
import com.evgenyt.snowgame.sprites.SnowFlake;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Main menu screen
 */

public class MainMenu extends GameState {

    // Game "physics" parameters
    private static float GRAVITY;
    private static int MAX_FLAKES;
    private static int FLAKES_PAUSE;
    private static int FLAKES_TIMER;

    private Button gameButton;
    private Button prizeButton;
    private ScreenObject background;
    private static ArrayList<SnowFlake> snowFlakes;

    // Create main menu
    public MainMenu(GameStateManager manager) {
        super(manager);
        gameButton = new Button(GameUtils.getCenterX() - 150, GameUtils.getCenterY() + 50,
                "Play");
        prizeButton = new Button(GameUtils.getCenterX() - 150, GameUtils.getCenterY() - 50,
                "Gifts");
        background = new ScreenObject("back_main.png",0, 0);
        snowFlakes = new ArrayList<>();
        GRAVITY = GameUtils.getStartGravity();
        MAX_FLAKES = 20;
        FLAKES_PAUSE = 50;
        FLAKES_TIMER = FLAKES_PAUSE;
    }

    // User click
    @Override
    protected void handleInput() {

        // If nothing pressed - exit
        if (!Gdx.input.justTouched())
            return;
        // Get X and Y of user click
        float touchX = Gdx.input.getX();
        float touchY = GameUtils.flipY(Gdx.input.getY());

        // Buttons clicked
        if (gameButton.getBounds().contains(touchX, touchY)) {
            getStateManager().push(new PlayState(getStateManager()));
        } else
        if (prizeButton.getBounds().contains(touchX, touchY)) {
            getStateManager().push(new PrizeState(getStateManager()));
        }

    }

    @Override
    public void update(float deltaTime) {
        handleInput();
        // Manage snow flakes
        Iterator<SnowFlake> objectIterator = snowFlakes.iterator();
        while (objectIterator.hasNext()) {
            SnowFlake snowFlake = objectIterator.next();
            snowFlake.update(deltaTime);
            // Snowflake on the ground
            if (snowFlake.getY() < -1 * snowFlake.getHeight()) {
                snowFlake.dispose();
                objectIterator.remove();
            }
        }
        // Make more snow
        if (snowFlakes.size() < MAX_FLAKES && FLAKES_TIMER > FLAKES_PAUSE) {
            snowFlakes.add(new SnowFlake(GameUtils.getRandomX(),
                    GameUtils.getScreenHeight(),
                    GRAVITY / 2));
            FLAKES_TIMER = 0;
        } else {
            FLAKES_TIMER ++;
        }
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.begin();
        background.draw(spriteBatch, GameUtils.getScreenWidth(), GameUtils.getScreenHeight());
        for (SnowFlake snowFlake : snowFlakes)
            snowFlake.draw(spriteBatch);
        gameButton.draw(spriteBatch);
        prizeButton.draw(spriteBatch);
        spriteBatch.end();
    }

    @Override
    public void dispose() {
        gameButton.dispose();
        prizeButton.dispose();
        background.dispose();
    }

}
