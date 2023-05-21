package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class GameOverScreen implements Screen {

    private final Game game;

    public GameOverScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Aquí puedes realizar cualquier inicialización necesaria para la pantalla de Game Over
    }

    @Override
    public void render(float delta) {
        // Aquí puedes realizar la lógica de renderizado de la pantalla de Game Over
    }

    @Override
    public void resize(int width, int height) {
        // Aquí puedes realizar cualquier ajuste necesario cuando se cambia el tamaño de la pantalla
    }

    @Override
    public void pause() {
        // Aquí puedes realizar cualquier acción necesaria cuando se pausa la pantalla
    }

    @Override
    public void resume() {
        // Aquí puedes realizar cualquier acción necesaria cuando se reanuda la pantalla
    }

    @Override
    public void hide() {
        // Aquí puedes realizar cualquier acción necesaria cuando la pantalla se oculta
    }

    @Override
    public void dispose() {
        // Aquí puedes liberar cualquier recurso que hayas utilizado en la pantalla de Game Over
    }
}