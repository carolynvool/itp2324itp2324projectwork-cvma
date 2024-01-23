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
        private int[][] mapArray;
        private SpriteBatch spriteBatch;
        private int maxX;
        private int maxY;
        private GameScreen1 gameScreen1; // Add a reference to GameScreen1
        private ObjectMap<Integer, ObjectMap<String, String>> mapPropertiesByLevel;

        public MapRenderer(int selectedLevel, SpriteBatch spriteBatch, GameScreen1 gameScreen1) {
            this.selectedLevel = selectedLevel;
            this.spriteBatch = spriteBatch;
            this.gameScreen1 = gameScreen1; // Assign the instance of GameScreen1
            this.mapPropertiesByLevel = new ObjectMap<>();
            loadMapProperties();
            // Load textures
            loadTextures();
            // Initialize maxX and maxY
            maxX = calculateMaxX();
            maxY = calculateMaxY();
            // Initialize mazeData and mapArray
            mazeData = new int[maxX][maxY];
            mapArray = new int[maxX][maxY];
            // Add your properties here (e.g., properties.put("0,0", "1"))
            processProperties();
        }
        private void loadMapProperties() {
            Array<String> propertyFiles = new Array<>();
            propertyFiles.add("maps/level-1.properties");
            propertyFiles.add("maps/level-2.properties");
            propertyFiles.add("maps/level-3.properties");
            propertyFiles.add("maps/level-4.properties");
            propertyFiles.add("maps/level-5.properties");

            for (String propertyFile : propertyFiles) {
                try {
                    ObjectMap<String, String> properties = new ObjectMap<>();
                    PropertiesUtils.load(properties, Gdx.files.internal(propertyFile).reader());
                    int level = extractLevelFromFileName(propertyFile);
                    mapPropertiesByLevel.put(level, properties);
                } catch (Exception e) {
                    Gdx.app.error("MapRenderer", "Error loading map properties for file: " + propertyFile, e);
                }
            }
        }
        private int extractLevelFromFileName(String fileName) {
            // Assuming that your file names follow the pattern "level-x.properties"
            String levelPart = fileName.substring(fileName.indexOf('-') + 1, fileName.indexOf('.'));
            return Integer.parseInt(levelPart);
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
            // Initialize maxX to ensure it is valid
            int maxX = 0;

            // Iterate through levels and properties
            for (int level = 1; level <= 5; level++) {
                ObjectMap<String, String> properties = mapPropertiesByLevel.get(level);

                // Iterate through properties and update maxX
                for (ObjectMap.Entry<String, String> entry : properties.entries()) {
                    String coordinate = entry.key;

                    // Extract x coordinate from the string
                    int x = Integer.parseInt(coordinate.split(",")[0]);

                    // Update maxX
                    maxX = Math.max(maxX, x + 1);
                }
            }
            return maxX;
        }

        private int calculateMaxY() {
            // Initialize maxY to ensure it is valid
            int maxY = 0;

            // Iterate through levels and properties
            for (int level = 1; level <= 5; level++) {
                ObjectMap<String, String> properties = mapPropertiesByLevel.get(level);

                // Iterate through properties and update maxY
                for (ObjectMap.Entry<String, String> entry : properties.entries()) {
                    String coordinate = entry.key;

                    // Extract y coordinate from the string
                    int y = Integer.parseInt(coordinate.split(",")[1]);

                    // Update maxY
                    maxY = Math.max(maxY, y + 1);
                }
            }
            return maxY;
        }

        private void processProperties() {
            // Initialize maxX and maxY to ensure they are valid
            maxX = 0;
            maxY = 0;

            // Use mapPropertiesByLevel.size() instead of hard-coding the level count
            for (ObjectMap.Entry<Integer, ObjectMap<String, String>> levelEntry : mapPropertiesByLevel.entries()) {
                ObjectMap<String, String> properties = levelEntry.value;

                // Iterate through properties and render elements
                for (ObjectMap.Entry<String, String> entry : properties.entries()) {
                    String coordinate = entry.key;
                    int value = Integer.parseInt(entry.value);

                    // Extract x and y coordinates from the string
                    String[] coordinates = coordinate.split(",");
                    int x = Integer.parseInt(coordinates[0]);
                    int y = Integer.parseInt(coordinates[1]);

                    // Render the element based on the value
                    renderElement(x, y, value);

                    // Update maxX and maxY
                    maxX = Math.max(maxX, x + 1);
                    maxY = Math.max(maxY, y + 1);
                }
            }
        }
        public void renderMap (SpriteBatch spriteBatch, float characterX, float characterY) {
            // Begin spriteBatch before rendering map elements
            spriteBatch.begin();
            for (int x = 0; x < maxX; x++) {
                for (int y = 0; y < maxY; y++) {
                    int value = mapArray[x][y];
                    renderElement(x, y, value);
                }
            }
            // Render the character at its position using getter methods
            spriteBatch.draw(new TextureRegion(gameScreen1.getCharacterTexture()),
                    gameScreen1.getCharacterX(), gameScreen1.getCharacterY());
            // End spriteBatch after rendering the character
            spriteBatch.end();
        }
        private void renderElement(int x, int y, int value) {
            // Render based on the interpreted value
            TextureRegion texture = textureMap.get(String.valueOf(value));
            if (texture != null) {
                spriteBatch.draw(texture, x * TILE_SIZE, y * TILE_SIZE);
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



