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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EntrySaverJson {

    public void save(EntryManager entryManager) throws IOException {
        save(entryManager, "SavedData.json");
    }
    
    public void save(EntryManager entryManager, String saveFile) throws IOException {

        JSONObject json = new JSONObject();
        

        for (String key : entryManager){
            HashMap<String, String> innerMap = new HashMap<String, String>();
            innerMap.put("title", entryManager.getEntry(key).getTitle());
            innerMap.put("comment", entryManager.getEntry(key).getComment());
            innerMap.put("date", entryManager.getEntry(key).getDate().toString());
            innerMap.put("duration", String.valueOf(entryManager.getEntry(key).getDuartion().getSeconds()));

            json.put(key, innerMap);
        }
        File file = new File(saveFile);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(json.toJSONString());
        writer.flush();

        writer.close();
        }
    
    public void load(EntryManager entryManager) throws FileNotFoundException {
        load(entryManager, "SavedData.json");
    }

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
