package de.tum.cit.ase.maze;
// his game prototype
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import games.spooky.gdx.nativefilechooser.NativeFileChooser;

/**
 * The MazeRunnerGame class represents the core of the Maze Runner game.
 * It manages the screens and global resources like SpriteBatch and Skin.
 */
public class MazeRunnerGame extends Game {
    // Screens
    private MenuScreen menuScreen;
    private GameScreen gameScreen;

    // Sprite Batch for rendering 2D graphics
    private SpriteBatch spriteBatch;

    // BitmapFont for rendering fonts

    public BitmapFont font;

    // UI Skin for styling like buttons, labels, tabs
    private Skin skin;

    // Character animation downwards
    private Animation<TextureRegion> characterDownAnimation;

    // constructor to initialize the game; NativeFileChooser parameter which is used in a desktop environment
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
    }


    // Called when the game is created. Initializes the SpriteBatch and Skin;
    // also initializes the character animation; background music is loaded and played;
    // initial screen is set to the menu screen
    @Override
    public void create() {
        spriteBatch = new SpriteBatch(); // Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        this.loadCharacterAnimation(); // Load character animation

        // Play some background music
        // Background sound
        Music backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.play();

        goToMenu(); // Navigate to the menu screen
    }

    // go to the menu screen
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen != null) {
            gameScreen.dispose(); // Dispose the game screen if it exists
            gameScreen = null;
        }
    }
    public void render() {
        super.render(); // important!
    }

    // switch to the game screen
    public void goToGame() {
        this.setScreen(new GameScreen(this)); // Set the current screen to GameScreen
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

    /**
     * Loads the character animation from the character.png file.
     */
    // loads character animation from a sprite sheet containing the frames of the animation;
    // sets up the animation via LibGDX 'Animation' and 'TextureRegion'
    private void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("obesewomandoingcartwheels.png")); //
    //dimension variables of each frame in the provided sprite sheet; their number of cols and rows
        int frameWidth = 221;
        int frameHeight = 212;
        int rows = 5;
        int cols = 4;
        int animationFrames = rows * cols;

        // libGDX internal Array instead of ArrayList because of performance; array to store the individual frames; region= part of a texture
        Array<TextureRegion> walkFrames = new Array<>(TextureRegion.class);

        // Add all frames to the animation
       // extracting each of the frames from the sprite sheet and adds it to the walkFrames array
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int startX = col * frameWidth;
                int startY = row * frameHeight;
                walkFrames.add(new TextureRegion(walkSheet, startX, startY, frameWidth, frameHeight));
            }
        }
        characterDownAnimation = new Animation<>(0.2f, walkFrames); // to create the animation at duration of 0.2 seconds
    }

    /**
     * Cleans up resources when the game is disposed.
     */
    @Override
    public void dispose() {
        getScreen().hide(); // Hide the current screen
        getScreen().dispose(); // Dispose the current screen
        spriteBatch.dispose(); // Dispose the spriteBatch
        skin.dispose(); // Dispose the skin
    }

    // Getter methods for access from other parts of the game

    public Skin getSkin() {
        return skin;
    }

    public Animation<TextureRegion> getCharacterDownAnimation() {
        return characterDownAnimation;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
}
