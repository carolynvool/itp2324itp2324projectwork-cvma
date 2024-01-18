package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.PropertiesUtils;

public class MapRenderer {
    private ObjectMap<String, TextureRegion> textureMap;
    private ObjectMap<String, float[]> coordinateMap;
    private int selectedLevel;

    private int[][] mazeData;
    private int width;
    private int height;
    public MapRenderer(int selectedLevel) {
        this.selectedLevel = selectedLevel;
        textureMap = new ObjectMap<>();
        coordinateMap = new ObjectMap<>();
        loadMapProperties();
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

                for (ObjectMap.Entry<String, String> entry : properties.entries()) {
                    String key = entry.key;
                    String value = entry.value;
                    String[] coordinates = value.split(",");
                    float[] xy = {Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1])};
                    coordinateMap.put(key, xy);
                }
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
                    float[] coordinates = {x * 16, y * 16}; // Adjust if needed
                    coordinateMap.put(key, coordinates);
                }
            }
        }
    }

    public void render(SpriteBatch spriteBatch) {
        // Iterate through the coordinateMap and draw textures
        for (ObjectMap.Entry<String, float[]> entry : coordinateMap.entries()) {
            String key = entry.key;
            float[] coordinates = entry.value;

            TextureRegion texture = textureMap.get(key);
            if (texture != null) {
                spriteBatch.draw(texture, coordinates[0], coordinates[1]);
            }
        }
    }

}
