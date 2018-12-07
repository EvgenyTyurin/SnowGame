package com.evgenyt.snowgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.evgenyt.snowgame.GameUtils;
import com.evgenyt.snowgame.SnowGame;

/**
 * Run game under Windows
 */

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		// Setting config of the game window
		config.width = GameUtils.getDesktopScreenWidth();
		config.height = GameUtils.getDesktopScreenHeight();
		config.title = SnowGame.APP_TITLE;
		new LwjglApplication(new SnowGame(), config);
	}
}
