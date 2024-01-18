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


public class GameScreen1 implements Screen {
    private final MazeRunnerGame game; // connects the MazeRunnerGame
    private final OrthographicCamera camera;
    private final BitmapFont font; // for the positioning of the characters in the respective textured levels
    Rectangle obesewomanbananafall; // her position on the screen; the woman

    private Texture backdrop; // which texture is that; the background
    private Animation<TextureRegion> characterDownAnimation;
    private float characterX, characterY;
    private float stateTime;

    private float sinusInput = 0f;
    private float zoomLevel = 0.5f;

    private MapRenderer mapRenderer;

    private int mapNumber;
    private SpriteBatch batch;

    private TextureRegion[] tiles;

    // Constants for map values of the properties files provided - fix to the same givens as in artemis table
    private static final int WALL = 0;
    private static final int ENTRY_POINT = 1;
    private static final int EXIT = 2;
    private static final int FIXED_TRAP = 3;
    private static final int DYNAMIC_ENEMY = 4;
    private static final int KEY = 5; // still unsure what it is because I think there is only one thing with value of 5

    // Define textures for different elements
    private Texture wallTexture; // Make sure to load this texture in the constructor
    private Texture entryPointTexture; // Load this texture
    private Texture exitTexture; // Load this texture
    private Texture trapTexture; // Load this texture
    private Texture enemyTexture; // Load this texture
    private Texture keyTexture; // Load this texture

    public GameScreen1(MazeRunnerGame game, int mapNumber){
        // Load additional textures
        wallTexture = new Texture(Gdx.files.internal("basictiles_1.png"));
        entryPointTexture = new Texture(Gdx.files.internal("basictiles_2.png"));
        exitTexture = new Texture(Gdx.files.internal("basictiles_3.png"));
        trapTexture = new Texture(Gdx.files.internal("basictiles_4.png"));
        enemyTexture = new Texture(Gdx.files.internal("basictiles_5.png"));
        keyTexture = new Texture(Gdx.files.internal("basictiles_6.png"));

        this.game = game;
        game.setSelectedLevel(mapNumber);

        batch = game.getSpriteBatch();


        // Initialize map renderer with the selected map number
        mapRenderer = new MapRenderer(mapNumber);

        characterX = Gdx.graphics.getWidth() / 2f;
        characterY = Gdx.graphics.getHeight() / 2f;
        stateTime = 0f;

        this.characterDownAnimation = game.getCharacterDownAnimation();

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1.5f;
        this.characterX = 320; // initial x position for character
        this.characterY = 320; // intial y position for character


        // Get the font from the game's skin; the Textures objects and backdrop are loaded but where are they used after??????
        font = game.getSkin().getFont("font");
        // objects = new Texture(Gdx.files.internal("obesewomanbananafall.png"));
        //backdrop = new Texture(Gdx.files.internal("basictiles.png")); // BASIC TILES HAVE NOT BEEN USED!!!!!!!
        obesewomanbananafall = new Rectangle();
        obesewomanbananafall.x = 800 / 2 - 64 / 2; // center the bucket horizontally
        obesewomanbananafall.y = 20; // bottom left corner of the bucket is 20 pixels above
    }
    private void renderElement(int x, int y, int value) {
        // Render based on the interpreted value
        switch (value) {
            case WALL:
                renderWall(x, y);
                break;
            case ENTRY_POINT:
                renderEntryPoint(x, y);
                break;
            case EXIT:
                renderExit(x, y);
                break;
            case FIXED_TRAP:
                renderFixedTrap(x, y);
                break;
            case DYNAMIC_ENEMY:
                renderDynamicEnemy(x, y);
                break;
            case KEY:
                renderKey(x, y);
                break;
        }
    }
    private void renderWall(int x, int y) {
        game.getSpriteBatch().draw(wallTexture, x * 40, y * 40);
    }

    private void renderEntryPoint(int x, int y) {
        game.getSpriteBatch().draw(entryPointTexture, x * 40, y * 40);
    }

    private void renderExit(int x, int y) {
        game.getSpriteBatch().draw(exitTexture, x * 40, y * 40);
    }

    private void renderFixedTrap(int x, int y) {
        game.getSpriteBatch().draw(trapTexture, x * 40, y * 40);
    }

    private void renderDynamicEnemy(int x, int y) {
        game.getSpriteBatch().draw(enemyTexture, x * 40, y * 40);
    }

    private void renderKey(int x, int y) {
        game.getSpriteBatch().draw(keyTexture, x * 40, y * 40);
    }

    private void handleInput() {
        float speed = 200f; // Adjust the speed as needed

        float oldCharacterX = characterX;
        float oldCharacterY = characterY;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            //camera.translate(-3, 0);
            characterX -= speed * Gdx.graphics.getDeltaTime();

        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
           // camera.translate(3, 0);
            characterX += speed * Gdx.graphics.getDeltaTime();

        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
           // camera.translate(0, -3);
            characterY -= speed * Gdx.graphics.getDeltaTime();

        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            //camera.translate(0, 3);
            characterY += speed * Gdx.graphics.getDeltaTime();
            if (characterX < 0 || characterX > Gdx.graphics.getWidth() - 16) {
                characterX = oldCharacterX;
            }
            if (characterY < 0 || characterY >  Gdx.graphics.getHeight()  - 16) {
                characterY = oldCharacterY;
            }
        }
        camera.zoom = MathUtils.clamp(camera.zoom, 0.1f, 100 / Gdx.graphics.getWidth());

        // Clamp the character position within the map boundaries
        float halfViewportWidth = camera.viewportWidth * 0.5f * camera.zoom;
        float halfViewportHeight = camera.viewportHeight * 0.5f * camera.zoom;
        float maxX = Gdx.graphics.getWidth() - halfViewportWidth;
        float maxY = Gdx.graphics.getHeight() - halfViewportHeight;

        characterX = MathUtils.clamp(characterX, 0, Gdx.graphics.getWidth() - 16);
        characterY = MathUtils.clamp(characterY, 0, Gdx.graphics.getHeight()  - 16);

        // Update camera position
        camera.position.set(characterX + 8, characterY + 8, 0);
        camera.update();
    }
    @Override
    public void show() {
        // Called when this screen becomes the current screen.
        // Initialize UI elements, resources, etc.
        mapRenderer = new MapRenderer(game.getSelectedLevel());

    }

    @Override
    public void render(float delta) {

        handleInput();

        Gdx.gl.glClearColor(0.678f, 0.847f, 0.902f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stateTime += delta;
        TextureRegion currentFrame = characterDownAnimation.getKeyFrame(stateTime, true);

        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin();
        game.getSpriteBatch().draw(currentFrame, characterX, characterY);// draw the character
        mapRenderer.render(game.getSpriteBatch());// to draw the map
        game.getSpriteBatch().end();
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
        batch.dispose();
    }

    @Override
    public void dispose() {
        wallTexture.dispose();
        entryPointTexture.dispose();
        exitTexture.dispose();
        trapTexture.dispose();
        enemyTexture.dispose();
        keyTexture.dispose();
    }
}
