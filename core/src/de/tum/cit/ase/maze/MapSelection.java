package de.tum.cit.ase.maze;
/*
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
*/

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/*public class MapSelection implements Screen {
    private final MazeRunnerGame game;
    private OrthographicCamera camera;
    Texture background;
    private Stage stage;
    public MapSelection(MazeRunnerGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        background = new Texture(Gdx.files.internal("beachbackgroundphoto.jpeg"));
        Label.LabelStyle labelStyle = new Label.LabelStyle(game.getSkin().getFont("font"), Color.WHITE);
        Label explanationLabel = new Label("Choose a level:", labelStyle);
        explanationLabel.setPosition(100, 350); // this is for the text position
        stage.addActor(explanationLabel);

        for (int i = 1; i <= 5; i++){ // to create the 5 buttons for each level
            Texture buttonTexture = new Texture(Gdx.files.internal("buttonStock1.png"));// how the button will look like
            Drawable buttonDrawable = new TextureRegionDrawable((buttonTexture));// to draw it
            TextButton levelButton = new TextButton("level" + i, game.getSkin());//for each respective level
            levelButton.addListener(new ClickListener(){//for the action of each button to be connected to the execution of the code of continuing to the level of label chosen
                public void clicked(InputEvent event, float x, float y) { // event = clicking;
                    game.setScreen(new GameScreen(game));}
            });
            levelButton.setPosition(100, 300 - i * 50); // this is for the level tabs position
            stage.addActor(levelButton);
        }
    }// end of constructor
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        background.dispose();
    }
    }
 */

/* public class MapSelection extends ScreenAdapter {
    private Stage stage;
    private Skin skin;
    private Game game;

    public MapSelection(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Create buttons for each map
        for (int i = 1; i <= 5; i++) {
            final int mapNumber = i;
            TextButton button = new TextButton("Map " + i, skin);
            button.setSize(200, 60);
            button.setPosition(50, Gdx.graphics.getHeight() - i * 80); // Adjust position accordingly

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    onMapSelected(mapNumber);
                }
            });

            stage.addActor(button);
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

     private void onMapSelected(int mapNumber) {
        game.setScreen(new GameScreen(this, mapNumber));
    }



    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MapSelection implements Screen {
    private final MazeRunnerGame game;
    private Stage stage;

    public MapSelection(MazeRunnerGame game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());

        // Create buttons for each level
        for (int i = 1; i <= 5; i++) {
            final int mapNumber = i;
            TextButton button = new TextButton("Level " + i, game.getSkin());
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.goToGame(mapNumber); // Navigate to the GameScreen with the selected mapNumber
                }
            });
            stage.addActor(button);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // No action needed for pause
    }

    @Override
    public void resume() {
        // No action needed for resume
    }

    @Override
    public void hide() {
        // No action needed for hide
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

