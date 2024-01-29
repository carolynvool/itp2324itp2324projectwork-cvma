package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
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
    private ExtendViewport viewport;
    private final BitmapFont font;
    private float sinusInput = 0f;
    private SpriteBatch batch;
    private int value = 0;
    private Maze maze; // Add a Maze variable to connect to the maze class
    private Woman woman;
    private Mob mob;



    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public GameScreen(MazeRunnerGame game, String mapFileName) {
        this.game = game;
        this.maze = new Maze(mapFileName);

        // Create and configure the camera for the game view (e.g., position, viewport, etc.)
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(maze.arrayData.length * 16, maze.arrayData[0].length * 16, camera);
        camera.setToOrtho(false);
        camera.zoom = 0.75f;
        camera.position.set(maze.arrayData.length * 16 / 2f, maze.arrayData[0].length * 16 / 2f, 0);
        camera.update();

        // Get the font from the game's skin
        font = game.getSkin().getFont("font");

        //add mob
        mob = new Mob(10, 10, calculateMovementRectangle());

        // add woman
        this.woman = new Woman(30, 30);

        //set womans initial position
        setInitialWomanPosition();
    }

    // Screen interface methods with necessary functionality
    @Override
    public void render(float delta) {
        // Check for escape key press to go back to the menu
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.goToMenu();
        }

        //moving the camera to follow the character
        camera.position.set(woman.getX() + woman.getWidth() / 2, woman.getY() + woman.getHeight() / 2, 0);
        camera.update();

        // clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        // to render the map elements
        renderMap();

        // Set up and begin drawing with the sprite batch
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        // Important to call this before drawing anything
        game.getSpriteBatch().begin();

        // delta = time passed in sec since the previous frame; so, by increasing the input we create a repeating pattern
        sinusInput += delta;

        // Draw woman, check for collision after updating woman's position
        woman.act(delta);
        checkWomanWallCollision();
        woman.draw(game.getSpriteBatch(), 1);

        // draw out mob
        mob.act(delta);
        mob.draw(game.getSpriteBatch(), 1);

        // Important to call this after drawing everything
        game.getSpriteBatch().end();
    }

    // to render elements part of the maze itself but for the Woman and Mob (which are moving on top of the static maze)
    private void renderMap() {
        // Clear the screen with the desired sandy background color
        ScreenUtils.clear(139 / 255f, 69 / 255f, 19 / 255f, 1);

        // Update the camera and sprite batch
        camera.update();
        game.getSpriteBatch().setProjectionMatrix(camera.combined);

        game.getSpriteBatch().begin();

        // Iterate over the maze arrayData
        for (int x = 0; x < maze.arrayData.length; x++) {
            for (int y = 0; y < maze.arrayData[0].length; y++) {
                int value = maze.arrayData[x][y];
                float renderX = x * 16 * 10;
                float renderY = y * 16 * 10;

                switch (value) {
                    case 0:
                        game.getSpriteBatch().draw(maze.wallTexture, x * 16, y * 16);
                        break;
                    case 1:
                        game.getSpriteBatch().draw(maze.entryPointTexture, x * 16, y * 16);
                        break;
                    case 2:
                        game.getSpriteBatch().draw(maze.exitTexture, x * 16, y * 16);
                        break;
                    case 3:
                        game.getSpriteBatch().draw(maze.trapTexture, x * 16, y * 16);
                        break;
                    case 4:
                        game.getSpriteBatch().draw(maze.enemyTexture, x * 16, y * 16);
                        break;
                    case 5:
                        game.getSpriteBatch().draw(maze.keyTexture, x * 16, y * 16);
                        break;
                    case 6:
                        game.getSpriteBatch().draw(maze.pathTexture, x * 16, y * 16);
                        break;
                }
            }

        }


                // Other rendering logic as needed...
        //add code to render additional game elements based on their values in the maze object. This is where you
        //would handle rendering logic for elements other than the floor inside walls of the maze.
        game.getSpriteBatch().end();


    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false);
        viewport.update(width, height, true);
        camera.position.set(maze.arrayData.length * 16/ 2f,maze.arrayData[0].length * 16 / 2f, 0);
        camera.update();
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
    private Rectangle calculateMovementRectangle() {
        // Calculate the movement rectangle based on maze dimensions
        float mazeWidth = maze.arrayData.length * 16;
        float mazeHeight = maze.arrayData[0].length * 16;
        float rectangleWidth = 10 * 16; // 10 tiles wide
        float rectangleHeight = 10 * 16; // 10 tiles high

        // Ensure the rectangle fits within the maze dimensions
        rectangleWidth = Math.min(rectangleWidth, mazeWidth);
        rectangleHeight = Math.min(rectangleHeight, mazeHeight);

        return new Rectangle(0, 0, rectangleWidth, rectangleHeight);
    }
    private void checkWomanWallCollision() {
        int[][] arrayData = maze.getArrayData();
        int tileSize = 16;

        // Calculate the indices in the array corresponding to the woman's position
        int arrayX = (int) (woman.getX() / tileSize);
        int arrayY = (int) (woman.getY() / tileSize);

        // Check for collisions with walls
        if (arrayX >= 0 && arrayY >= 0 && arrayX < arrayData.length && arrayY < arrayData[0].length) {
            if (arrayData[arrayX][arrayY] == 0) {
                // Handle collision (e.g., stop movement)
                woman.setSpeed(0);
            }
        }
    }
    private void setInitialWomanPosition() {
        // Find the entry point coordinates in the map data
        for (Map.Entry<MapRenderer.MapCoordinates, Integer> entry : maze.getMapData().entrySet()) {
            if (entry.getValue() == 1) { // Assuming 1 is the code for the entry point
                float entryPointX = entry.getKey().getX() * 16; // Adjust based on tile size
                float entryPointY = entry.getKey().getY() * 16; // Adjust based on tile size
                woman.setInitialPosition(entryPointX, entryPointY);
                break; // No need to continue once entry point is found
            }
        }
    }
    // Additional methods and logic can be added as needed for the game screen
    // Move text in a circular path to have an example of a moving object - FOR MID PAUSE SCREEN WITH GAME PAUSE OPTIONS
    //sinusInput += delta;
    //float textX = (float) (camera.position.x + Math.sin(sinusInput) * 100);
    //float textY = (float) (camera.position.y + Math.cos(sinusInput) * 100);

    // Render the text - FOR MID PAUSE SCREEN WITH GAME PAUSE OPTIONS
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
}
