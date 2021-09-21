package localpersistence;

import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import core.LogEntry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EntrySaverJson {

    
    private void save(HashMap<String, LogEntry> logEntry) throws IOException {

        JSONObject json = new JSONObject();
        

        for (String key : logEntry.keySet()){
            HashMap<String, String> innerMap = new HashMap<String, String>();
            innerMap.put("title", logEntry.get(key).getTitle());
            innerMap.put("comment", logEntry.get(key).getComment());
            innerMap.put("date", logEntry.get(key).getDate().toString());
            innerMap.put("duration", String.valueOf(logEntry.get(key).getDuartion().getSeconds()));

            json.put(key, innerMap);
        }
        File file = new File("SavedData.json");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(json.toJSONString());
        writer.close();
        }

    
}
