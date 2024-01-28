package de.tum.cit.ase.maze;

public class LevelScreen extends GameScreen{
    Woman woman;
    /**
     * Constructor for GameScreen. Sets up the camera and font.
     *
     * @param game The main game class, used to access global resources and methods.
     * @param mapFileName
     */
    public LevelScreen(MazeRunnerGame game, String mapFileName) {
        super(game, mapFileName);
    }

    public void initialise(){
        woman = new Woman(0, 0);
    }
}
