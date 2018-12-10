package com.evgenyt.snowgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evgenyt.snowgame.GameUtils;
import com.evgenyt.snowgame.sprites.GiftBox;
import com.evgenyt.snowgame.sprites.Glove;
import com.evgenyt.snowgame.sprites.ScreenObject;
import com.evgenyt.snowgame.sprites.ScreenLabel;
import com.evgenyt.snowgame.sprites.SnowFlake;
import com.evgenyt.snowgame.sprites.Wand;

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

    // Game score
    private static int SCORE;
    private static int HIGH_SCORE;
    private static int LIVES;
    private static boolean GAME_OVER;

    // Prefs storage
    Preferences prefs;

    // Sprites on a screen
    private static ArrayList<SnowFlake> snowFlakes;
    private static Glove playerGlove;
    private static ScreenObject gift;
    private static ScreenLabel scoreLabel;
    private static ScreenLabel highScoreLabel;
    private static ScreenLabel gameOverLabel;
    private static ScreenLabel livesLabel;

    // Game window create
    public PlayState(GameStateManager manager) {
        super(manager);
        // Saved data storage init
        prefs = Gdx.app.getPreferences("prefs");
        // Sprites init
        playerGlove = new Glove(GameUtils.getCenterX(), 0);
        scoreLabel = new ScreenLabel(GameUtils.getScreenWidth() - 200,
                GameUtils.getScreenHeight(),  "SCORE: " + SCORE);
        highScoreLabel = new ScreenLabel(GameUtils.getScreenWidth() - 200,
                GameUtils.getScreenHeight() - 50,  "TOP: " + readHighScore());
        gameOverLabel = new ScreenLabel(GameUtils.getCenterX() - 100,
                GameUtils.getCenterY() + 30, "GAME OVER");
        livesLabel = new ScreenLabel(0,
                GameUtils.getScreenHeight(), "LIVES: " + LIVES);
        snowFlakes = new ArrayList<>();
        // Start new game
        newGame();
    }

    // Game start/restart
    private void newGame() {
        physicsReset();
        playerGlove.setX(GameUtils.getCenterX());
        killSnow();
        if (SCORE > HIGH_SCORE) {
            setHighScore(SCORE);
        }
        setHighScore(readHighScore());
        setScore(0);
        setLives(3);
        GAME_OVER = false;
    }

    // Set initial physics settings
    private void physicsReset() {
        GRAVITY = GameUtils.getStartGravity();
        MAX_FLAKES = 4;
        FLAKES_PAUSE = 100;
        FLAKES_TIMER = FLAKES_PAUSE;
    }

    // Destroy all snow flakes
    private void killSnow(){
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
        // If game over - start new game
        if (GAME_OVER) {
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
        // Manage gift
        if (gift != null) {
            gift.update(deltaTime);
            if (gift.collides(playerGlove)) {
                // + 1 life
                if(gift instanceof Glove) {
                    setLives(++LIVES);
                } else
                if (gift instanceof GiftBox) {
                    SCORE += 10;
                    setScore(SCORE);
                } else
                if (gift instanceof Wand) {
                    physicsReset();
                }
                gift.dispose();
                gift = null;
            }
        }
        // Manage snow flakes
        Iterator<SnowFlake> objectIterator = snowFlakes.iterator();
        while (objectIterator.hasNext()) {
            SnowFlake snowFlake = objectIterator.next();
            snowFlake.update(deltaTime);
            // Snowflake on the ground
            if (snowFlake.getY() < -1 * snowFlake.getHeight()) {
                setLives(--LIVES);
                // Game over, men. Game over.
                if (LIVES <= 0)
                    GAME_OVER = true;
                else
                    physicsReset();
                    killSnow();
                break;
            } else
            // Snowflake cached
            if (snowFlake.collides(playerGlove)) {
                snowFlake.dispose();
                objectIterator.remove();
                setScore(++SCORE);
                // Make game harder
                GRAVITY--;
                if (SCORE % 4 == 0) {
                    FLAKES_PAUSE--;
                    MAX_FLAKES++;
                }
                // Make a gift
                if (SCORE % 100 == 0) {
                    switch (GameUtils.random.nextInt(3)) {
                        case 0: gift = new Glove(GameUtils.getRandomX(),
                                    GameUtils.getScreenHeight());
                                break;
                        case 1: gift = new GiftBox(GameUtils.getRandomX(),
                                GameUtils.getScreenHeight());
                                break;
                        case 2: gift = new Wand(GameUtils.getRandomX(),
                                GameUtils.getScreenHeight());
                            break;
                    }
                    gift.setVelocityY(GRAVITY * 2);
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
        if (gift != null)
            gift.draw(spriteBatch);
        playerGlove.draw(spriteBatch);
        scoreLabel.draw(spriteBatch);
        highScoreLabel.draw(spriteBatch);
        livesLabel.draw(spriteBatch);
        if (GAME_OVER)
            gameOverLabel.draw(spriteBatch);
        // Sprite tool draw cycle end
        spriteBatch.end();
    }

    // Update lives count
    private void setLives(int lives) {
        LIVES = lives;
        livesLabel.setText("LIVES: " + LIVES);
    }

    // Update game score
    private void setScore(int score) {
        SCORE = score;
        scoreLabel.setText("SCORE: " + score);
    }

    // Update game high score
    private void setHighScore(int score) {
        HIGH_SCORE = score;
        highScoreLabel.setText("TOP: " + score);
        prefs.putInteger("HIGH_SCORE", score);
        prefs.flush();
    }

    // Get top score from prefs
    private int readHighScore() {
        return prefs.getInteger("HIGH_SCORE", 0);
    }

    // Exit game
    @Override
    public void dispose() {
        killSnow();
        playerGlove.dispose();
        scoreLabel.dispose();
        highScoreLabel.dispose();
        livesLabel.dispose();
    }

}
