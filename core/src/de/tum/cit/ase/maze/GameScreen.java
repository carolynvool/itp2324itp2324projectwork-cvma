package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Map;
/**
 * The GameScreen class is responsible for rendering the gameplay screen.
 * It handles the game logic and rendering of the game elements.
 */
public class GameScreen implements Screen {

    private final MazeRunnerGame game;
    private final OrthographicCamera camera;
    private final BitmapFont font;
    private float sinusInput = 0f;
    private final Texture characterTexture;
    private Rectangle character;
    private Animation<TextureRegion> characterdownanimation;
    private float characterX, characterY;
    private SpriteBatch batch;
    private TextureRegion[] tileRegions;
    private TextureRegion wallTexture;
    private TextureRegion entryPointTexture;
    private TextureRegion exitTexture;
    private TextureRegion trapTexture;
    private TextureRegion enemyTexture;
    private TextureRegion keyTexture;
    private Map<MapRenderer.MapCoordinates, Integer> mapData;
    private int value = 0;
    private int[][] arrayData;


    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game) {
        this.game = game;
        mapData = MapRenderer.readMapFile("maps/level-1.properties");
        arrayData = MapRenderer.convertToArrays(mapData);
        Texture tileSheet = new Texture(Gdx.files.internal("basictiles.png"));
        Texture tileSheet2 = new Texture(Gdx.files.internal("objects.png"));
        Texture tileSheet3 = new Texture(Gdx.files.internal("mobs.png"));

        TextureRegion[][] region = TextureRegion.split(tileSheet, 16,16);
        TextureRegion[][] region2 = TextureRegion.split(tileSheet3, 16,16);

        wallTexture = region[0][1];
        entryPointTexture = region[6][0];
        exitTexture = region[6][2];
        trapTexture = region2[0][0];
        enemyTexture = region2[4][0];
        keyTexture = region2[0][3];

        this.batch = new SpriteBatch();

        // Create and configure the camera for the game view
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        camera.zoom = 0.75f;
        camera.position.set(arrayData.length * 16 / 2f, arrayData[0].length * 16 / 2f, 0);

        font = game.getSkin().getFont("font"); // Get the font from the game's skin

        characterTexture = new Texture(Gdx.files.internal("obesewomanbananafall.png"));
        character = new Rectangle();
        character.x = 800 / 2 - 64 / 2; // centering bucket horisontally
        character.y = 20; // bottom left corner of the bucket is 20 pixels above
    }


    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        ScreenUtils.clear(0, 0, 0, 1); // Clear the screen

        camera.update(); // Update the camera

        // Move text in a circular path to have an example of a moving object
        sinusInput += delta;
        float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
        float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin(); // Important to call this before drawing anything

        for(int x = 0; x < arrayData.length; x ++){
            for(int y = 0; y < arrayData[0].length; y++){
                value = arrayData[x][y];

                switch(value){
                    case 0: game.getSpriteBatch().draw(wallTexture, x*16, y*16); break;
                    case 1: game.getSpriteBatch().draw(entryPointTexture, x*16, y*16); break;
                    case 2: game.getSpriteBatch().draw(exitTexture, x*16, y*16); break;
                    case 3: game.getSpriteBatch().draw(trapTexture, x*16, y*16); break;
                    case 4: game.getSpriteBatch().draw(enemyTexture, x*16, y*16); break;
                    case 5: game.getSpriteBatch().draw(keyTexture, x*16, y*16); break;
                }
            }

        }
        // Render the text
        // font.draw(game.getSpriteBatch(), "Press ESC to go to menu", textX, textY);

        // Draw the character next to the text :) / We can reuse sinusInput here
        /* game.getSpriteBatch().draw(
                game.getCharacterDownAnimation().getKeyFrame(sinusInput, true),
                textX - 96,
                textY - 64,
                64,
                128
        );
         */

        game.getSpriteBatch().end(); // Important to call this after drawing everything
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    // Additional methods and logic can be added as needed for the game screen
}
