package de.tum.cit.ase.maze;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Map;

public class Maze {
    TextureRegion[] tileRegions;
    TextureRegion wallTexture;
    TextureRegion entryPointTexture;
    TextureRegion exitTexture;
    TextureRegion trapTexture;
    TextureRegion enemyTexture;
    TextureRegion keyTexture;
    TextureRegion pathTexture;
    Map<MapRenderer.MapCoordinates, Integer> mapData;
    int[][] arrayData;

    public Maze(String mapFileName){
        mapData = MapRenderer.readMapFile(mapFileName);
        arrayData = MapRenderer.convertToArrays(mapData);
        Texture tileSheet = new Texture(Gdx.files.internal("basictiles.png"));
        Texture tileSheet2 = new Texture(Gdx.files.internal("things.png"));
        Texture tileSheet3 = new Texture(Gdx.files.internal("mobs.png"));
        Texture tileSheet4 = new Texture(Gdx.files.internal("objects.png"));

        TextureRegion[][] region = TextureRegion.split(tileSheet, 16,16);
        TextureRegion[][] region2 = TextureRegion.split(tileSheet2, 16,16);
        TextureRegion[][] region3 = TextureRegion.split(tileSheet3,16,16);
        TextureRegion[][] region4 = TextureRegion.split(tileSheet4,16,16);

        wallTexture = region[1][6];
        entryPointTexture = region2[2][1];
        exitTexture = region2[0][4];
        trapTexture = region4[3][9];
        enemyTexture = region3[4][0];// to be the mob by carol - assign the mob to appear randomly
        keyTexture = region2[0][3];
        pathTexture = region[1][2];
    }

    public Map<MapRenderer.MapCoordinates, Integer> getMapData() {
        return mapData;
    }

    public int[][] getArrayData() {
        return arrayData;
    }

    public TextureRegion getWallTexture() {
        return wallTexture;
    }

    public TextureRegion getEntryPointTexture() {
        return entryPointTexture;
    }

    public TextureRegion getExitTexture() {
        return exitTexture;
    }

    public TextureRegion getTrapTexture() {
        return trapTexture;
    }

    public TextureRegion getEnemyTexture() {
        return enemyTexture;
    }

    public TextureRegion getKeyTexture() {
        return keyTexture;
    }

    public TextureRegion getPathTexture() {
        return pathTexture;
    }
}
