package com.evgenyt.snowgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evgenyt.snowgame.GameUtils;
import com.evgenyt.snowgame.sprites.Button;
import com.evgenyt.snowgame.sprites.GiftBox;
import com.evgenyt.snowgame.sprites.Glove;
import com.evgenyt.snowgame.sprites.ScreenObject;
import com.evgenyt.snowgame.sprites.ScreenLabel;
import com.evgenyt.snowgame.sprites.SnowFlake;
import com.evgenyt.snowgame.sprites.Snowman;
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
    private static int GIFT_PAUSE_MAX = 150;
    private static int GIFT_PAUSE;
    private static int GIFT_TIMER;

    // Game score
    private static int SCORE;
    private static int HIGH_SCORE;
    private static int LIVES;
    private static boolean GAME_OVER;

    // Sprites on a screen
    private static ArrayList<SnowFlake> snowFlakes;
    private static Glove playerGlove;
    private static ScreenObject gift;
    private static ScreenObject prize;
    private static ScreenLabel scoreLabel;
    private static ScreenLabel highScoreLabel;
    private static ScreenLabel gameOverLabel;
    private static ScreenLabel livesLabel;
    private static Button backButton;
    private ScreenObject background;


    // Game window create
    public PlayState(GameStateManager manager) {
        super(manager);
        // Sprites init
        background = new ScreenObject("back_main.png",0, 0);
        playerGlove = new Glove(GameUtils.getCenterX(), 0);
        scoreLabel = new ScreenLabel(GameUtils.getScreenWidth() - 200,
                GameUtils.getScreenHeight(),  "SCORE: " + SCORE);
        highScoreLabel = new ScreenLabel(GameUtils.getScreenWidth() - 200,
                GameUtils.getScreenHeight() - 50,  "TOP: " + readHighScore());
        gameOverLabel = new ScreenLabel(GameUtils.getCenterX() - 100,
                GameUtils.getCenterY() + 30, "GAME OVER");
        livesLabel = new ScreenLabel(310,
                GameUtils.getScreenHeight(), "LIVES: " + LIVES);
        backButton = new Button(10, GameUtils.getScreenHeight() - 60, "<- Back");
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
        SCORE = 0;
        addScore(0);
        setLives(3);
        GAME_OVER = false;
        newPrize();
    }

    // Set initial physics settings
    private void physicsReset() {
        GRAVITY = GameUtils.getStartGravity();
        MAX_FLAKES = 4;
        FLAKES_PAUSE = 100;
        GIFT_PAUSE = GameUtils.random.nextInt(GIFT_PAUSE_MAX) + 10;
        GIFT_TIMER = 0;
        FLAKES_TIMER = FLAKES_PAUSE;
    }

    private static void newPrize() {
        if (prize != null)
            prize.dispose();
        prize = new Snowman(0, 0);
        prize.setX(GameUtils.getScreenWidth() - prize.getWidth() - 10);
        prize.setY(-1 * prize.getHeight());
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
        // If game over - go main menu
        if (GAME_OVER) {
            dispose();
            getStateManager().pop();
        }
        // Get X and Y of user click
        float touchX = Gdx.input.getX();
        float touchY = GameUtils.flipY(Gdx.input.getY());
        // Exit to main menu
        if (backButton.getBounds().contains(touchX, touchY)) {
            dispose();
            getStateManager().pop();
        }
        // Move player glove
        playerGlove.setX(touchX - playerGlove.getWidth() / 2);
    }

    // Update screen
    @Override
    public void update(float deltaTime) {
        handleInput();
        if (GAME_OVER)
            return;
        prize.update(deltaTime);
        // Manage gift
        if (gift != null) {
            gift.update(deltaTime);
            if (gift.collides(playerGlove)) {
                // + 1 life
                if(gift instanceof Glove) {
                    setLives(++LIVES);
                } else
                if (gift instanceof GiftBox) {
                    addScore(10);
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
                addScore(1);
                // Make game harder
                GRAVITY -= GameUtils.deltaGravity();
                if (SCORE % 4 == 0) {
                    FLAKES_PAUSE--;
                    MAX_FLAKES++;
                }
                // It's time to make a gift
                if (GIFT_TIMER >= GIFT_PAUSE) {
                    float giftX = GameUtils.getPlayRandomX();
                    float giftY = GameUtils.getScreenHeight();
                    switch (GameUtils.random.nextInt(3)) {
                        case 0: gift = new Glove(giftX, giftY);
                            break;
                        case 1: gift = new GiftBox(giftX, giftY);
                            break;
                        case 2: gift = new Wand(giftX, giftY);
                            break;
                    }
                    gift.setVelocityY(GRAVITY);
                    GIFT_TIMER = 0;
                    GIFT_PAUSE = GameUtils.random.nextInt(GIFT_PAUSE_MAX) + 10;
                } else {
                    GIFT_TIMER++;
                }
            }
        }
        // Make more snow
        if (snowFlakes.size() < MAX_FLAKES && FLAKES_TIMER > FLAKES_PAUSE) {
            snowFlakes.add(new SnowFlake(GameUtils.getPlayRandomX(),
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
        background.draw(spriteBatch, GameUtils.getScreenWidth(), GameUtils.getScreenHeight());
        for (SnowFlake snowFlake : snowFlakes)
            snowFlake.draw(spriteBatch);
        if (gift != null)
            gift.draw(spriteBatch);
        if (prize != null)
            prize.draw(spriteBatch);
        playerGlove.draw(spriteBatch);
        scoreLabel.draw(spriteBatch);
        highScoreLabel.draw(spriteBatch);
        livesLabel.draw(spriteBatch);
        backButton.draw(spriteBatch);
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
    private void addScore(int delta) {
        SCORE += delta;
        scoreLabel.setText("SCORE: " + SCORE);
        if (prize != null) {
            prize.setY(prize.getY() + delta * GameUtils.getPrizeDeltaY());
            if (prize.getY() >= 0) {
                if (prize instanceof Snowman) {
                    GameUtils.prefs.putInteger(GameUtils.KEY_PRIZE_SNOWMAN,
                            GameUtils.prefs.getInteger(GameUtils.KEY_PRIZE_SNOWMAN, 0) + 1);
                    GameUtils.prefs.flush();
                }
                getStateManager().push(new PrizeState(getStateManager()));
                newPrize();
            }

        }
    }

    // Update game high score
    private void setHighScore(int score) {
        HIGH_SCORE = score;
        highScoreLabel.setText("TOP: " + score);
        GameUtils.prefs.putInteger("HIGH_SCORE", score);
        GameUtils.prefs.flush();
    }

    // Get top score from prefs
    private int readHighScore() {
        return GameUtils.prefs.getInteger("HIGH_SCORE", 0);
    }

    // Exit game
    @Override
    public void dispose() {
        killSnow();
        playerGlove.dispose();
        scoreLabel.dispose();
        highScoreLabel.dispose();
        livesLabel.dispose();
        prize.dispose();
        backButton.dispose();
        if (gift != null)
            gift.dispose();
    }

}
