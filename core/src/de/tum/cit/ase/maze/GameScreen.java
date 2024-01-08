package de.tum.cit.ase.maze;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashMap;
import java.util.Properties;

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
    private Texture backdrop;
    Array<Rectangle> gifofthewoman;
    private java.util.Map<Integer, Properties> mapPropertiesByLevel;  // Map to store map properties for each level;
    // written like that because I have created the class Map, and they have the same name dumb, but I can't start from the beginning
    private float sinusInput = 0f;

    // Constants for map values of the properties files provided
    private static final int WALL = 0;
    private static final int WALKABLE_AREA = 1;
    private static final int KEY_TO_COLLECT = 2;
    private static final int FIXED_TRAP = 3;
    private static final int DYNAMIC_ENEMY = 4;
    private static final int CACTUS = 5; // still unsure what it is because I think there is only one thing with value of 5


    // Loading our different textures
    private Texture wallTexture;
    private Texture walkableAreaTexture;
    private Texture keyTexture;
    private Texture trapTexture;
    private Texture enemyTexture;
    private Texture cactusTexture;


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;

        // Load map properties for all levels
        mapPropertiesByLevel = new HashMap<Integer, Properties>();
        for (int level = 1; level <= 5; level++) {
            String filePath = "maps/level-" + level + ".properties";
            Properties properties = Map.loadMapProperties(filePath);
            mapPropertiesByLevel.put(level, properties);
        }

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

        // Initializing our textures with what we have downloaded from the internet
        wallTexture = new Texture(Gdx.files.internal("assets/wall.png"));
        walkableAreaTexture = new Texture(Gdx.files.internal("assets/walkable_area.jpg"));
        keyTexture = new Texture(Gdx.files.internal("assets/key.png"));
        trapTexture = new Texture(Gdx.files.internal("assets/chain_trap.png"));
        enemyTexture = new Texture(Gdx.files.internal("assets/dynamo.png"));
        cactusTexture = new Texture(Gdx.files.internal("assets/cactus.png"));

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

        ScreenUtils.clear(0, 0, 0.2f, 1); // Clear the screen

        camera.update(); // Update the camera

        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);
        //handleInput();

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);
        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        //!!!!!!!!!!!!!!
        // Iterate through levels and render elements based on properties
        for (int level = 1; level <= 5; level++) {
            Properties properties = mapPropertiesByLevel.get(level);

            // Iterate through properties and render elements
            for (Object key : properties.keySet()) {
                String coordinate = (String) key;
                int value = Integer.parseInt(properties.getProperty(coordinate));

                // Extract x and y coordinates from the string
                String[] coordinates = coordinate.split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);

                // Render the element based on the value
                renderElement(x, y, value);
            }
        }
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

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

@Override
public void resize(int width, int height) { // Called when the screen is resized.
    // Update viewport, camera, or handle other resize-related tasks.
    camera.viewportWidth = 0.80f;
    camera.viewportHeight = 0.80f * height/width;
    camera.zoom = 0.75f;
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

        wallTexture.dispose();
        walkableAreaTexture.dispose();
        keyTexture.dispose();
        trapTexture.dispose();
        enemyTexture.dispose();
        cactusTexture.dispose();

    }

    // Additional methods and logic can be added as needed for the game screen

    private void renderElement(int x, int y, int value) {
        // Render based on the interpreted value
        switch (value) {
            case WALL:
                renderWall(x, y);
                break;
            case WALKABLE_AREA:
                renderWalkableArea(x, y);
                break;
            case KEY_TO_COLLECT:
                renderKeyToCollect(x, y);
                break;
            case FIXED_TRAP:
                renderFixedTrap(x, y);
                break;
            case DYNAMIC_ENEMY:
                renderDynamicEnemy(x, y);
                break;
            case CACTUS:
                renderSomethingElse(x, y);
                break;
        }
    }

    private void renderWall(int x, int y) {
        // Your rendering logic for a wall element at coordinates (x, y)
        // Example: render a texture or shape at the specified coordinates
        game.getSpriteBatch().draw(wallTexture, x * 40, y * 40);

    } private void renderWalkableArea(int x, int y) {
        // Render walkable area logic
        game.getSpriteBatch().draw(walkableAreaTexture, x * 40, y * 40);
    }

    private void renderKeyToCollect(int x, int y) {
        // Render key to collect logic
        game.getSpriteBatch().draw(keyTexture, x * 40, y * 40);
    }

    private void renderFixedTrap(int x, int y) {
        // Render fixed trap logic
        game.getSpriteBatch().draw(trapTexture, x * 40, y * 40);
    }

    private void renderDynamicEnemy(int x, int y) {
        // Render dynamic enemy logic
        game.getSpriteBatch().draw(enemyTexture, x * 40, y * 40);
    }

    private void renderSomethingElse(int x, int y) {
        // Render something else logic
        game.getSpriteBatch().draw(cactusTexture, x * 40, y * 40);
    }
}



