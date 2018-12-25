package com.evgenyt.snowgame.sprites;

import com.evgenyt.snowgame.GameUtils;

public class Prize extends ScreenObject {

    private GameUtils.PrizeTypes type;

    public Prize(float x, float y, GameUtils.PrizeTypes type) {
        super(GameUtils.getPrizeTexture(type), x, y);
        this.type = type;
    }

    public GameUtils.PrizeTypes getType() {
        return type;
    }
}
