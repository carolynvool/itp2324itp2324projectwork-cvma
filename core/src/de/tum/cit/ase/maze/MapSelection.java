package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MapSelection implements Screen {
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

        createLevelButtons();
    }
    @Override
    public void show() {
        stage.getBatch().begin();
        stage.getBatch().draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        Label.LabelStyle labelStyle = new Label.LabelStyle(game.getSkin().getFont("font"), Color.WHITE);
        Label explanationLabel = new Label("Choose a level:", labelStyle);
        explanationLabel.setPosition(100, 350);
        stage.addActor(explanationLabel);
    }
    private void createLevelButtons() {
        for (int i = 1; i <= 5; i++) {
            Texture buttonTexture = new Texture(Gdx.files.internal("buttonStock1.png"));
            Drawable buttonDrawable = new TextureRegionDrawable(buttonTexture);
            TextButton levelButton = new TextButton("level" + i, game.getSkin());

            final int levelNumber = i; // Store the level number in a final variable for the inner class
            levelButton.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    // Replace 'YOUR_MAP_NUMBER' with the actual map number or logic to determine it
                    game.setScreen(new GameScreen1(game, levelNumber));
                }
            });

            levelButton.setPosition(100, 300 - i * 50);
            stage.addActor(levelButton);
        }
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


