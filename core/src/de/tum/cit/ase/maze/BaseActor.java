package de.tum.cit.ase.maze;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class BaseActor extends Group {
    private Animation<TextureRegion> animation;
    private float elapsedTime;
    private boolean animationPaused;
    private Vector2 velocityVec;
    private Vector2 accelerationVec;
    private float acceleration;
    private float maxSpeed;
    private float deceleration;
    private Polygon boundaryPolygon;

    // stores size of game world for all actors
    private static Rectangle worldBounds;

    public BaseActor(float x, float y) {
        // call constructor from Actor class
        super();

        // perform additional initialization tasks
        setPosition(x, y);

        // initialize animation data
        animation = null;
        elapsedTime = 0;
        animationPaused = false;

        // initialize physics data
        velocityVec = new Vector2(0, 0);
        accelerationVec = new Vector2(0, 0);
        acceleration = 0;
        maxSpeed = 1000;
        deceleration = 0;

        boundaryPolygon = null;
    }
    public void wrapAroundWorld() { // if character moves out of boundaries, it returns it to the other side of the world
        if (getX() + getWidth() < 0)
            setX(worldBounds.width);

        if (getX() > worldBounds.width)
            setX(-getWidth());

        if (getY() + getHeight() < 0)
            setY(worldBounds.height);

        if (getY() > worldBounds.height)
            setY(-getHeight());
    }
    public void centerAtPosition(float x, float y) { // align center of actor at given position coordinates
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }
    public void centerAtActor(BaseActor other) { // repositions this baseactor so its center is alligned with center of other baseactor.
        // useful when one baseactor spawns another
        centerAtPosition(other.getX() + other.getWidth() / 2, other.getY() + other.getHeight() / 2);
    }
    public void setAnimation(Animation<TextureRegion> animation) { // animation used when rendering this actor, also sets actor size
        this.animation = animation;
        TextureRegion textureRegion = this.animation.getKeyFrame(0);
        float regionWidth = textureRegion.getRegionWidth();
        float regionHeight = textureRegion.getRegionHeight();
        setSize(regionWidth, regionHeight);
        setOrigin(regionWidth / 2, regionHeight / 2);

        if (boundaryPolygon == null)
            setBoundaryRectangle();
    }
    public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration, boolean loop) { // creates an animation from images stored in
        // separate files
        int fileCount = fileNames.length;
        Array<TextureRegion> textureArray = new Array<>();

        for (int n = 0; n < fileCount; n++) {
            String fileName = fileNames[n];
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }

        Animation<TextureRegion> animation = new Animation<>(frameDuration, textureArray);
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        if (loop)
            animation.setPlayMode(Animation.PlayMode.LOOP);

        if (this.animation == null)
            setAnimation(animation);

        return animation;

    }
    public void setAnimationPaused(boolean pause) {
        animationPaused = pause;
    }

    // creates an animation from a sprite sheet: a rectangular grid of images stored in a single file
    public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols, float frameDuration, boolean loop) {
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        Array<TextureRegion> textureArray = new Array<>();

        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                textureArray.add(temp[r][c]);

        Animation<TextureRegion> animation = new Animation<>(frameDuration, textureArray);
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        if (loop)
            animation.setPlayMode(Animation.PlayMode.LOOP);

        if (this.animation == null)
            setAnimation(animation);

        return animation;
    }
    // creating a 1-frame animation from a single texture
    public Animation<TextureRegion> loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        return loadAnimationFromFiles(fileNames, 1, true);
    }
