package de.tum.cit.ase.maze;
// his GamePanel
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.badlogic.gdx.graphics.g3d.particles.ParticleShader.Setters.screenWidth;

/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {
    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private Texture objects;
    Rectangle obesewomanbananafall;
    Texture backdrop;
    Array<Rectangle> gifofthewoman;
    SpriteBatch batch;

    private float sinusInput = 0f;
    /*
    private static final int MAP_WIDTH = 26;
    private static final int MAP_HEIGHT = 20;
    private static final String PROMPT_TEXT = "Click anywhere to generate a new map";
    private static final Color PROMPT_COLOR = Color.CORAL;
    private static final float PROMPT_FADE_IN = 2f;
    private static final float PROMPT_FADE_OUT = 4f;

     */
    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;


        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");
        objects = new Texture(Gdx.files.internal("obesewomanbananafall.png"));
        backdrop = new Texture(Gdx.files.internal("basictiles.png"));
        obesewomanbananafall = new Rectangle();
        obesewomanbananafall.x = 800 / 2 - 64 / 2; // center the bucket horizontally
        obesewomanbananafall.y = 20; // bottom left corner of the bucket is 20 pixels above
        // the bottom screen edge

    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.goToGame();
        }

        ScreenUtils.clear(0, 0, 0.5f, 1); // Clear the screen

        Texture backdrop = new Texture(Gdx.files.internal("beachbackgroundscreen.jpg"));
        camera.update(); // Update the camera
        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin(); // Important to call this before drawing anything
        // draw the background
        game.getSpriteBatch().draw(backdrop, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Render the text
        font.draw(game.getSpriteBatch(), "Press ESC to go to menu or press SPACE to start game", textX, textY);
        // Draw the character next to the text :) / We can reuse sinusInput here
        game.getSpriteBatch().draw(
                game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                textX - 96,
                textY - 64,
                64,
                128);


        game.getSpriteBatch().end(); // Important to call this after drawing everything

    }

    private void handleInput() {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(3, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0, -3);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0, 3);
        }
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100/camera.viewportWidth);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
        camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
    }
/* .clamp ( value : Float, min : Float, max : Float ) : Float
value — Value to be clamped, hence to limit this value to a range between the provided min and max
min — Minimum value.
max — Maximum value.
 */
@Override
public void resize(int width, int height) { // Called when the screen is resized.
    // Update viewport, camera, or handle other resize-related tasks.
    camera.viewportWidth = 0.80f;
    camera.viewportHeight = 0.80f * height/width;
    camera.update();
    camera.setToOrtho(false);
}
    @Override
    public void pause() { // Called when the game is paused.
        // Pause ongoing activities or save game state.
    }

    @Override
    public void resume() { // Called when the game is resumed.
        // Resume paused activities or restore game state.
    }

    @Override
    public void show() {
        // Called when this screen becomes the current screen.
        // Initialize UI elements, resources, etc.
    }

    @Override
    public void hide() { // Called when this screen is no longer the current screen.
        // Dispose of resources, pause ongoing activities, etc.
    }

    @Override
    public void dispose() { // Called when the screen should release all resources.
        // Dispose of textures, sounds, and other assets.
        backdrop.dispose();
    }

    // Additional methods and logic can be added as needed for the game screen
}

    // Additional methods and logic can be added as needed for the game screen

