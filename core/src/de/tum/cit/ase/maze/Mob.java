package de.tum.cit.ase.maze;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Mob extends BaseActor {
    private Vector2 velocity;
    private Rectangle movementRectangle;

    public Mob(float x, float y, Rectangle movementRectangle) {
        super(x, y);
        this.movementRectangle = movementRectangle;
        loadAnimationFromSheet("sumohulk.png", 9, 6, 0.2f, true);
        setOpacity(1f);

        velocity = new Vector2();
        setRandomDirection();
    }

    public void act(float delta) {
        super.act(delta);

        updateMovement(delta);
        keepInBounds();
    }

    private void updateMovement(float delta) {
        float speed = 50f; // Adjust the speed as needed

        // Update position based on velocity and speed
        float newX = getX() + velocity.x * speed * delta;
        float newY = getY() + velocity.y * speed * delta;

        setPosition(newX, newY);
    }

    private void setRandomDirection() {
        float randomAngle = MathUtils.random(0, 360);
        velocity.set(MathUtils.cosDeg(randomAngle), MathUtils.sinDeg(randomAngle)).nor();
    }

    private void keepInBounds() {
        float mobX = getX();
        float mobY = getY();

        // Clamp position within the allowed rectangle
        mobX = MathUtils.clamp(mobX, movementRectangle.x, movementRectangle.x + movementRectangle.width - getWidth());
        mobY = MathUtils.clamp(mobY, movementRectangle.y, movementRectangle.y + movementRectangle.height - getHeight());

        setPosition(mobX, mobY);

        // If the mob hits the boundary, set a new random direction
        if (mobX == movementRectangle.x || mobX == movementRectangle.x + movementRectangle.width - getWidth() ||
                mobY == movementRectangle.y || mobY == movementRectangle.y + movementRectangle.height - getHeight()) {
            setRandomDirection();
        }
    }
}
