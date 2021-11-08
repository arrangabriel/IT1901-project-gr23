package localpersistence;

import core.EntryManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Class for saving and loading entryManagers to and from JSON files.
 */
public final class EntrySaverJson {

    /**
     * Hidden constructor.
     */
    private EntrySaverJson() {
    }

    /**
     * Iterates over every entry in the provided EntryManager
     * and adds their data as a string to a hashmap.
     * Saves the hashmap to SavedData.json.
     *
     * @param entryManager the EntryManager instance to be saved.
     * @throws IOException              if there was an issue during write.
     * @throws IllegalArgumentException if entryManager is null.
     */
    public static void save(
            final EntryManager entryManager)
            throws IOException, IllegalArgumentException {

        save(entryManager, "SavedData.json");
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
    public static void save(
            final EntryManager entryManager,
            final String saveFile)
            throws IOException, IllegalArgumentException {

        if (entryManager == null || saveFile == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        JSONObject json = new JSONObject();

        HashMap<String, HashMap<String, String>> map = entryManager.toHashMap();
        for (String entryId : map.keySet()) {
            json.put(entryId, map.get(entryId));
        }

        File file = new File(saveFile);

        if (!file.exists() || !file.isFile()) {
            boolean created = file.createNewFile();
            assert created;
        }

        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(json.toString());
            writer.flush();
        } catch (IOException ignored) {
        }
    }

    /**
     * Loads SavedData.json and constructs LogEntries which it appends to the
     * provided EntryManager.
     *
     * @param entryManager the EntryManager to load data into.
     * @throws IOException              if SavedData.JSON could not be found.
     * @throws IllegalArgumentException if entryManager is null.
     */
    public static void load(
            final EntryManager entryManager)
            throws IOException, IllegalArgumentException {

        load(entryManager, "SavedData.json");
    }



    /**
     * Loads a specified JSON file and constructs
     * LogEntries which it appends to the provided EntryManager.
     *
     * @param entryManager the EntryManager to load data into.
     * @param saveFile     the path of the JSON file to load from.
     * @throws IOException
     * if the specified path could not be found.
     * @throws IllegalArgumentException if the entryManager or saveFile is null.
     */
    public static void load(
            final EntryManager entryManager,
            final String saveFile)
            throws IOException, IllegalArgumentException {

        if (entryManager == null || saveFile == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        File file = new File(saveFile);
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
            HashMap<String,HashMap<String, String>> jsonHash = new HashMap<>();


            for (String key : jsonObject.keySet()) {
                HashMap<String, String> entryHash = new HashMap<>();

                entryHash.put("title", jsonObject.getJSONObject(key).getString("title"));
                entryHash.put("comment", jsonObject.getJSONObject(key).getString("comment"));
                entryHash.put("date", jsonObject.getJSONObject(key).getString("date"));
                entryHash.put("feeling", jsonObject.getJSONObject(key).getString("feeling"));
                entryHash.put("duration", jsonObject.getJSONObject(key).getString("duration"));
                entryHash.put("distance", jsonObject.getJSONObject(key).getString("distance"));
                entryHash.put("maxHeartRate", jsonObject.getJSONObject(key).getString("maxHeartRate"));
                entryHash.put("exerciseCategory", jsonObject.getJSONObject(key).getString("exerciseCategory"));
                entryHash.put("exerciseSubcategory", jsonObject.getJSONObject(key).getString("exerciseSubcategory"));

                jsonHash.put(key, entryHash);

            }

            EntryManager.fromHash(jsonHash, entryManager);


        } catch (JSONException pException) {
            throw new IllegalStateException("Could not load data from file");
        }
    }
}
