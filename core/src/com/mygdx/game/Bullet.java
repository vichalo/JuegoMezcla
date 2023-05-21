package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.List;


public class Bullet {
    private Texture texture;
    private Vector2 startPosition;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle boundingBox;
    boolean markedForRemoval = false;

    public Bullet(Texture texture,float x, float y, float speedX, float speedY) {
        this.texture = texture;
        this.startPosition = new Vector2(x, y);
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(speedX, speedY);
        this.boundingBox = new Rectangle(x, y, 20, 20); // Ajusta la anchura y altura de la bala
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        boundingBox.setPosition(position);
        if (position.x > startPosition.x+200 || position.y > startPosition.y+200 || position.x < startPosition.x-200 || position.y < startPosition.y-200) {
            markedForRemoval = true;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public Vector2 getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Vector2 startPosition) {
        this.startPosition = startPosition;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
    }

    public boolean isMarkedForRemoval() {

        return markedForRemoval;
    }
    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}