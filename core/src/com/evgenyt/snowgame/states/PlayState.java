package com.evgenyt.snowgame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evgenyt.snowgame.GameUtils;
import com.evgenyt.snowgame.sprites.Deer;
import com.evgenyt.snowgame.sprites.GiftBox;
import com.evgenyt.snowgame.sprites.Glove;
import com.evgenyt.snowgame.sprites.Rabbit;
import com.evgenyt.snowgame.sprites.Santa;
import com.evgenyt.snowgame.sprites.ScreenObject;
import com.evgenyt.snowgame.sprites.ScreenLabel;
import com.evgenyt.snowgame.sprites.Snegurka;
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
    private float GRAVITY;
    private int MAX_FLAKES;
    private int FLAKES_PAUSE;
    private int FLAKES_TIMER;
    private int GIFT_PAUSE;
    private int GIFT_TIMER;

    // Game score
    private int SCORE;
    private int HIGH_SCORE;
    private int LIVES;
    private boolean GAME_OVER;

    // Sprites on a screen
    private ArrayList<SnowFlake> snowFlakes;
    private ArrayList<ScreenObject> liveGloves;
    private Glove playerGlove;
    private ScreenObject gift;
    private ScreenObject prize;
    private ScreenLabel scoreLabel;
    private ScreenLabel highScoreLabel;
    private ScreenLabel gameOverLabel;
    private ScreenObject backButton;
    private ScreenObject background;


    // Game window create
    PlayState(GameStateManager manager) {
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
        backButton = new ScreenObject("button_close.png", 10, GameUtils.getScreenHeight());
        backButton.setY(GameUtils.getScreenHeight() - backButton.getHeight() - 10);
        snowFlakes = new ArrayList<>();
        liveGloves = new ArrayList<>();
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
        LIVES = 0;
        addLives(2);
        GAME_OVER = false;
        newPrize();
    }

    // Set initial physics settings
    private void physicsReset() {
        GRAVITY = GameUtils.getStartGravity();
        MAX_FLAKES = 4;
        FLAKES_PAUSE = 100;
        GIFT_PAUSE = GameUtils.random.nextInt(GameUtils.getGiftPauseMax()) + 10;
        GIFT_TIMER = 0;
        FLAKES_TIMER = FLAKES_PAUSE;
    }

    // New prize appears under right corner of screen
    private void newPrize() {
        if (prize != null)
            prize.dispose();
        int prizeType = GameUtils.random.nextInt(54);
        if (prizeType < 15)
            prize = new Snowman(0, 0);
        else if (prizeType < 30)
            prize = new Rabbit(0, 0);
        else if (prizeType < 45)
            prize = new Deer(0, 0);
        else if (prizeType < 50)
            prize = new Snegurka(0, 0);
        else
            prize = new Santa(0, 0);
        /*
        switch (GameUtils.random.nextInt(10)) {
            case 0: prize = new Snowman(0, 0);
                    break;
            case 3: prize = new Rabbit(0, 0);
                break;
            case 4: prize = new Deer(0, 0);
                break;
            case 2: prize = new Snegurka(0, 0);
                break;
            case 1: prize = new Santa(0, 0);
                    break;
        }
        */
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
                    addLives(1);
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
                addLives(-1);
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
                    GIFT_PAUSE = GameUtils.random.nextInt(GameUtils.getGiftPauseMax()) + 10;
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
        if (prize != null)
            prize.draw(spriteBatch);
        for (ScreenObject liveGlove : liveGloves)
            liveGlove.draw(spriteBatch, liveGlove.getWidth() / 2, liveGlove.getHeight() / 2);
        for (SnowFlake snowFlake : snowFlakes)
            snowFlake.draw(spriteBatch);
        if (gift != null)
            gift.draw(spriteBatch);
        playerGlove.draw(spriteBatch);
        scoreLabel.draw(spriteBatch);
        highScoreLabel.draw(spriteBatch);
        backButton.draw(spriteBatch);
        if (GAME_OVER)
            gameOverLabel.draw(spriteBatch);
        // Sprite tool draw cycle end
        spriteBatch.end();
    }

    // Update lives count
    private void addLives(int delta_lives) {
        LIVES +=delta_lives;
        GameUtils.clearSpriteArray(liveGloves);
        for (int i = 1; i <= LIVES; i++) {
            Glove liveGlove = new Glove(0,0);
            liveGlove.setX(liveGlove.getWidth() * (i - 1) / 2 +
                    backButton.getX() + backButton.getWidth() + 10);
            liveGlove.setY(GameUtils.getScreenHeight() - (liveGlove.getHeight() / 2) - 10);
            liveGloves.add(liveGlove);
        }
    }

    // Update game score
    private void addScore(int delta) {
        SCORE += delta;
        scoreLabel.setText("SCORE: " + SCORE);
        if (prize != null) {
            prize.setY(prize.getY() + delta * GameUtils.getPrizeDeltaY());
            if (prize.getY() >= 0) {
                String prefKey = "";
                if (prize instanceof Snowman)
                    prefKey = GameUtils.KEY_PRIZE_SNOWMAN;
                if (prize instanceof Santa)
                    prefKey = GameUtils.KEY_PRIZE_SANTA;
                if (prize instanceof Snegurka)
                    prefKey = GameUtils.KEY_PRIZE_SNEGURKA;
                if (prize instanceof Deer)
                    prefKey = GameUtils.KEY_PRIZE_DEER;
                if (prize instanceof Rabbit)
                    prefKey = GameUtils.KEY_PRIZE_RABBIT;
                if (!prefKey.equals("")) {
                    GameUtils.prefs.putInteger(prefKey,
                            GameUtils.prefs.getInteger(prefKey, 0) + 1);
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

    // Exit play state
    @Override
    public void dispose() {
        killSnow();
        GameUtils.clearSpriteArray(liveGloves);
        playerGlove.dispose();
        scoreLabel.dispose();
        highScoreLabel.dispose();
        prize.dispose();
        backButton.dispose();
        if (gift != null)
            gift.dispose();
    }

}
