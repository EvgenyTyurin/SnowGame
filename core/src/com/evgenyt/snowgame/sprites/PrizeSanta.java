package com.evgenyt.snowgame.sprites;

import com.evgenyt.snowgame.GameUtils;

/**
 * Santa prize
 */

public class PrizeSanta extends Prize {
    public PrizeSanta(float x, float y) {
        super(x, y, GameUtils.PrizeTypes.SANTA);
    }
}