// sets opacity for the actor, idk maybe useful or cool
    public void setOpacity(float opacity) {
        this.getColor().a = opacity;
    }
    // set acceleration
    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }
    // set deceleration
    public void setDeceleration(float deceleration) {
        this.deceleration = deceleration;
    }
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    // determines if object is moving
    public boolean isMoving() {
        return (getSpeed() > 0);
    }
    public float getSpeed() {
        return velocityVec.len();
    }
    public void applyPhysics(float deltaTime) { // cool method to try if we happen to get the character moving
        // apply acceleration
        velocityVec.add(accelerationVec.x * deltaTime, accelerationVec.y * deltaTime);

        float speed = getSpeed();

        // decrease speed (decelerate) when not accelerating
        if (accelerationVec.len() == 0)
            speed -= deceleration * deltaTime;

        // keep speed within set bounds
        speed = MathUtils.clamp(speed, 0, maxSpeed);

        // update velocity
        setSpeed(speed);

        // update position according to value stored in velocity vector
        moveBy(velocityVec.x * deltaTime, velocityVec.y * deltaTime);

        // reset acceleration
        accelerationVec.set(0, 0);
    }
    public void setSpeed(float speed) {
        // if length is zero, then assume motion angle is zero degrees
        if (velocityVec.len() == 0)
            velocityVec.set(speed, 0);
        else
            velocityVec.setLength(speed);
    }
    public void setMotionAngle(float angle) {
        velocityVec.setAngleDeg(angle);
    }
    public float getMotionAngle() {
        return velocityVec.angleDeg();
    }
    public void accelerateAtAngle(float angle) {
        accelerationVec.add(
                new Vector2(acceleration, 0).setAngleDeg(angle));
    }
    public void accelerateForward() {
        accelerateAtAngle(getRotation());
    }

    // set rectangle - automatically called when animation is set, provided that the current boundary is null
    public void setBoundaryRectangle() {
        float w = getWidth();
        float h = getHeight();

        float[] vertices = {0, 0, w, 0, w, h, 0, h};
        boundaryPolygon = new Polygon(vertices);
    }
    // replace default rectangle with an n sided polygon, vertices of polygon lie on the ellipse contained within bounding rectangle
    // not sure whether to use it its mad complicated
    public void setBoundaryPolygon(int numSides) {
        float w = getWidth();
        float h = getHeight();

        float[] vertices = new float[2 * numSides];
        for (int i = 0; i < numSides; i++) {
            float angle = i * 6.28f / numSides;
            // x-coordinate
            vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2;
            // y-coordinate
            vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2;
        }
        boundaryPolygon = new Polygon(vertices);

    }
    public Polygon getBoundaryPolygon() {
        boundaryPolygon.setPosition(getX(), getY());
        boundaryPolygon.setOrigin(getOriginX(), getOriginY());
        boundaryPolygon.setRotation(getRotation());
        boundaryPolygon.setScale(getScaleX(), getScaleY());
        return boundaryPolygon;
    }
    // determine if this baseactor overlaps with other baseactor according to collision polygons
    public boolean overlaps(BaseActor other, Sound collisinSound) {
        boolean result = false;
        Polygon currentPoly = this.getBoundaryPolygon();
        Polygon otherPoly = other.getBoundaryPolygon();

        // initial test to improve performance
        if (currentPoly.getBoundingRectangle().overlaps(otherPoly.getBoundingRectangle())) {
            result = Intersector.overlapConvexPolygons(currentPoly, otherPoly);
        }

        return result;
    }
    // set world dimensions for use by methods
    public static void setWorldBounds(float width, float height) {
        worldBounds = new Rectangle(0, 0, width, height);
    }
    // get world dimensions
    public static Rectangle getWorldBounds() {
        return worldBounds;
    }
    // if an edge of an object moves past the world bounds, adjust its position to keep it completely on screem
    public void boundToWorld() {
        if (getX() < 0)
            setX(0);
        if (getX() + getWidth() > worldBounds.width)
            setX(worldBounds.width - getWidth());
        if (getY() < 0)
            setY(0);
        if (getY() + getHeight() > worldBounds.height)
            setY(worldBounds.height - getHeight());
    }
    // CENTER CAMERA ON THIS OBJECT
    public void alignCamera() {
        Camera cam = this.getStage().getCamera();
        Viewport v = this.getStage().getViewport();

        // center camera on actor
        cam.position.set(this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0);

        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x, cam.viewportWidth / 2, worldBounds.width - cam.viewportWidth / 2);
        cam.position.y = MathUtils.clamp(cam.position.y, cam.viewportHeight / 2, worldBounds.height - cam.viewportHeight / 2);
        cam.update();
    }
    // draws current frame of animation; automaticallu called by draw method in Stage class
    // if no color has been set, image will be tinted by that color
    // if no animation has been set or object is invisible, nothing will be drawn
    public void draw(Batch batch, float parentAlpha) {

        // apply color tint effect
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);

        if (animation != null && isVisible())
            batch.draw(animation.getKeyFrame(elapsedTime),
                    getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

        super.draw(batch, parentAlpha);
    }

}
