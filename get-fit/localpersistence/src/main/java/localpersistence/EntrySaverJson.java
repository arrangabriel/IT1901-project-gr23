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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EntrySaverJson {

    /**
     * Iterates over every entry in the provided EntryManager and adds their data as a string to a hashmap.
     * Saves the hashmap to SavedData.json.
     * @param entryManager
     * @throws IOException
     */
    public void save(EntryManager entryManager) throws IOException {
        save(entryManager, "SavedData.json");
    }
    
    /**
     * Iterates over every entry in the provided EntryManager and adds their data as a string to a hashmap.
     * Saves the hashmap to a specified JSON file.
     * @param entryManager
     * @param saveFile
     * @throws IOException
     */
    public void save(EntryManager entryManager, String saveFile) throws IOException {

        JSONObject json = new JSONObject();
        

        for (LogEntry entry : entryManager){
            HashMap<String, String> innerMap = new HashMap<String, String>();
            innerMap.put("title", entry.getTitle());
            innerMap.put("comment", entry.getComment());
            innerMap.put("date", entry.getDate().toString());
            innerMap.put("duration", String.valueOf(entry.getDuartion().getSeconds()));

            json.put(entry.getId(), innerMap);
        }
        File file = new File(saveFile);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(json.toJSONString());
        writer.flush();

        writer.close();
        }
    
    /**
     * Loads SavedData.json and constructs LogEntries which it appends to the provided EntryManager.
     * @param entryManager
     * @throws FileNotFoundException
     */
    public void load(EntryManager entryManager) throws FileNotFoundException {
        load(entryManager, "SavedData.json");
    }

    /**
     * Loads a specified JSON file and constructs LogEntries which it appends to the provided EntryManager.
     * @param entryManager
     * @param saveFile
     * @throws FileNotFoundException
     */
    public void load(EntryManager entryManager, String saveFile) throws FileNotFoundException {
        
        JSONParser jsonParser = new JSONParser();
        File file = new File(saveFile);
        Scanner reader = new Scanner(file);
        String dataString = "";

        while (reader.hasNextLine()){
            dataString += reader.nextLine();
        }
        reader.close();

        try{
            JSONObject jsonObject = (JSONObject) jsonParser.parse(dataString);

            for (Object key : jsonObject.keySet()){
                String id = (String) key;
                
                //Suppressed unchecked warning. Any better solution Stefan?:
                @SuppressWarnings("unchecked")
                HashMap<String, String> innerMap = (HashMap<String, String>) jsonObject.get(key);
                
                entryManager.addEntry(
                    id, 
                    innerMap.get("title"),
                    innerMap.get("comment"),
                    LocalDate.parse(innerMap.get("date")),
                    Duration.ofSeconds(Long.parseLong(innerMap.get("duration"))));
                }

        }
        catch (ParseException pException){
            throw new IllegalStateException("Could not load data from file");
        }
    }
}
