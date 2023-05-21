package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemigo {
    private Vector2 posicion;
    private Vector2 velocidad;
    private Rectangle boundingBox;
    private Texture sprite;

    public Enemigo(float x, float y, float velocidadX, float velocidadY, Texture sprite) {
        posicion = new Vector2(x, y);
        velocidad = new Vector2(velocidadX, velocidadY);
        this.sprite = sprite;
        this.boundingBox = new Rectangle(x, y, 50, 50); // Ajusta la anchura y altura de la bala

    }

    public void update(float delta) {
        // Actualiza la posición del enemigo basado en la velocidad y el tiempo transcurrido
        posicion.add(velocidad.x * delta, velocidad.y * delta);
        boundingBox.setPosition(posicion);

    }

    public void render(SpriteBatch batch) {
        batch.draw(sprite, posicion.x, posicion.y,100,100);
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    public void setPosicion(Vector2 posicion) {
        this.posicion = posicion;
    }

    public Vector2 getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(Vector2 velocidad) {
        this.velocidad = velocidad;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

// Resto de los métodos y funcionalidad del enemigo
}