package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

	TextureRegion[] up,down,left,right,idle;
	float stateTime = 0;
	Animation<TextureRegion> walkAnimation; // Must declare frame type (TextureRegion)
	Texture walkSheet;
	SpriteBatch spriteBatch;
	private static final int FRAME_COLS = 10, FRAME_ROWS = 8;
	Music menuMusic;
	Sound sound;
	boolean isPlayingMusic = false;
	int x,y,speed;
	@Override
	public void create () {
		x = 0;
		y = 0;
		speed = 200;
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mainTheme.mp3"));
	 	sound = Gdx.audio.newSound(Gdx.files.internal("deathSound.wav"));
		menuMusic.setLooping(true);
		menuMusic.setVolume(0.5f);
		menuMusic.play();
		walkSheet = new Texture("linkSheet.png");

		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / FRAME_COLS,
				walkSheet.getHeight() / FRAME_ROWS);


		up = new TextureRegion[10];
	 	down = new TextureRegion[10];
		left = new TextureRegion[10];
		right = new TextureRegion[10];
		idle = new TextureRegion[3];


		for (int j = 0; j < FRAME_COLS; j++) {
			up[j] = tmp[6][j];
		}
		for (int j = 0; j < FRAME_COLS; j++) {
			down[j] = tmp[4][j];
		}
		for (int j = 0; j < FRAME_COLS; j++) {
			left[j] = tmp[5][j];
		}
		for (int j = 0; j < FRAME_COLS; j++) {
			right[j] = tmp[7][j];
		}
		for (int j = 0; j < 3; j++) {
			idle[j] = tmp[0][j];
		}



		walkAnimation = new Animation<TextureRegion>(0.15f, left);
		spriteBatch = new SpriteBatch();
		stateTime = 0f;


	}
	
	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stateTime += Gdx.graphics.getDeltaTime();

		TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		movimiento();

		spriteBatch.begin();
		spriteBatch.draw(currentFrame, x, y);
		spriteBatch.end();

	}
	public void movimiento(){

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			x -= speed * Gdx.graphics.getDeltaTime();
			walkAnimation = new Animation<TextureRegion>(0.15f, left);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			x += speed * Gdx.graphics.getDeltaTime();
			walkAnimation = new Animation<TextureRegion>(0.15f, right	);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			y += speed * Gdx.graphics.getDeltaTime();
			walkAnimation = new Animation<TextureRegion>(0.15f, up);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			y -= speed * Gdx.graphics.getDeltaTime();
			walkAnimation = new Animation<TextureRegion>(0.15f, down);
		}else{
			walkAnimation = new Animation<TextureRegion>(0.5f, idle);
		}
	}
	@Override
	public void dispose () {
		spriteBatch.dispose();
		walkSheet.dispose();

	}
}
