package com.discryptment.util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvReader {
    private static Dotenv dotenv;
    private static boolean isLoaded = false;

    // Private constructor to prevent instantiation
    private EnvReader(){};

    public static void init() {
        init("./", ".env");
    }
    public static void init(String directory) {
        init(directory, ".env");
    }
    /**
     * Initialize the environment utility with custom directory and filename
     * @param directory The directory containing the env file
     * @param filename The name of the env file
     */
    public static void init(String directory, String filename) {
        try {
            dotenv = Dotenv.configure()
                    .directory(directory)
                    .filename(filename)
                    .ignoreIfMissing()
                    .load();
            isLoaded = true;
            System.out.println("Environment loaded from: " + directory + "/" + filename);
        } catch (Exception e) {
            System.err.println("Failed to load environment file: " + e.getMessage());
            // Create empty dotenv to prevent null pointer exceptions
            dotenv = Dotenv.configure().ignoreIfMissing().load();
            isLoaded = false;
        }
    }
    /**
     * Get environment variable value
     * @param key The environment variable key
     * @return The value or null if not found
     */
    public static String get(String key) {
        ensureLoaded();
        return dotenv.get(key);
    }

    /**
     * Get environment variable value with default
     * @param key The environment variable key
     * @param defaultValue The default value if key not found
     * @return The value or default value
     */
    public static String get(String key, String defaultValue) {
        ensureLoaded();
        return dotenv.get(key, defaultValue);
    }

    public static boolean isLoaded() {
        return isLoaded;
    }
    private static void ensureLoaded() {
        if (dotenv == null) {
            init();
        }
    }


    public static void readWithDotenv() {
        Dotenv dotenv = Dotenv.configure()
                .directory("../../resources/.env")       // Directory where .env file is located
                .filename(".env")      // Name of the env file
                .load();

        // Read variables
        String botToken = dotenv.get("BOT_TOKEN");
        System.out.println(botToken);
    }
}
