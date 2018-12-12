package com.evgenyt.snowgame.sprites;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evgenyt.snowgame.GameUtils;

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
            spriteBatch.draw(getTexture(), getX(), getY(),
                    getTexture().getWidth() * GameUtils.textureRatio(),
                    getTexture().getHeight() * GameUtils.textureRatio());
        if (getText() != null && !getText().equals(""))
            getFont().draw(spriteBatch, getText(), getX(),
                    getY() + getTexture().getHeight() * GameUtils.textureRatio());

    }
}
