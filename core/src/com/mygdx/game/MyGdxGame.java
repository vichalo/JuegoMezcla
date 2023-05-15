package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {

	float stateTime = 0;
	Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
	Texture walkSheet;
	SpriteBatch spriteBatch;
	private static final int FRAME_COLS = 10, FRAME_ROWS = 8;
	Music menuMusic;
	Sound sound;
	boolean isPlayingMusic = false;
	@Override
	public void create () {
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mainTheme.mp3"));
	 	sound = Gdx.audio.newSound(Gdx.files.internal("deathSound.wav"));
		menuMusic.setLooping(true);
		menuMusic.setVolume(0.5f);
		menuMusic.play();
		walkSheet = new Texture("linkSheet.png");

		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / FRAME_COLS,
				walkSheet.getHeight() / FRAME_ROWS);


		TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
		int index = 0;
		for (int i = 0; i < FRAME_ROWS; i++) {
			for (int j = 0; j < FRAME_COLS; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}


		walkAnimation = new Animation<TextureRegion>(0.15f, walkFrames);
		spriteBatch = new SpriteBatch();
		stateTime = 0f;


	}
	
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime();

		TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, 50, 50);
		spriteBatch.end();


		if (walkAnimation.isAnimationFinished(stateTime)) {
			if (!isPlayingMusic) {
				isPlayingMusic = true;
				sound.play();
			}
			stateTime = 0;
		} else {
			isPlayingMusic = false;
		}
	}

	@Override
	public void dispose () {
		spriteBatch.dispose();
		walkSheet.dispose();

	}
}
