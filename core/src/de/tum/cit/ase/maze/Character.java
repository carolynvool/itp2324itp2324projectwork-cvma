package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Character extends Sprite {
    private Vector2 velocity = new Vector2(); // the movement velocity
    private float speed = 60 * 2;
    TextureRegion womanStand;
    private TextureRegion womanToTheRight;
    private TextureRegion womanToTheLeft;
    private TextureRegion womanGoUp;
    private TextureRegion womanGoDown;
    private TextureRegion currentTexture;
    float actorX, actorY;
    private Sound collisionSound;
    private boolean isColliding;
    private int[][] map;
    private float getSpeed = 100f;
    public Character(Sprite sprite) {
        super(sprite);
        this.map = map;
        Texture texturesheet = new Texture(Gdx.files.internal("obesewomandoingcartweels.png"));
        TextureRegion[][] cartwheelMove = TextureRegion.split(texturesheet, 4, 5);
        womanStand = cartwheelMove[1][1];
        womanToTheRight = cartwheelMove[3][2];
        womanToTheLeft = cartwheelMove[1][3];
        womanGoUp = cartwheelMove[0][0];
        womanGoDown = cartwheelMove[0][4];
        currentTexture = womanStand;
        collisionSound = Gdx.audio.newSound(Gdx.files.internal("fart-with-reverb.mp3"));
    }

    @Override
    public void draw(Batch batch) {
        update(Gdx.graphics.getDeltaTime());
        super.draw(batch);
    }

    public void update(float delta) {
        if(velocity.y > speed)
            velocity.y = speed;
        else if (velocity.y < speed)
            velocity.y = -speed;

        setX(getX() + velocity.x * delta);
        setY(getY() + velocity.y * delta);
    }

    public TextureRegion getWomanStand() {
        return womanStand;
    }
}
// in order to call methods and update it in the main gamescreen class, call in render method public void render(float delta) {
//        // Update and render the character
//        character.update(delta);
//        batch.begin();
//        character.render(batch);
//        batch.end();
//    }