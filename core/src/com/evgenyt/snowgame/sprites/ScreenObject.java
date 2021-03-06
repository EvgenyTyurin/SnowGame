package com.evgenyt.snowgame.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.evgenyt.snowgame.GameUtils;

/**
 * Game screen object
 */

public class ScreenObject {

    private Texture texture;
    private String text;
    private float x, y, velocityY, velocityX;
    private BitmapFont font;
    private boolean keepTexture = false;

    // New empty object on x,y coordinates
    public ScreenObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // New object with loaded texture
    public ScreenObject(Texture texture, float x, float y) {
        this(x,y);
        this.texture = texture;
        keepTexture = true;
    }

    // New object with img and text
    public ScreenObject(String textureFile, float x, float y, String text) {

        if (textureFile != null && !textureFile.equals("")) {
            if (GameUtils.getScreenHeight() > 500)
                textureFile = GameUtils.getTextureDir() + textureFile;
            this.texture = new Texture(textureFile);
        }
        this.text = text;
        this.x = x;
        this.y = y;
        font = new BitmapFont(Gdx.files.internal(GameUtils.getTextureDir() + "verdana.fnt"));
        font.setColor(Color.WHITE);
    }

    // New object with img only
    public ScreenObject(String textureFile, float x, float y) {
        this(textureFile, x, y, "");
    }

    // New object with text only
    public ScreenObject(float x, float y, String text) {
        this("", x, y, text);
    }

    // Draw object on screen with game scaling
    public void draw(SpriteBatch spriteBatch) {
        if (texture != null) {
            spriteBatch.draw(texture, x, y, texture.getWidth() * GameUtils.textureRatio(),
                    texture.getHeight() * GameUtils.textureRatio());
        }
        if (text != null && !text.equals(""))
            font.draw(spriteBatch, text, x, y);
    }

    // Draw object on screen with specified width and height
    public void draw(SpriteBatch spriteBatch, float w, float h) {
        if (texture != null)
            spriteBatch.draw(texture, x, y, w, h);
        if (text != null && !text.equals("") && font != null)
            font.draw(spriteBatch, text, x, y);
    }

    // Draw object on screen with specified scale
    public void draw(SpriteBatch spriteBatch, float scale) {
        if (texture != null) {
            float w = getWidth() * scale;
            float h = getHeight() * scale;
            spriteBatch.draw(texture, x, y, w, h);
        }
        if (text != null && !text.equals("") && font != null)
            font.draw(spriteBatch, text, x, y);
    }

    // Update object position
    public void update(float dt) {
        y += velocityY * dt;
        x += velocityX * dt;
    }

    // Object height
    public float getHeight() {
        if (texture != null)
            return texture.getHeight() * GameUtils.textureRatio();
        else
            return 0;
    }

    // Object width
    public float getWidth() {
        if (texture != null)
            return texture.getWidth() * GameUtils.textureRatio();
        else
            return 0;
    }

    // Object bounds
    public Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }

    // Is object collides with another
    public boolean collides(ScreenObject otherObject) {
        if (getBounds().overlaps(otherObject.getBounds()))
            return true;
        else
            return false;
    }

    // Set position on a screen
    public void setPos(float x, float y) {
        setX(x);
        setY(y);
    }

    // Destroy object
    public void dispose() {
        if (texture != null && !keepTexture)
            texture.dispose();
    }

    public Texture getTexture() {
        return texture;
    }

    public String getText() {
        return text;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setY(float y) {
        this.y = y;
    }

    public BitmapFont getFont() {
        return font;
    }

}
