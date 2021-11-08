package restserver;

import core.EntryManager;
import core.LogEntry;
import core.LogEntry.EXERCISECATEGORY;
import localpersistence.EntrySaverJson;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//localhost:8080/api/v1/entries
//Run: mvn spring-boot:run

@RestController
@RequestMapping("/api/v1/entries")
public class EntryManagerController {

    //@Autowired
    private final EntryManager entryManager = new EntryManager();

    @GetMapping("/{entryId}")
    public LogEntry getLogEntry(@PathVariable("entryId") String id) {
        return entryManager.getEntry(id);
    }

    @GetMapping("/filters")
    public String getFilters() {
        HashMap<String, String> filters = new HashMap<>();

        for (EXERCISECATEGORY categories : EXERCISECATEGORY.values()) {
            filters.put(categories.toString().toLowerCase(),
                    Arrays.toString(categories.getSubcategories())
                            .toLowerCase());
        }
        return "{categories: " + filters.toString().replace("=", ": ") + "}";
    }

    @GetMapping("/list")
    @ResponseBody
    public String getListOfLogEntries(
            @RequestParam(value = "s", defaultValue = "date") String sortType,
            @RequestParam(value = "r", defaultValue = "false") String reverse,
            @RequestParam(value = "c", required = false) String category,
            @RequestParam(value = "cd", required = false) String subcategory,
            @RequestParam(value = "d", required = false) String date) {

        LogEntry.SORTCONFIGURATIONS sortConfiguration = null;

        try {
            sortConfiguration =
                    LogEntry.SORTCONFIGURATIONS.valueOf(sortType.toUpperCase());
        } catch (IllegalArgumentException IA) {
            // TODO: handle bad request
        }
        EntryManager.SortedIteratorBuilder iteratorBuilder =
                new EntryManager.SortedIteratorBuilder(entryManager,
                        sortConfiguration);
        if (category != null) {
            try {
                LogEntry.EXERCISECATEGORY categories =
                        LogEntry.EXERCISECATEGORY.valueOf(category);
                iteratorBuilder =
                        iteratorBuilder.filterExerciseCategory(categories);

                LogEntry.Subcategory subcategories = null;

                switch (category) {
                    case "STRENGTH" -> {
                        subcategories =
                                LogEntry.STRENGTHSUBCATEGORIES.valueOf(
                                        category);
                    }
                    case "SWIMMING", "CYCLING", "RUNNING" -> {
                        subcategories =
                                LogEntry.CARDIOSUBCATEGORIES.valueOf(
                                        category);
                    }
                    default -> {
                    }
                }
                iteratorBuilder =
                        iteratorBuilder.filterSubCategory(subcategories);
            } catch (IllegalArgumentException IA) {
            }
        }

        List<LogEntry> returnList = new ArrayList<>();
        iteratorBuilder.iterator(Boolean.parseBoolean(reverse))
                .forEachRemaining(returnList::add);

        JSONObject returnObject = new JSONObject(returnList);

        return returnObject.toString();
    }

    @PostMapping("/add")
    public String addLogEntry(final @RequestBody String logEntry) {

        entryManager.addEntry(stringToEntry(logEntry));
        save();
        return "{\"id\":\"" + entryManager.addEntry(stringToEntry(logEntry))
                + "\" }";
    }


    @PostMapping("edit/{entryId}")
    public void editLogEntry(final @PathVariable("entryId")
                                     String id,
                             final @RequestBody String logEntry) {

        entryManager.swapEntry(id, stringToEntry(logEntry));
        save();
    }


    @PostMapping("remove/{entryId}")
    public void removeLogEntry(final @PathVariable("entryId") String id) {
        entryManager.removeEntry(id);
        save();

    }

    private LogEntry stringToEntry(final String logEntry) {

        JSONObject jsonObject = new JSONObject(logEntry);
        HashMap<String, String> entryHash = new HashMap<>();

        entryHash.put("title", jsonObject.getString("title"));
        entryHash.put("comment", jsonObject.getString("comment"));
        entryHash.put("date", jsonObject.getString("date"));
        entryHash.put("feeling", jsonObject.getString("feeling"));
        entryHash.put("distance", jsonObject.getString("distance"));
        entryHash.put("duration", jsonObject.getString("duration"));
        entryHash.put("maxHeartRate", jsonObject.getString("maxHeartRate"));
        entryHash.put("exerciseCategory",
                jsonObject.getString("exerciseCategory"));
        entryHash.put("exerciseSubCategory",
                jsonObject.getString("exerciseSubCategory"));
        return LogEntry.fromHash(entryHash);
    }

    private void save() {
        try {
            EntrySaverJson.save(entryManager);
        } catch (IllegalArgumentException ia) {
        } catch (IOException io) {
        }
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIllegalArgumentException(
            final IllegalAccessException ia) {
        return ia.getMessage();
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIOException(final IOException io) {
        return io.getMessage();
    }
}