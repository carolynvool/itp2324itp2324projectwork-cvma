package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;


public class GameScreen1 implements Screen {
    private final MazeRunnerGame game; // connects the MazeRunnerGame
    private final OrthographicCamera camera;

    private final Texture characterTexture;// Load this texture
    private Rectangle character; // her position on the screen; the woman

    //private Texture backdrop; // which texture is that; the background
    private Animation<TextureRegion> characterDownAnimation;
    private float characterX, characterY;
    private float stateTime;

    //private float sinusInput = 0f;
    //private float zoomLevel = 0.5f;

    private MapRenderer mapRenderer;

   // private int mapNumber;
    private SpriteBatch batch;
/*
    // Define textures for different elements
    private Texture wallTexture; // Make sure to load texture in the constructor
    private Texture entryPointTexture; // Load this texture
    private Texture exitTexture; // Load this texture
    private Texture trapTexture; // Load this texture
    private Texture enemyTexture; // Load this texture
    private Texture keyTexture; // Load this texture

    private HashMap<Integer, Texture> textureMapping;
*/
    private static final int TILE_SIZE = 16;
    private static final float SPEED = 200f;


    public GameScreen1(MazeRunnerGame game) { //, int mapNumber
        /*
        // Load textures
        wallTexture = new Texture(Gdx.files.internal("basictiles_1.png"));
        entryPointTexture = new Texture(Gdx.files.internal("basictiles_2.png"));
        exitTexture = new Texture(Gdx.files.internal("basictiles_3.png"));
        trapTexture = new Texture(Gdx.files.internal("basictiles_4.png"));
        enemyTexture = new Texture(Gdx.files.internal("basictiles_5.png"));
        keyTexture = new Texture(Gdx.files.internal("basictiles_6.png"));

        // Mapping between values in the properties files and their corresponding textures
        textureMapping = new HashMap<>();
        textureMapping.put(0, wallTexture);
        textureMapping.put(1, entryPointTexture);
        textureMapping.put(2, exitTexture);
        textureMapping.put(3, trapTexture);
        textureMapping.put(4, enemyTexture);
        textureMapping.put(5, keyTexture);

         */

        this.game = game;
        this.batch = new SpriteBatch(); // having created a separate batch for the gamescreen1 class so that the disposal of this onedoes not effect the actions on a batch in the maprenderer class

        // Use a default map number (e.g., 1) for now
        int defaultMapNumber = 1;

        // Initialize map renderer with the selected map number and textures
        //mapRenderer = new MapRenderer(mapNumber, this.batch, this);
        mapRenderer = new MapRenderer(defaultMapNumber, this.batch);

        game.setSelectedLevel(defaultMapNumber);
        //game.setSelectedLevel(mapNumber);

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.5f;
        this.characterX = 320; // initial x position for character
        this.characterY = 320; // initial y position for character

        // Get the font from the game's skin; the Textures objects and backdrop are loaded but where are they used after??????
        // for the positioning of the characters in the respective textured levels
        BitmapFont font = game.getSkin().getFont("font");
        characterTexture = new Texture(Gdx.files.internal("obesewomanbananafall.png"));
        character = new Rectangle();
        character.x = 800 / 2 - 64 / 2; // center the bucket horizontally
        character.y = 20; // bottom left corner of the bucket is 20 pixels above

        characterX = Gdx.graphics.getWidth() / 2f;
        characterY = Gdx.graphics.getHeight() / 2f;
        stateTime = 0f;
        this.characterDownAnimation = game.getCharacterDownAnimation();
    }

    /*
    private void renderElement(int x, int y, int value) { // used to render game elements related to the character
        // Render based on the interpreted value
        Texture texture = textureMapping.get(value);
        if (texture != null) {
            game.getSpriteBatch().draw(texture, x * TILE_SIZE, y * TILE_SIZE);

        }
    }

     */
    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.678f, 0.847f, 0.902f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;
        TextureRegion currentFrame = characterDownAnimation.getKeyFrame(stateTime, true);

        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        this.batch.begin();
        mapRenderer.render(this.batch);// to draw the map
       // renderElement((int) characterX / TILE_SIZE, (int) characterY / TILE_SIZE, 1); //to render game-specific elements

