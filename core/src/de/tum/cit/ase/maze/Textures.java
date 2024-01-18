package de.tum.cit.ase.maze;
// a class to split the tile set into individual sprites to THEN assign to the elements of 0-5 value from the coordinates

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Textures extends ApplicationAdapter { // to load the different tile elements from the spite sheets FOR NOW PROVIDED BY PROF.; later to change to our own

        private SpriteBatch batch;
        private Texture tileSet;
        private TextureRegion[] tiles;


        @Override
        public void create() {
            batch = new SpriteBatch();
            tileSet = new Texture(Gdx.files.internal("basictiles.png")); // Adjust the paths

            // Specify the number of rows and columns in your tile set
            int rows = 14;
            int cols = 8;

            // Calculate the dimensions of each tile
            int tileWidth = tileSet.getWidth() / cols;
            int tileHeight = tileSet.getHeight() / rows;

            // Create TextureRegions for each tile
            tiles = new TextureRegion[rows * cols]; // number of all elements aka single tiles aka length of array
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    int x = col * tileWidth;
                    int y = row * tileHeight;
                    tiles[row * cols + col] = new TextureRegion(tileSet, x, y, tileWidth, tileHeight); // to get every row to be split into the number aka 8 column squares
                }
            }
        }


        @Override
        public void dispose() {
            batch.dispose();
            tileSet.dispose();
        }

    }


// Loading our different textures - suggestion to use the given textures first and then improve to; check in with the given dimensions and pixels of the provided textures to be matched to new ones
// try to first make it work with one map until game actually shows; then figure out the logic for the rest 4
//
//private Texture wallTexture;
// private Texture walkableAreaTexture;
//private Texture keyTexture;
//private Texture trapTexture;
// private Texture enemyTexture;
//private Texture cactusTexture;

// Initializing our textures with what we have downloaded from the internet
// wallTexture = new Texture(Gdx.files.internal("assets/textures/wall.png"));
//walkableAreaTexture = new Texture(Gdx.files.internal("assets/textures/walkable_area.jpg"));
// keyTexture = new Texture(Gdx.files.internal("assets/textures/key.png"));
// trapTexture = new Texture(Gdx.files.internal("assets/textures/trap.png")); // we maybe need a new one; why is it red?
//enemyTexture = new Texture(Gdx.files.internal("assets/textures/dynamo.png"));
// cactusTexture = new Texture(Gdx.files.internal("assets/textures/cactus.png"));

/*
//!!!!!!!!!!!!!!
// Iterate through levels and render elements based on properties
        for (int level = 1; level <= 5; level++) {
                Properties properties = mapPropertiesByLevel.get(level);

                // Iterate through properties and render elements
                for (Object key : properties.keySet()) {
                String coordinate = (String) key;
                int value = Integer.parseInt(properties.getProperty(coordinate));

                // Extract x and y coordinates from the string
                String[] coordinates = coordinate.split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);

                // Render the element based on the value
                renderElement(x, y, value);
                }
                }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */

/*
@Override
    public void dispose() { // Called when the screen should release all resources.
        // Dispose of textures, sounds, and other assets.

        //wallTexture.dispose();
        //walkableAreaTexture.dispose();
        //keyTexture.dispose();
        //trapTexture.dispose();
        //enemyTexture.dispose();
        //cactusTexture.dispose();

    }
 */

/*
private void renderElement(int x, int y, int value) {
        // Render based on the interpreted value
        // switch (value) {
        //case WALL:
        //  renderWall(x, y);
        //  break;
        // case WALKABLE_AREA:
        // renderWalkableArea(x, y);
        //  break;
        // case KEY_TO_COLLECT:
        // renderKeyToCollect(x, y);
        // break;
        // case FIXED_TRAP:
        // renderFixedTrap(x, y);
        // break;
        // case DYNAMIC_ENEMY:
        // renderDynamicEnemy(x, y);
        // break;
        // case CACTUS:
        // renderSomethingElse(x, y);
        // break;
        // }
        // }
/*
    private void renderWall(int x, int y) {
        // Your rendering logic for a wall element at coordinates (x, y)
        // Example: render a texture or shape at the specified coordinates
        game.getSpriteBatch().draw(wallTexture, x * 40, y * 40);

    } private void renderWalkableArea(int x, int y) {
        // Render walkable area logic
        game.getSpriteBatch().draw(walkableAreaTexture, x * 40, y * 40);
    }

    private void renderKeyToCollect(int x, int y) {
        // Render key to collect logic
        game.getSpriteBatch().draw(keyTexture, x * 40, y * 40);
    }

    private void renderFixedTrap(int x, int y) {
        // Render fixed trap logic
        game.getSpriteBatch().draw(trapTexture, x * 40, y * 40);
    }

    private void renderDynamicEnemy(int x, int y) {
        // Render dynamic enemy logic
        game.getSpriteBatch().draw(enemyTexture, x * 40, y * 40);
    }

    private void renderSomethingElse(int x, int y) {
        // Render something else logic
        game.getSpriteBatch().draw(cactusTexture, x * 40, y * 40);
    }
//}
 */
