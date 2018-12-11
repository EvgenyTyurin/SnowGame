package com.evgenyt.snowgame.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Button
 */

public class Button extends ScreenObject {
    public Button(float x, float y, String text) {
        super("button.png", x, y, text);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if (getTexture() != null)
            spriteBatch.draw(getTexture(), getX(), getY());
        if (getText() != null && !getText().equals(""))
            getFont().draw(spriteBatch, getText(), getX(), getY() + 50);

    }
}
