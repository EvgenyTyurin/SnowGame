package com.evgenyt.snowgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evgenyt.snowgame.states.GameStateManager;
import com.evgenyt.snowgame.states.PlayState;

/**
 * Let It Snow Game
 * 2018 Evgeny Tyurin
 */

public class SnowGame extends ApplicationAdapter {

	public static final String APP_TITLE = "Let It Snow";

	// Sprites draw instrument
	private SpriteBatch spriteBatch;

	// Game screens manager
	private GameStateManager stateManager;

	// Game launch
	@Override
	public void create () {
		// Prepare screen
		spriteBatch = new SpriteBatch();
		Gdx.gl.glClearColor(0, 0, 0, 1);

		// Game start
		stateManager = new GameStateManager();
		stateManager.push(new PlayState(stateManager));
	}

	// Game render
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Screen manager process and render current game screen
		stateManager.update(Gdx.graphics.getDeltaTime());
		stateManager.render(spriteBatch);
	}

	// Game exit
	@Override
	public void dispose () {
		spriteBatch.dispose();
	}

}
