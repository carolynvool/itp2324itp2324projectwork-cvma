package de.tum.cit.ase.maze;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
    private GameScreen1 gameScreen1;
   // private MapSelection mapSelectionScreen;
    private int selectedLevel;

    // Sprite Batch for rendering 2D graphics
    private SpriteBatch spriteBatch;

    // BitmapFont for rendering fonts

    public BitmapFont font;

    // UI Skin for styling like buttons, labels, tabs
    private Skin skin;

    // Character animation downwards
    private Animation<TextureRegion> characterDownAnimation;

    // Load the backgroundMusic in order to control it after
    private Music backgroundMusic;


    // constructor to initialize the game; NativeFileChooser parameter which is used in a desktop environment
    public MazeRunnerGame(NativeFileChooser fileChooser) {
        super();
    }
    // the game loop should continuously call render() on the current screen!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    // Called when the game is created. Initializes the SpriteBatch and Skin;
    // also initializes the character animation; background music is loaded and played;
    // initial screen is set to the menu screen
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();// Create SpriteBatch
        skin = new Skin(Gdx.files.internal("craft/craftacular-ui.json")); // Load UI skin
        this.loadCharacterAnimation(); // Load character animation
        // Play some background music
        // Background sound
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("background.mp3"));
        backgroundMusic.setLooping(true);
        //backgroundMusic.play(); i muted it so we don't have to listen to it every time we run the game
        // need a bit of peace during the night
        // 1. maybe a different calmer sound during the game play; 2. and sound effects for having a key collected; 3. opening the exit
        // 1. message appearing for a victory after the exit has been opened; 2. message appearing for a game over after all lives have been used up
        // set number of available lives to say 5; QUESTION: do we have 5 lives available for all the levels or for each one respectively?

        goToMenu(); // START at the menu screen
        // setScreen(new GameScreen());

    }
    // SWITCH SCREENS METHODS
    // go to the menu screen
    public void goToMenu() {
        this.setScreen(new MenuScreen(this)); // Set the current screen to MenuScreen
        if (gameScreen1 != null) {
            gameScreen1.dispose(); // Dispose the game screen if it exists
            gameScreen1 = null;
        }
    }
    // go to the mapSelection screen
    /*public void goToMapSelection() {
        mapSelectionScreen = new MapSelection(this); // THIS = THE GAME ITSELF AND IT REPEATS THE GameScreen class
        setScreen(mapSelectionScreen);
    }
    // go to the game screen
    // switch to the game screen - WE HAVE TO ADAPT CODE TO OUR LOGIC
    public void goToGame(int mapNumber) {
        this.setScreen(new GameScreen1(this, mapNumber)); // Pass mapNumber to the GameScreen constructor
        if (menuScreen != null) {
            menuScreen.dispose(); // Dispose the menu screen if it exists
            menuScreen = null;
        }
    }

     */
    public void render() {
        super.render(); // important!
    }
    public void setSelectedLevel(int level) {
        selectedLevel = level;
    }

    public int getSelectedLevel() {
        return selectedLevel;
    }
    /**
     * Loads the character animation from the character.png file.
     */
    // loads character animation from a sprite sheet containing the frames of the animation;
    // sets up the animation via LibGDX 'Animation' and 'TextureRegion'
    private void loadCharacterAnimation() {
        Texture walkSheet = new Texture(Gdx.files.internal("obesewomandoingcartweels.png")); //
    //dimension variables of each frame in the provided sprite sheet; their number of cols and rows
        int frameWidth = 16;
        int frameHeight = 16;
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
        if (getScreen() != null) {
            getScreen().hide();
            getScreen().dispose();
        }

        if (spriteBatch != null) {
            spriteBatch.dispose();
        }

        if (skin != null) {
            skin.dispose();
        }

        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }

        if (gameScreen1 != null) {
            gameScreen1.dispose();
            gameScreen1 = null;
        }

        if (menuScreen != null) {
            menuScreen.dispose();
            menuScreen = null;
        }
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

    public void goToGame() {
        setScreen(new GameScreen1(this));
    }
}