        this.batch.draw(currentFrame, characterX, characterY);// draw the character
        this.batch.end();
    }

    private void handleInput() {
        float speed = 200f; // Adjust the speed as needed

        float oldCharacterX = characterX;
        float oldCharacterY = characterY;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveCharacter(-SPEED * Gdx.graphics.getDeltaTime(), 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveCharacter(SPEED * Gdx.graphics.getDeltaTime(), 0);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveCharacter(0, -SPEED * Gdx.graphics.getDeltaTime());
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveCharacter(0, SPEED * Gdx.graphics.getDeltaTime());
        }

        // Check for collisions after moving in both X and Y directions
        if (!isValidMove(characterX, characterY)) {
            characterX = oldCharacterX;
            characterY = oldCharacterY;
        }

        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100 / Gdx.graphics.getWidth());

        // Clamp the character position within the map boundaries
        float halfViewportWidth = camera.viewportWidth * 0.5f * camera.zoom;
        float halfViewportHeight = camera.viewportHeight * 0.5f * camera.zoom;

        characterX = MathUtils.clamp(characterX, 0, Gdx.graphics.getWidth() - TILE_SIZE);
        characterY = MathUtils.clamp(characterY, 0, Gdx.graphics.getHeight() - TILE_SIZE);

        // Update camera position
        camera.position.set(characterX + TILE_SIZE / 2, characterY + TILE_SIZE / 2, 0);
        camera.update();
    }
    private void moveCharacter(float deltaX, float deltaY) {
        // Calculate new position
        float newX = characterX + deltaX;
        float newY = characterY + deltaY;

        // Check for collisions with walls or other elements
        if (isValidMove(newX, newY)) {
            characterX = newX;
            characterY = newY;
        }
    }
    private boolean isValidMove(float newX, float newY) {
        // Implement collision detection logic here
        // Check if the new position is within free spaces and not colliding with walls or other elements

        // Example: Check if the new position is within the map boundaries
        if (newX >= 0 && newX <= Gdx.graphics.getWidth() - TILE_SIZE &&
                newY >= 0 && newY <= Gdx.graphics.getHeight() - TILE_SIZE) {
            // Check if the new position collides with walls or other elements using mapRenderer or other logic
            int tileX = (int) (newX / TILE_SIZE);
            int tileY = (int) (newY / TILE_SIZE);

            int cellValue = mapRenderer.getMazeCellValue(tileX, tileY); // Implement this method in MapRenderer

            // Ensure the cell is a free space (you may need to adjust the condition based on your map elements)
            return cellValue != 0;
        }
        return true;
    }

    @Override
    public void show() {
        // Called when this screen becomes the current screen.
        // Initialize UI elements, resources, etc.
    }


    @Override
    public void resize(int width, int height) {
        // Update viewport, camera, or handle other resize-related tasks.
        camera.viewportWidth = 0.80f;
        camera.viewportHeight = 0.80f * height / width;
        camera.zoom = 0.75f;

       // camera.setToOrtho(false, width, height);
        camera.update();
    }

    @Override
    public void pause() {
        // Pause ongoing activities or save game state.
    }

    @Override
    public void resume() {
        // Resume paused activities or restore game state.

    }

    @Override
    public void hide() {
        // Dispose of resources, pause ongoing activities, etc.
        // Dispose of textures, sounds, and other assets.
    }
/*
    @Override
    public void dispose() {
        wallTexture.dispose();
        entryPointTexture.dispose();
        exitTexture.dispose();
        trapTexture.dispose();
        enemyTexture.dispose();
        keyTexture.dispose();
        this.batch.dispose();
    }

    public Texture getWallTexture() {
        return wallTexture;
    }

    public Texture getEntryPointTexture() {
        return entryPointTexture;
    }

    public Texture getExitTexture() {
        return exitTexture;
    }

    public Texture getTrapTexture() {
        return trapTexture;
    }

    public Texture getEnemyTexture() {
        return enemyTexture;
    }

    public Texture getKeyTexture() {
        return keyTexture;
    }

 */
public void dispose() {
    // Dispose of resources, textures, or anything that needs cleanup
    characterTexture.dispose();
    mapRenderer.dispose(); // Make sure to dispose of resources in MapRenderer if necessary
    // Dispose of any other resources used in this class
    batch.dispose();
}
}
