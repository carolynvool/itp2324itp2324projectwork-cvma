package de.tum.cit.ase.maze;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapRenderer{
    public static class MapCoordinates {
        private int x;
        private int y;
        public MapCoordinates(int x, int y){
            this.x = x;
            this.y = y;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
    }
    public static void main(String[] args) {
      for (int level = 1; level <= 5; level++){
          String filePath = "maps/level-" + level + ".properties";
          Map<MapCoordinates, Integer> mapData = readMapFile(filePath);
          System.out.println("Map Data for Level " + level + ":");
          mapData.forEach((coordinates, value)-> // lec 6 content
                  System.out.println(coordinates.getX() + "," + coordinates.getY() + "=" + value));
          int[][] arrayData = convertToArrays(mapData);
          System.out.println("Array Data for Level " + level + ":");
          for (int[] row : arrayData) {
              for (int value : row) {
                  System.out.print(value + " ,");
              }
              System.out.println();
          }
      }
    }
    public static Map<MapCoordinates, Integer>  readMapFile(String filePath) {
        Map<MapCoordinates, Integer> mapData = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                String[] coordinates = parts[0].split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                int value = Integer.parseInt(parts[1]);
                mapData.put(new MapCoordinates(x, y), value);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return mapData;
    }
    public static int[][] convertToArrays(Map<MapCoordinates, Integer> mapData) {
        int maxX = mapData.keySet().stream().mapToInt(MapCoordinates::getX).max().orElse(0);
        int maxY = mapData.keySet().stream().mapToInt(MapCoordinates::getY).max().orElse(0);

        int[][] arrayData = new int[maxX + 1][maxY + 1];

        for (Map.Entry<MapCoordinates, Integer> entry : mapData.entrySet()) {
            MapCoordinates coordinates = entry.getKey();
            int x = coordinates.getX();
            int y = coordinates.getY();
            arrayData[x][y] = entry.getValue();
        }

        return arrayData;
    }

}