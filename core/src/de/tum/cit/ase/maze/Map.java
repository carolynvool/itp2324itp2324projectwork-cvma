package de.tum.cit.ase.maze;

import java.io.InputStream;
import java.util.Properties;

public class Map { // to load the 5 properties files from the maps folder
    public static Properties loadMapProperties(String filePath) {
        Properties properties = new Properties(); // create a new object
        try (InputStream input = Map.class.getClassLoader().getResourceAsStream(filePath)) {
            properties.load(input); // for the process of loading the properties and reading the file
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties; // return the object containing the properties
    }

    public static void main(String[] args) {
        String propertiesFilePath1 = "maps/level-1.properties";
        Properties mapProperties1 = loadMapProperties(propertiesFilePath1);

        String propertiesFilePath2 = "maps/level-2.properties";
        Properties mapProperties2 = loadMapProperties(propertiesFilePath2);

        String propertiesFilePath3 = "maps/level-3.properties";
        Properties mapProperties3 = loadMapProperties(propertiesFilePath3);

        String propertiesFilePath4 = "maps/level-4.properties";
        Properties mapProperties4 = loadMapProperties(propertiesFilePath4);

        String propertiesFilePath5 = "maps/level-5.properties";
        Properties mapProperties5 = loadMapProperties(propertiesFilePath5);

        // Access properties and allow us to perform operations with them

        printProperties("level-1", mapProperties1);
        printProperties("level-2", mapProperties2);
        printProperties("level-3", mapProperties3);
        printProperties("level-4", mapProperties4);
        printProperties("level-5", mapProperties5);
    }
    private static void printProperties(String level, Properties properties) {
        System.out.println("Properties for " + level + ":");
        properties.forEach((key, value) -> System.out.println(key + " = " + value));
        System.out.println();
        }
}