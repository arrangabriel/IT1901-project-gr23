package localpersistence;

import core.EntryManager;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;


/**
 * Class for saving and loading entryManagers to and from JSON files.
 */
public final class EntrySaverJson {
    /**
     * A static reference to Get-Fits save location.
     */
    public static final String SYSTEM_SAVE_LOCATION =
        (System.getProperty("user.home")
        + System.getProperty("file.separator")
        + "getfit"
        + System.getProperty("file.separator")
        + "SavedData.json");

    /**
     * Hidden constructor to simulate static class.
     */
    private EntrySaverJson() { }

    /**
     * Iterates over every entry in the provided EntryManager
     * and adds their data as a string to a hashmap.
     * Saves the hashmap to SavedData.json.
     *
     * @param entryManager the EntryManager instance to be saved.
     * @throws IOException              if there was an issue during write.
     * @throws IllegalArgumentException if entryManager is null.
     */
    public static void save(final EntryManager entryManager)
            throws IOException, IllegalArgumentException {
        save(entryManager, SYSTEM_SAVE_LOCATION);
    }

    /**
     * Iterates over every entry in the provided EntryManager
     * and adds their data as a string to a hashmap.
     * Saves the hashmap to the specified JSON file.
     *
     * @param entryManager the EntryManager instance to be saved.
     * @param saveFile     path to the file being written to.
     * @throws IOException              if there was an issue during write.
     * @throws IllegalArgumentException if entryManager or saveFile is null.
     */
    public static void save(final EntryManager entryManager,
                            final String saveFile)
            throws IOException, IllegalArgumentException {
        if (entryManager == null || saveFile == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        JSONObject json = new JSONObject();

        HashMap<String, HashMap<String, String>> map = entryManager.toHashMap();
        map.entrySet().forEach(x -> json.put(x.getKey(), x.getValue()));

        String[] split = saveFile.split(
                System.getProperty("file.separator").replace("\\", "\\\\"));
        String folderPath = String.join(System.getProperty("file.separator"),
                Arrays.copyOfRange(split, 0, split.length - 1));

        File folder = new File(folderPath);

        // Ignored return value from folder and file creation
        boolean created = false;

        if (!folder.exists()) {
            created = folder.mkdirs();
        }

        File file = new File(saveFile);

        if (!file.exists() || !file.isFile()) {
            created = file.createNewFile();
        }

        // Flip created to avoid unused variable warning.
        created = !created;

        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(json.toString());
            writer.flush();
        } catch (IOException ignored) { }
    }

    /**
     * Loads SavedData.json and constructs LogEntries which it appends to the
     * provided EntryManager.
     *
     * @param entryManager the EntryManager to load data into.
     * @throws IOException              if SavedData.JSON could not be found.
     * @throws IllegalArgumentException if entryManager is null.
     */
    public static void load(final EntryManager entryManager)
            throws IOException, IllegalArgumentException {
        load(entryManager, SYSTEM_SAVE_LOCATION);
    }

    /**
     * Loads a specified JSON file and constructs LogEntries,
     * which it appends to the provided EntryManager.
     *
     * @param entryManager the EntryManager to load data into.
     * @param saveFile     the path of the JSON file to load from.
     * @throws IOException
     * if the specified path could not be found.
     * @throws IllegalArgumentException if the entryManager or saveFile is null.
     */
    public static void load(final EntryManager entryManager,
                            final String saveFile)
            throws IOException, IllegalArgumentException {
        if (entryManager == null || saveFile == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        File file = new File(saveFile);
        if (!file.exists()) {
            return;
        }
        Scanner reader = new Scanner(file, StandardCharsets.UTF_8);
        String dataString;
        StringBuilder buffer = new StringBuilder();

        while (reader.hasNextLine()) {
            buffer.append(reader.nextLine());
        }

        reader.close();

        dataString = buffer.toString();

        try {
            JSONObject jsonObject = new JSONObject(dataString);
            HashMap<String, HashMap<String, String>> jsonHash = new HashMap<>();

            for (String key : jsonObject.keySet()) {
                HashMap<String, String> entryHash = new HashMap<>();

                entryHash.put("title",
                        jsonObject.getJSONObject(key).getString("title"));
                entryHash.put("comment",
                        jsonObject.getJSONObject(key).getString("comment"));
                entryHash.put("date",
                        jsonObject.getJSONObject(key).getString("date"));
                entryHash.put("feeling",
                        jsonObject.getJSONObject(key).getString("feeling"));
                entryHash.put("duration",
                        jsonObject.getJSONObject(key).getString("duration"));
                entryHash.put("distance",
                        jsonObject.getJSONObject(key).getString("distance"));
                entryHash.put("maxHeartRate", jsonObject.getJSONObject(key)
                        .getString("maxHeartRate"));
                entryHash.put("exerciseCategory", jsonObject.getJSONObject(key)
                        .getString("exerciseCategory"));
                entryHash.put("exerciseSubCategory",
                        jsonObject.getJSONObject(key)
                                .getString("exerciseSubCategory"));

                jsonHash.put(key, entryHash);

            }

            EntryManager.fromHash(jsonHash, entryManager);

        } catch (JSONException pException) {
            throw new IllegalStateException("Could not load data from file");
        }
    }
}
