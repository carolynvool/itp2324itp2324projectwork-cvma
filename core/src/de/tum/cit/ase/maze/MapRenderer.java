package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.PropertiesUtils;

    public class MapRenderer {
        private static final int TILE_SIZE = 16;
        private ObjectMap<String, TextureRegion> textureMap;
        private int selectedLevel;
        private int[][] mazeData;
        private SpriteBatch spriteBatch;
        private int[][] mapArray; // Add this member variable
        private int maxX;
        private int maxY;
        private GameScreen1 gameScreen1; // Add a reference to GameScreen1

        public MapRenderer(int selectedLevel, SpriteBatch spriteBatch, GameScreen1 gameScreen1) {
            this.selectedLevel = selectedLevel;
            this.spriteBatch = spriteBatch;
            this.gameScreen1 = gameScreen1; // Assign the instance of GameScreen1

            // Initialize maxX and maxY
            maxX = calculateMaxX();
            maxY = calculateMaxY();
            // Initialize mapArray with dimensions (maxY + 1) and (maxX + 1)
            mapArray = new int[maxY + 1][maxX + 1];
            // Load textures
            loadTextures();
        }
        private void loadTextures() {
            textureMap = new ObjectMap<>();
            textureMap.put("0", new TextureRegion(gameScreen1.getWallTexture()));
            textureMap.put("1", new TextureRegion(gameScreen1.getEntryPointTexture()));
            textureMap.put("2", new TextureRegion(gameScreen1.getExitTexture()));
            textureMap.put("3", new TextureRegion(gameScreen1.getTrapTexture()));
            textureMap.put("4", new TextureRegion(gameScreen1.getEnemyTexture()));
            textureMap.put("5", new TextureRegion(gameScreen1.getKeyTexture()));
        }

        private int calculateMaxX() {
            // Assuming width is calculated during property processing
            return maxX;
        }

        private int calculateMaxY() {
            // Assuming height is calculated during property processing
            return maxY;
        }

        private void processProperties(ObjectMap<String, String> properties) {
            // Initialize maxX and maxY to ensure they are valid
            maxX = 0;
            maxY = 0;

            for (int x = 0; x < maxX; x++) {
                for (int y = 0; y < maxY; y++) {
                    mapArray[y][x] = mazeData[x][y]; // Adjust the indices if needed
                    String key = x + "," + y;
                    int cellType = Integer.parseInt(properties.get(key, "0"));
                    mazeData[x][y] = cellType;

                    // Update maxX and maxY
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }

        private void renderElement(int x, int y, int value) {
            // Render based on the interpreted value
            TextureRegion texture = textureMap.get(String.valueOf(value));
            if (texture != null) {
                spriteBatch.draw(texture, x * TILE_SIZE, y * TILE_SIZE);
            }
        }

        public void renderMap (SpriteBatch spriteBatch) {
            for (int x = 0; x <= maxX; x++) {
                for (int y = 0; y <= maxY; y++) {
                    int value = mapArray[y][x];
                    renderElement(x, y, value);
                }
            }
        }
        public int getMazeCellValue(int tileX, int tileY) {
            if (tileX >= 0 && tileX < maxX && tileY >= 0 && tileY < maxY) {
                return mazeData[tileX][tileY];
            }
            return -1; // else...
        }

        public void dispose() {
            // Dispose of any resources used by MapRenderer
            for (TextureRegion textureRegion : textureMap.values()) {
                textureRegion.getTexture().dispose();
            }
        }
    }



