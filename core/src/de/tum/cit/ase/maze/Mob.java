package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.awt.*;


public class Mob {
    private Texture mobTexture;
    private float x, y;
    private float width, height;
    private float speed;
    private Rectangle bounds;
    private int[][] collisionMap;
    public Mob(float x, float y) {
        this.bounds = new Rectangle(x, y, width, height);
        mobTexture = new Texture(Gdx.files.internal("frog_mob.png"));
        this.width = 16;
        this.height = 16;
        this.speed = 200;
        this.collisionMap = collisionMap;

        }
        public void update(float delta){
        // TODO implement logic on how to move mobs
            move();
        }
        private void move(){
            // Move up until the top boundary of the rectangle is reached
            if (bounds.y + bounds.height + speed * Gdx.graphics.getDeltaTime() < 16 * 16 && !checkCollision(bounds.x, bounds.y + speed * Gdx.graphics.getDeltaTime())) {
                bounds.y += speed * Gdx.graphics.getDeltaTime();
            }

            // Move down until the bottom boundary of the rectangle is reached
            if (bounds.y - speed * Gdx.graphics.getDeltaTime() > 0 && !checkCollision(bounds.x, bounds.y - speed * Gdx.graphics.getDeltaTime())) {
                bounds.y -= speed * Gdx.graphics.getDeltaTime();
            }

            // Move right until the right boundary of the rectangle is reached
            if (bounds.x + bounds.width + speed * Gdx.graphics.getDeltaTime() < 16 * 16 && !checkCollision(bounds.x + speed * Gdx.graphics.getDeltaTime(), bounds.y)) {
                bounds.x += speed * Gdx.graphics.getDeltaTime();
            }

            // Move left until the left boundary of the rectangle is reached
            if (bounds.x - speed * Gdx.graphics.getDeltaTime() > 0 && !checkCollision(bounds.x - speed * Gdx.graphics.getDeltaTime(), bounds.y)) {
                bounds.x -= speed * Gdx.graphics.getDeltaTime();
            }

        }
        private boolean checkCollision(float newX, float newY){
        int tileX = (int) (newX / 16);
        int tileY = (int) (newY / 16);

            // Ensure the indices are within the bounds of the collision map
            if (tileX < 0 || tileX >= collisionMap.length || tileY < 0 || tileY >= collisionMap[0].length) {
                return false;
            }

            // Check if the new position collides with a wallTexture tile
            return collisionMap[tileX][tileY] == 1;
        }

        public void render(SpriteBatch batch){
            batch.draw(mobTexture, bounds.x, bounds.y, bounds.width, bounds.height);
        }
        public Rectangle getBounds(){
        // TODO return bounding box of the mob for collision
            return new Rectangle();
        }
}

// when adding mobs into gamescreen class, private List<Mob> mobs; with show method for
// public void show() {
//        character = new Character(/* pass your sound instance here */);
//        mobs = new ArrayList<>();
//        // Initialize and add mobs to the list
//        mobs.add(new Mob(100, 100, 50, 50)); // Example: Adjust position and dimensions accordingly
//    } and in render method
// // Update and render mobs
//        for (Mob mob : mobs) {
//            mob.update(delta);
//            mob.render(batch);
//        }