package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import static com.badlogic.gdx.Gdx.input;

public class Woman extends BaseActor {
    Animation left;
    Animation right;
    Animation up;
    Animation down;

    public Woman(float x, float y) {
        super(x, y);
        String fileName = "obesewomandoingcartweels.png";
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        int rows = 5;
        int cols = 4;
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        Array<TextureRegion> textureArray = new Array<>();
        float frameDuration = 0.2f;

        for (int c = 0; c < cols; c++)
            textureArray.add(temp[0][c]);
        down = new Animation(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[1][c]);
        left = new Animation(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[2][c]);
        right = new Animation(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[3][c]);
        up = new Animation(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        setAnimation(down);
        // set after animation established
        setBoundaryPolygon(8);
        setAcceleration(1);
        setSpeed(1);
        setDeceleration(1);
    }

    public void act(float deltaTime) {
        super.act(deltaTime);

        // woman movement controls
        if (input.isKeyPressed(Input.Keys.LEFT)) accelerateAtAngle(180);
        if (input.isKeyPressed(Input.Keys.RIGHT)) accelerateAtAngle(0);
        if (input.isKeyPressed(Input.Keys.UP)) accelerateAtAngle(90);
        if (input.isKeyPressed(Input.Keys.DOWN)) accelerateAtAngle(270);

        // pause animation when the woman is not moving
        if (getSpeed() == 0) {
            setAnimationPaused(true);
        } else {
            setAnimationPaused(false);
        }
        // set direction animation
        float angle = getMotionAngle();
        if (angle >= 45 && angle <= 135) {
            setAnimation(up);
        } else if (angle > 135 && angle < 225) {
            setAnimation(left); // west
        } else if (angle >= 225 && angle <= 315) {
            setAnimation(down);
        } else {
            setAnimation(right); // east
        }
        applyPhysics(deltaTime);
    }
}
