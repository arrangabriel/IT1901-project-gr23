package localpersistence;

import java.util.HashMap;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.time.Duration;

import core.EntryManager;
import core.LogEntry;
import core.LogEntry.EXERCISECATEGORY;
import core.LogEntry.Subcategory;
import core.LogEntry.EntryBuilder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/** Class for saving and loading entryManagers to and from JSON files. */
public final class EntrySaverJson {

    /** Hidden constructor. */
    private EntrySaverJson() { }

    /**
     * Iterates over every entry in the provided EntryManager
     * and adds their data as a string to a hashmap.
     * Saves the hashmap to SavedData.json.
     * @param entryManager the EntryManager instance to be saved.
     * @throws IOException if there was an issue during write.
     * @throws IllegalArgumentException if entryManager is null.
     */
    public static void save(
        final EntryManager entryManager)
            throws IOException, IllegalArgumentException {

        save(entryManager, "SavedData.json");

    }

    /**
     * Puts the information stored in the enty into the hashmap.
     * @param map the map to put into.
     * @param entry the entry to store.
     */
    private static void putEntry(
        final HashMap<String, String> map,
        final LogEntry entry) {

        map.put("title", entry.getTitle());
        map.put("comment", entry.getComment());
        map.put("date", entry.getDate().toString());
        map.put("feeling", Integer.toString(entry.getFeeling()));
        map.put("duration", Long.toString(entry.getDuration().toSeconds()));
        map.put("distance", Double.toString(entry.getDistance()));
        map.put("maxHeartRate", Integer.toString(entry.getMaxHeartRate()));
        map.put("exerciseCategory", entry.getExerciseCategory().toString());
        map.put("exerciseSubCategory", entry.getExerciseSubCategory()
        .toString());
    }

    /**
     * Iterates over every entry in the provided EntryManager
     * and adds their data as a string to a hashmap.
     * Saves the hashmap to the specified JSON file.
     * @param entryManager the EntryManager instance to be saved.
     * @param saveFile path to the file being written to.
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

        for (String entryId : entryManager.entryIds()) {
            LogEntry entry = entryManager.getEntry(entryId);
            HashMap<String, String> innerMap = new HashMap<>();
            putEntry(innerMap, entry);

            json.put(entryId, innerMap);
        }
        File file = new File(saveFile);
        file.createNewFile();
        FileWriter writer = new FileWriter(file, Charset.forName("utf-8"));
        try {
            writer.write(json.toJSONString());
            writer.flush();   
        } catch (IOException e) {

        } finally {
            writer.close();
        }
    }

    /**
     * Loads SavedData.json and constructs LogEntries which it appends to the
     * provided EntryManager.
     * @param entryManager the EntryManager to load data into.
     * @throws FileNotFoundException    if SavedData.JSON could not be found.
     * @throws IllegalArgumentException if entryManager is null.
     */
    public static void load(
        final EntryManager entryManager)
            throws FileNotFoundException, IllegalArgumentException {

        load(entryManager, "SavedData.json");

    }

    /**
     * Converts a string representation of a subcategory into a subcategory.
     * @param category The string representation of the subcategory.
     * @return The actual subcategory or null if no match.
     */
    public static LogEntry.Subcategory stringToSubcategory(
        final String category) {

        LogEntry.Subcategory subCategory = null;
        outerloop:
        for (EXERCISECATEGORY exCategory: LogEntry.EXERCISECATEGORY.values()) {
            for (LogEntry.Subcategory sub: exCategory.getSubcategories()) {
                try {
                    subCategory = sub.getValueOf(category);
                    if (subCategory != null) {
                        break outerloop;
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
     * @param entryManager the EntryManager to load data into.
     * @param saveFile     the path of the JSON file to load from.
     * @throws FileNotFoundException   if the specified path could not be found.
     * @throws IllegalArgumentException if the entryManager or saveFile is null.
     */
    public static void load(
        final EntryManager entryManager,
        final String saveFile)
            throws FileNotFoundException, IllegalArgumentException {

        if (entryManager == null || saveFile == null) {

            throw new IllegalArgumentException("Arguments cannot be null");

        }

        JSONParser jsonParser = new JSONParser();
        File file = new File(saveFile);
        Scanner reader = new Scanner(file, "utf-8");
        String dataString = "";

        while (reader.hasNextLine()) {
            dataString += reader.nextLine();
        }
        reader.close();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(dataString);

            for (Object key : jsonObject.keySet()) {

                //Suppressed unchecked warning. Any better solution Stefan?:
                @SuppressWarnings("unchecked")
                HashMap<String, String> innerMap =
                            (HashMap<String, String>) jsonObject.get(key);

                String title = innerMap.get("title");
                LocalDate date = LocalDate.parse(innerMap.get("date"));
                String comment = null;
                Double distance = null;
                Integer maxHeartRate = null;
                Integer feeling = Integer.parseInt(innerMap.get("feeling"));

                if (!innerMap.get("distance").equals("null")) {
                    distance = Double.parseDouble(
                        innerMap.get("distance"));
                }
                if (!innerMap.get("maxHeartRate").equals("null")) {
                    maxHeartRate = Integer.parseInt(
                        innerMap.get("maxHeartRate"));
                }
                if (!innerMap.get("comment").equals("null")) {
                    comment = innerMap.get("comment");
                }

                Duration duration = Duration.ofSeconds(
                    Long.parseLong(innerMap.get("duration")));

                EXERCISECATEGORY category = EXERCISECATEGORY.valueOf(
                    innerMap.get("exerciseCategory"));

                Subcategory subCategory = stringToSubcategory(
                    innerMap.get("exerciseSubcategory"));

                EntryBuilder builder = new EntryBuilder(
                    title, date, duration, category, feeling)
                    .comment(comment)
                    .distance(distance)
                    .exerciseSubcategory(subCategory)
                    .maxHeartRate(maxHeartRate);


                entryManager.addEntry(builder.build());
            }

        } catch (ParseException pException) {
            throw new IllegalStateException("Could not load data from file");
        }
    }
}
