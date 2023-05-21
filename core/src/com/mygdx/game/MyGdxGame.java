package com.mygdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	private static final int FRAME_COLS = 10, FRAME_ROWS = 8;
	Rectangle link;
	TextureRegion[] up,down,left,right,idle;
	float stateTime = 0;
	long direction,initialTouchY,initialTouchX;
	boolean canShoot = true;
	private float tiempoEntreDisparos = 0.5f; // Tiempo en segundos entre disparos
	private float tiempoTranscurridoDesdeDisparo = 0;
	int lastDirection = -1;
	Texture proyectilTexture;
	Animation<TextureRegion> walkAnimation, shootAnimation; // Must declare frame type (TextureRegion)
	Texture walkSheet,backgroundImage,muelte;
	SpriteBatch spriteBatch;
	Music menuMusic;
	Sound sound;
	int gi = 0, px = 364, py = 364;
	ArrayList<Bullet>proyectiles = new ArrayList<>();
	int x,y,speed;
	private List<Enemigo> enemigos;
	private OrthographicCamera camera;
	private Viewport viewport;
	private int screenWidth = 900; // Ancho deseado de la pantalla
	private int screenHeight = 600; // Alto deseado de la pantalla
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height); // Actualiza el viewport con el nuevo tamaño de la pantalla
	}
	@Override
	public void create () {
		x = 200;
		y = 200;
		enemigos = new ArrayList<Enemigo>();
		link = new Rectangle(x,y,50,50);
		camera = new OrthographicCamera();
		viewport = new FitViewport(screenWidth, screenHeight, camera); // Utiliza el modo FitViewport para ajustar el tamaño manteniendo la relación de aspecto
		viewport.apply(); // Aplica los cambios en el viewpo
		speed = 200;

		setAssets();
		walkAnimations();

		walkAnimation = new Animation<TextureRegion>(0.15f, right);
		spriteBatch = new SpriteBatch();
		stateTime = 0f;
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		System.out.println(delta);
		stateTime += Gdx.graphics.getDeltaTime();
		generarEnemigos();
		// Actualiza y dibuja los enemigos
		for (Enemigo enemigo : enemigos) {
			enemigo.update(delta);
			// Dibuja el enemigo en la pantalla
		}

		for (int i = 0; i < proyectiles.size(); i++) {
			Bullet proyectil = proyectiles.get(i);
			proyectil.update(1.5f);

			if (proyectil.isMarkedForRemoval()) {
				proyectiles.remove(i);
				i--;
			}
		}

		tiempoTranscurridoDesdeDisparo += delta;

		if (tiempoTranscurridoDesdeDisparo >= tiempoEntreDisparos) {
			canShoot = true;
		}
		spriteBatch.begin();
		spriteBatch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		for (Bullet proyectil : proyectiles) {
			proyectil.render(spriteBatch);
		}
		for (Enemigo enemigo : enemigos) {
			enemigo.update(delta);
			Vector2 direccion = new Vector2(x - enemigo.getPosicion().x, y- enemigo.getPosicion().y);
			float velocidadX = direccion.x/5; // Ajusta la velocidad horizontal del enemigo según la dirección
			float velocidadY = direccion.y/5; // Ajusta la velocidad vertical del enemigo según la dirección

			enemigo.setVelocidad(new Vector2(velocidadX,velocidadY));
			enemigo.render(spriteBatch);
		}

		TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		controles();
		limites();

		spriteBatch.draw(currentFrame, x, y,50,50);
		spriteBatch.end();
		for (Bullet bala : proyectiles) {
			for (Enemigo enemigo : enemigos) {
				if (bala.getBoundingBox().overlaps(enemigo.getBoundingBox())) {
					// Colisión detectada: elimina la bala y el enemigo
					bala.setMarkedForRemoval(true);
					enemigos.remove(enemigo);
					break; // Rompe el bucle inter
				}
			}
		}
	}
	private void generarEnemigos() {

		if (TimeUtils.millis() % 55 == 0) {
			float ex = MathUtils.random(0, Gdx.graphics.getWidth());
			float ey = MathUtils.random(0, Gdx.graphics.getHeight());
			float velocidadX = 0;
			float velocidadY = 0;
			Enemigo nuevoEnemigo = new Enemigo(ex, ey, velocidadX, velocidadY, muelte);
			enemigos.add(nuevoEnemigo);
			System.out.println("ENEMUU");
		}
	}
	void limites(){
		if (x < 100) {
			x=100;
		}
		if (x + 100 > Gdx.graphics.getWidth()) {
			x=(Gdx.graphics.getWidth() - 100);
		}
		if (y < 100) {
			y=100;
		}
		if (y + 100 > Gdx.graphics.getHeight()) {
			y=(Gdx.graphics.getHeight() - 100);
		}
	}
	void disparar() {
		if (lastDirection != -1 && canShoot) {
			float proyectilSpeed = 5; // Velocidad del proyectil
			float proyectilX = x; // Posición X inicial del proyectil (según tu lógica de juego)
			float proyectilY = y; // Posición Y inicial del proyectil (según tu lógica de juego)

			switch (lastDirection) {
				case Input.Keys.UP:
					// Disparar hacia arriba
					proyectiles.add(new Bullet(proyectilTexture, proyectilX, proyectilY, 0, proyectilSpeed));
					break;
				case Input.Keys.DOWN:
					// Disparar hacia abajo
					proyectiles.add(new Bullet(proyectilTexture, proyectilX, proyectilY, 0, -proyectilSpeed));
					break;
				case Input.Keys.LEFT:
					// Disparar hacia la izquierda
					proyectiles.add(new Bullet(proyectilTexture, proyectilX, proyectilY, -proyectilSpeed, 0));
					break;
				case Input.Keys.RIGHT:
					// Disparar hacia la derecha
					proyectiles.add(new Bullet(proyectilTexture, proyectilX, proyectilY, proyectilSpeed, 0));
					break;
			}
			tiempoTranscurridoDesdeDisparo = 0;
			canShoot = false;
		}
	}
	public void controles(){

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			x -= speed * Gdx.graphics.getDeltaTime();
			lastDirection = Input.Keys.LEFT;
			walkAnimation = new Animation<TextureRegion>(0.15f, left);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			x += speed * Gdx.graphics.getDeltaTime();
			lastDirection = Input.Keys.RIGHT;
			walkAnimation = new Animation<TextureRegion>(0.15f, right	);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			y += speed * Gdx.graphics.getDeltaTime();
			walkAnimation = new Animation<TextureRegion>(0.15f, up);
			lastDirection = Input.Keys.UP;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			lastDirection = Input.Keys.DOWN;
			y -= speed * Gdx.graphics.getDeltaTime();
			walkAnimation = new Animation<TextureRegion>(0.15f, down);
		}else{

		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			disparar();

		}
	}
	public void walkAnimations(){
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
	}
	void setAssets(){
		menuMusic = Gdx.audio.newMusic(Gdx.files.internal("mainTheme.mp3"));
		sound = Gdx.audio.newSound(Gdx.files.internal("deathSound.wav"));
		menuMusic.setLooping(true);
		menuMusic.setVolume(0.5f);
		menuMusic.play();
		muelte = new Texture("muelte.png");
		proyectilTexture = new Texture(Gdx.files.internal("bullet.png"));
		backgroundImage = new Texture(Gdx.files.internal("background.png"));
		walkSheet = new Texture("linkSheet.png");
	}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		float diffX = screenX - initialTouchX;
		float diffY = screenY - initialTouchY;

		if (diffX >= 0) direction = 0;
		else direction = 1;
		px += diffX/40;
		py -= diffY/100;
		gi++;

		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		initialTouchX = screenX;
		initialTouchY = screenY;
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public void dispose () {
		spriteBatch.dispose();
		walkSheet.dispose();

	}

}
