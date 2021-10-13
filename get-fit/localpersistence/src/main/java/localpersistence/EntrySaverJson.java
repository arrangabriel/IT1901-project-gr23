package localpersistence;

import java.util.HashMap;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.time.Duration;

import core.EntryManager;
import core.LogEntry;
import core.LogEntry.EXERCISECATEGORY;
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
            innerMap.put("title", entry
                                    .getTitle());

            innerMap.put("comment", entry
                                    .getComment());

            innerMap.put("date", entry
                                    .getDate()
                                    .toString());

            innerMap.put("duration", String.valueOf(entry
                                                    .getDuration()
                                                    .getSeconds()));

            innerMap.put("feeling", Integer.toString(entry
                                                    .getFeeling()));

            innerMap.put("distance", String.valueOf(entry
                                                    .getDistance()));

            innerMap.put("maxHeartRate", String.valueOf(entry
                                                    .getMaxHeartRate()));

            innerMap.put("exerciseCategory", entry
                                                    .getExerciseCategory()
                                                    .toString());

            innerMap.put("exerciseSubCategory", entry
                                                    .getExerciseSubCategory()
                                                    .toString());

            json.put(entryId, innerMap);
        }
        File file = new File(saveFile);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(json.toJSONString());
        writer.flush();

        writer.close();
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
        Scanner reader = new Scanner(file);
        String dataString = "";

        while (reader.hasNextLine()) {
            dataString += reader.nextLine();
        }
        reader.close();

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(dataString);

            for (Object key : jsonObject.keySet()) {
                String id = (String) key;

                //Suppressed unchecked warning. Any better solution Stefan?:
                @SuppressWarnings("unchecked")
                HashMap<String, String> innerMap =
                            (HashMap<String, String>) jsonObject.get(key);

                LogEntry.Subcategory subCategory = null;
                outerloop:
                for (EXERCISECATEGORY category
                        : LogEntry.EXERCISECATEGORY.values()) {
                    for (LogEntry.Subcategory sub
                            : category.getSubcategories()) {
                       try {
                           subCategory = sub.getValueOf(innerMap.get(
                                                        "exerciseSubCategory"));
                           if (subCategory != null) {
                               break outerloop;
                           }
                       } catch (Exception e) {
                           // NEQ
                       }
                    }
                }

                String title = innerMap.get("title");
                LocalDate date = LocalDate.parse(innerMap.get("date"));
                String comment = innerMap.get("comment");
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

                Duration duration = Duration.ofSeconds(
                    Long.parseLong(innerMap.get("duration")));

                EXERCISECATEGORY category = EXERCISECATEGORY.valueOf(
                    innerMap.get("exerciseCategory"));


                EntryBuilder builder = new EntryBuilder(
                    title, date, duration, category, feeling)
                    .comment(comment)
                    .distance(distance)
                    .exerciseSubcategory(subCategory)
                    .maxHeartRate(maxHeartRate);


                entryManager.addEntry(builder);
            }

        } catch (ParseException pException) {
            throw new IllegalStateException("Could not load data from file");
        }
    }
}
