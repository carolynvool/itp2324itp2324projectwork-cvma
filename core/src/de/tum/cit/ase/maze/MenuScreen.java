package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.source.doctree.SystemPropertyTree;

/**
 * The MenuScreen class is responsible for displaying the main menu of the game.
 * It extends the LibGDX Screen class and sets up the UI components for the menu.
 */
public class MenuScreen implements Screen {
    MazeRunnerGame game;
    Texture background;
    SpriteBatch batch;
    private final Stage stage;

    BitmapFont font;

    /**
     * Constructor for MenuScreen. Sets up the camera, viewport, stage, and UI elements.
     *
     * @param game The main game class, used to access global resources and methods.
     */
    public MenuScreen(MazeRunnerGame game) {
        var camera = new OrthographicCamera();
        camera.zoom = 1.5f; // Set camera zoom for a closer view
        batch = new SpriteBatch();

        Texture background1 = new Texture(Gdx.files.internal("beachbackgroundphoto.jpeg"));

        Viewport viewport = new ScreenViewport(camera); // Create a viewport with the camera
        stage = new Stage(viewport, game.getSpriteBatch()); // Create a stage for UI elements

        Table table = new Table(); // Create a table for layout
        table.setFillParent(true); // Make the table fill the stage
        stage.addActor(table); // Add the table to the stage

        // Add a label as a title
        table.add(new Label("Press the woman to start game!", game.getSkin(), "title")).padBottom(80).row();

        // Loaded image as a drawable and create and add a button to go to game screen; but for us it will be the mid-screen where the player selects a level of map to play in
        // The start button is the woman character
        Texture spritesheetTexture = new Texture(Gdx.files.internal("obesewomandoingcartwheels1.png"));
        TextureRegion targetRegion = new TextureRegion(spritesheetTexture, 0, 3 * 212, 221, 212);
        ImageButton goToGameButton = new ImageButton(new TextureRegionDrawable(targetRegion));

        // Create and add a button to go to the game screen
        table.add(goToGameButton).width(300).row();
        goToGameButton.addListener(new InputListener() {
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    game.goToMapSelection(); // Change to the game screen when Enter key is pressed -> changed to go to map selection screen
                    return true; // Consume the event
                }
                return false; // Let other key events be handled
            }
        });
        goToGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.goToMapSelection(); // Change to the game screen when button is pressed -> changed to go to map selection screen
            }
        });
    }
    @Override
    public void render(float delta) {
        Texture background = new Texture(Gdx.files.internal("beachbackgroundphoto.jpeg"));
        Gdx.gl.glClearColor(0, 0, 0, 0.2f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the screen
        batch.begin();
        if (background != null) {
            batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        batch.end();
        stage.act(Math.min(delta, 1 / 30f)); // Update the stage
        stage.draw(); // Draw the stage
        Gdx.input.setInputProcessor(stage);
    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update the stage viewport on resize
    }
    @Override
    public void dispose() {
        // Dispose of the stage when screen is disposed
        stage.dispose();
        batch.dispose();
        background.dispose();
    }

    @Override
    public void show() {
        // Set the input processor so the stage can receive input events
        Gdx.input.setInputProcessor(stage);
    }

    // The following methods are part of the Screen interface but are not used in this screen.
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
