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
        public void render() {
            // Render the tiles (for testing purposes)
            batch.begin();
            for (int i = 0; i < tiles.length; i++) {
                int x = i % 2 * 40; // Adjust the positioning for testing
                int y = Gdx.graphics.getHeight() - (i / 2 + 1) * 40;
                batch.draw(tiles[i], x, y, 40, 40); // Adjust the size for testing
            }
            batch.end();
        }

        @Override
        public void dispose() {
            batch.dispose();
            tileSet.dispose();
        }

    }


