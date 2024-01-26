package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Character extends Actor {
    private TextureRegion womanStand;
    private TextureRegion womanToTheRight;
    private TextureRegion womanToTheLeft;
    private TextureRegion womanGoUp;
    private TextureRegion womanGoDown;
    private TextureRegion currentTexture;
    float actorX, actorY = 0;
    private Sound collisionSound;
    private boolean isColliding;
    public Character() {
        Texture texturesheet = new Texture(Gdx.files.internal("obesewomandoingcartwheels.png"));
        TextureRegion[][] cartwheelMove = TextureRegion.split(texturesheet, 4, 5);
        womanStand = cartwheelMove[1][1];
        womanToTheRight = cartwheelMove[3][2];
        womanToTheLeft = cartwheelMove[1][3];
        womanGoUp = cartwheelMove[0][0];
        womanGoDown = cartwheelMove[0][4];
        currentTexture = womanStand;
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("fart-with-reverb.mp3"));
    }
    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            currentTexture = womanToTheRight;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            currentTexture = womanToTheLeft;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            currentTexture = womanGoUp;
        }else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            currentTexture = womanGoDown;
        }
        if (checkCollisionWithMobs()){
            if (!isColliding){
                collisionSound.play();
                isColliding = true;
                // remove a heart
            }
        }
    }
private boolean checkCollisionWithMobs(){
        return false;
}
public void render(SpriteBatch batch){
        batch.draw(currentTexture, actorX, actorY, 16, 16);
    }
}
// in order to call methods and update it in the main gamescreen class, call in render method public void render(float delta) {
//        // Update and render the character
//        character.update(delta);
//        batch.begin();
//        character.render(batch);
//        batch.end();
//    }