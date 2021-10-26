package localpersistence;

import core.EntryManager;
import core.LogEntry;
import core.LogEntry.EXERCISECATEGORY;
import core.LogEntry.EntryBuilder;
import core.LogEntry.Subcategory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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
     * Puts the information stored in the entry into the hashmap.
     *
     * @param map   the map to put into.
     * @param entry the entry to store.
     */
    private static void putEntry(
            final HashMap<String, String> map,
            final LogEntry entry) {

        map.put("title", entry.getTitle());

        if (entry.getComment() != null) {
            map.put("comment", entry.getComment());
        } else {
            map.put("comment", "null");
        }

        map.put("date", entry.getDate().toString());
        map.put("feeling", Integer.toString(entry.getFeeling()));
        map.put("duration", Long.toString(entry.getDuration().toSeconds()));

        if (entry.getDistance() != null) {
            map.put("distance", Double.toString(entry.getDistance()));
        } else {
            map.put("distance", "null");
        }

        if (entry.getMaxHeartRate() != null) {
            map.put("maxHeartRate", Integer.toString(entry.getMaxHeartRate()));
        } else {
            map.put("maxHeartRate", "null");
        }

        map.put("exerciseCategory", entry.getExerciseCategory().toString());

        if (entry.getExerciseSubCategory() != null) {
            map.put("exerciseSubCategory", entry.getExerciseSubCategory()
                    .toString());
        } else {
            map.put("exerciseSubCategory", "null");
        }
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
            writer.write(json.toJSONString());
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
     * Converts a string representation of a subcategory into a subcategory.
     *
     * @param category The string representation of the subcategory.
     * @return The actual subcategory or null if no match.
     */
    public static LogEntry.Subcategory stringToSubcategory(
            final String category) {

        LogEntry.Subcategory subCategory = null;
        outerLoop:
        for (EXERCISECATEGORY exCategory : LogEntry.EXERCISECATEGORY.values()) {
            for (LogEntry.Subcategory sub : exCategory.getSubcategories()) {
                try {
                    subCategory = sub.getValueOf(category);
                    if (subCategory != null) {
                        break outerLoop;
                    }
                } catch (Exception e) {
                    // NEQ
                }
            }
        }
        return subCategory;
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

        JSONParser jsonParser = new JSONParser();
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
            JSONObject jsonObject = (JSONObject) jsonParser.parse(dataString);

            EntryManager.fromHash(jsonObject);

        } catch (ParseException pException) {
            throw new IllegalStateException("Could not load data from file");
        }
    }
}
