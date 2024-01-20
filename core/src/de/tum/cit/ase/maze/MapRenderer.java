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
    private ObjectMap<String, float[]> coordinateMap;

    private int selectedLevel;

    private int[][] mazeData;
    private int width;
    private int height;

    private SpriteBatch spriteBatch;

    private GameScreen1 gameScreen1; // Add a reference to GameScreen1

    public MapRenderer(int selectedLevel, SpriteBatch spriteBatch, GameScreen1 gameScreen1) {
        this.selectedLevel = selectedLevel;
        this.spriteBatch = spriteBatch;
        this.gameScreen1 = gameScreen1; // Assign the instance of GameScreen1
        this.textureMap = new ObjectMap<>();
        coordinateMap = new ObjectMap<>();
        loadTextures();
        loadMapProperties();
    }
    private void loadTextures() {
        textureMap.put("0", new TextureRegion(gameScreen1.getWallTexture()));
        textureMap.put("1", new TextureRegion(gameScreen1.getEntryPointTexture()));
        textureMap.put("2", new TextureRegion(gameScreen1.getExitTexture()));
        textureMap.put("3", new TextureRegion(gameScreen1.getTrapTexture()));
        textureMap.put("4", new TextureRegion(gameScreen1.getEnemyTexture()));
        textureMap.put("5", new TextureRegion(gameScreen1.getKeyTexture()));
    }

    private void loadMapProperties() {
        Array<String> propertyFiles = new Array<>();
        propertyFiles.add("level-1.properties");
        propertyFiles.add("level-2.properties");
        propertyFiles.add("level-3.properties");
        propertyFiles.add("level-4.properties");
        propertyFiles.add("level-5.properties");

        for (String propertyFile : propertyFiles) {
            try {
                ObjectMap<String, String> properties = new ObjectMap<>();
                PropertiesUtils.load(properties, Gdx.files.internal(propertyFile).reader());
                processProperties(properties);
            } catch (Exception e) {
                Gdx.app.error("MapRenderer", "Error loading map properties for file: " + propertyFile, e);
            }
        }
    }

    private void processProperties(ObjectMap<String, String> properties) {
        width = Integer.parseInt(properties.get("width"));
        height = Integer.parseInt(properties.get("height"));

        mazeData = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String key = x + "," + y;
                int cellType = Integer.parseInt(properties.get(key, "0"));
                mazeData[x][y] = cellType;

                // Load textures based on keys from coordinateMap
                TextureRegion region = textureMap.get(String.valueOf(cellType));
                if (region != null) {
                    float[] coordinates = {x * TILE_SIZE, y * TILE_SIZE};
                    coordinateMap.put(key, coordinates);
                }
            }
        }
    }

    private void renderElement(int x, int y, int value) { // used to render maze elements
        // Render based on the interpreted value
        TextureRegion texture = textureMap.get(String.valueOf(value));
        if (texture != null) {
            spriteBatch.draw(texture, x * TILE_SIZE, y * TILE_SIZE); // Adjust if needed
        }
    }

    public void render(SpriteBatch spriteBatch) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int value = mazeData[x][y];
                renderElement(x, y, value);
            }
        }
    }

    public int getMazeCellValue(int tileX, int tileY) {
        if (tileX >= 0 && tileX < width && tileY >= 0 && tileY < height) {
            return mazeData[tileX][tileY];
        }
        return -1; // else...
    }
}

