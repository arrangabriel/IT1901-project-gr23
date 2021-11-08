package restserver;

import core.EntryManager;
import core.LogEntry;
import core.LogEntry.EXERCISECATEGORY;
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

public class GetfitController {

    //@Autowired
    private final GetfitService getfitService = new GetfitService();

    @GetMapping("/{entryId}")
    public LogEntry getLogEntry(final @PathVariable("entryId") String id) {
        return getfitService.getEntryManager().getEntry(id);
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
    public List<LogEntry> getListOfLogEntries(
            final @RequestParam(value = "s") String sortType,
            final @RequestParam(value = "r", defaultValue = "false")
                    String reverse,
            final @RequestParam(value = "c", required = false) String category,
            final @RequestParam(value = "cd", required = false)
                    String subcategory,
            final @RequestParam(value = "d", required = false) String date) {

        LogEntry.SORTCONFIGURATIONS sortConfiguration = null;

        try {
            sortConfiguration = LogEntry.SORTCONFIGURATIONS.valueOf(sortType);
        } catch (IllegalArgumentException IA) {
        }

        EntryManager.SortedIteratorBuilder iteratorBuilder =
                new EntryManager.SortedIteratorBuilder(
                        getfitService.getEntryManager(),
                        sortConfiguration);
        try {
            LogEntry.EXERCISECATEGORY categories =
                    LogEntry.EXERCISECATEGORY.valueOf(category);
            iteratorBuilder =
                    iteratorBuilder.filterExerciseCategory(categories);

            LogEntry.Subcategory subcategories = null;

            switch (category) {
                case "STRENGTH" -> {
                    subcategories =
                            LogEntry.STRENGTHSUBCATEGORIES.valueOf(category);
                }
                case "SWIMMING", "CYCLING", "RUNNING" -> {
                    subcategories =
                            LogEntry.CARDIOSUBCATEGORIES.valueOf(category);
                }
                default -> {
                }
            }
            iteratorBuilder = iteratorBuilder.filterSubCategory(subcategories);
        } catch (IllegalArgumentException IA) {
        }

        List<LogEntry> returnList = new ArrayList<>();
        iteratorBuilder.iterator(Boolean.parseBoolean(reverse))
                .forEachRemaining(returnList::add);


        return returnList;
    }

    @PostMapping("/add")
    public String addLogEntry(final @RequestBody String logEntry) {

        getfitService.getEntryManager().addEntry(stringToEntry(logEntry));
        getfitService.save();
        return "{\"id\":\"" + getfitService.getEntryManager()
                .addEntry(stringToEntry(logEntry)) + "\" }";
    }


    @PostMapping("edit/{entryId}")
    public void editLogEntry(final @PathVariable("entryId") String id,
                             final @RequestBody String logEntry) {
        getfitService.getEntryManager().swapEntry(id, stringToEntry(logEntry));
        getfitService.save();
    }


    @PostMapping("remove/{entryId}")
    public void removeLogEntry(final @PathVariable("entryId") String id) {
        getfitService.getEntryManager().removeEntry(id);
        getfitService.save();

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
