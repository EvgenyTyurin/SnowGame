package com.evgenyt.snowgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evgenyt.snowgame.GameUtils;
import com.evgenyt.snowgame.sprites.Glove;
import com.evgenyt.snowgame.sprites.ScreenObject;
import com.evgenyt.snowgame.sprites.ScreenLabel;
import com.evgenyt.snowgame.sprites.SnowFlake;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Main play loop screen
 */

public class PlayState extends GameState {

    // Game "physics" parameters
    private static float GRAVITY;
    private static int MAX_FLAKES;
    private static int FLAKES_PAUSE;
    private static int FLAKES_TIMER;
    private static boolean GAME_OVER = false;

    // Game score
    private static int SCORE;

    // Sprites on a screen
    private static ArrayList<SnowFlake> snowFlakes;
    private static Glove playerGlove;
    private static ScreenLabel scoreLabel;
    private static ScreenLabel gameOverLabel;

    // Game start
    public PlayState(GameStateManager manager) {
        super(manager);
        playerGlove = new Glove(GameUtils.getCenterX(), 0);
        scoreLabel = new ScreenLabel(GameUtils.getScreenWidth() - 200,
                GameUtils.getScreenHeight(),  "SCORE: " + SCORE);
        gameOverLabel = new ScreenLabel(GameUtils.getCenterX() - 100,
                GameUtils.getCenterY() + 30, "GAME OVER");
        snowFlakes = new ArrayList<>();
        newGame();
    }

    // Game start/restart
    private void newGame() {
        setScore(0);
        GRAVITY = GameUtils.getStartGravity();
        MAX_FLAKES = 4;
        FLAKES_PAUSE = 100;
        FLAKES_TIMER = FLAKES_PAUSE;
        playerGlove.setX(GameUtils.getCenterX());
        Iterator<SnowFlake> objectIterator = snowFlakes.iterator();
        while (objectIterator.hasNext()) {
            SnowFlake snowFlake = objectIterator.next();
            snowFlake.dispose();
            objectIterator.remove();
        }
    }

    // Handle user input actions
    @Override
    protected void handleInput() {
        // If nothing pressed - exit
        if (!Gdx.input.justTouched())
            return;
        if (GAME_OVER) {
            GAME_OVER = false;
            newGame();
            return;
        }
        // Get X and Y of user click
        float touchX = Gdx.input.getX();
        // float touchY = GameUtils.flipY(Gdx.input.getY());

        playerGlove.setX(touchX - playerGlove.getWidth() / 2);

    }

    // Update screen
    @Override
    public void update(float deltaTime) {
        handleInput();
        if (GAME_OVER)
            return;
        // Manage screen objects
        Iterator<SnowFlake> objectIterator = snowFlakes.iterator();
        while (objectIterator.hasNext()) {
            SnowFlake snowFlake = objectIterator.next();
            snowFlake.update(deltaTime);
            if (snowFlake.getY() < -1 * snowFlake.getHeight()) {
                GAME_OVER = true;
            } else
            if (snowFlake.collides(playerGlove)) {
                snowFlake.dispose();
                objectIterator.remove();
                SCORE++;
                setScore(SCORE);
                if (SCORE % 2 == 0) {
                    MAX_FLAKES++;
                    GRAVITY--;
                    FLAKES_PAUSE--;
                }
            }
        }
        // Make more snow
        if (snowFlakes.size() < MAX_FLAKES && FLAKES_TIMER > FLAKES_PAUSE) {
            snowFlakes.add(new SnowFlake(GameUtils.getRandomX(),
                    GameUtils.getScreenHeight(),
                    GRAVITY));
            FLAKES_TIMER = 0;
        } else {
            FLAKES_TIMER ++;
        }
    }

    // Render screen
    @Override
    public void render(SpriteBatch spriteBatch) {
        // Sprite tool draw cycle begin
        spriteBatch.begin();
        for (SnowFlake snowFlake : snowFlakes)
            snowFlake.draw(spriteBatch);
        playerGlove.draw(spriteBatch);
        scoreLabel.draw(spriteBatch);
        if (GAME_OVER)
            gameOverLabel.draw(spriteBatch);
        // Sprite tool draw cycle end
        spriteBatch.end();
    }

    // Update game score
    private void setScore(int score) {
        SCORE = score;
        scoreLabel.setText("SCORE: " + score);
    }

    // Exit game
    @Override
    public void dispose() {
        for (ScreenObject screenObject : snowFlakes)
            screenObject.dispose();
        playerGlove.dispose();
        scoreLabel.dispose();
    }

}
