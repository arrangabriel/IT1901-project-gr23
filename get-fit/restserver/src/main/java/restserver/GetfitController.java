package restserver;

import core.CardioSubCategory;
import core.EntryManager;
import core.ExerciseCategory;
import core.LogEntry;
import core.SortConfiguration;
import core.StrengthSubCategory;
import core.Subcategory;

import org.json.JSONArray;
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
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;


//localhost:8080/api/v1/entries
//Run: mvn spring-boot:run

@RestController
@RequestMapping("/api/v1/entries")

public class GetfitController {

    //@Autowired
    private final GetfitService getfitService = new GetfitService();

    /**
     * Gets the entry given by the inputed id.
     * @param id the entry id
     * @return The entry returned as a String.
     */
    @GetMapping(value="/{entryId}", produces = "application/json")
    public String getLogEntry(final @PathVariable("entryId") String id) {
        JSONObject returnObject;
        try {
            returnObject = new JSONObject(getfitService.getEntryManager().getEntry(id).toHash());
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException(HttpStatus.NOT_FOUND + "Entry not found" + e);
        }
        return returnObject.toString();
    }

    /**
     * Gets the possible ways to filter logEntry as a string.
     * @return The filters
     */
    @GetMapping(value="/filters", produces="application/json")
    public String getFilters() {
        //HashMap<String, String> filters = new HashMap<>();
        JSONObject filters = new JSONObject();
        JSONObject categories = new JSONObject();

        for (core.ExerciseCategory category : core.ExerciseCategory.values()) {
            JSONArray subCategories = new JSONArray();
            for (core.Subcategory subcategory : category.getSubcategories()) {
                subCategories.put(subcategory.toString().toLowerCase());
            }
            categories.put(category.toString().toLowerCase(),
                    subCategories);
        }
        filters.put("categories", categories);
        return filters.toString();
    }

    /**
     * Gets the entries which fit into the given filters
     *  and sorting criteria.
     * @param sortType Type to sort by (ex: date).
     * @param reverse Reverse sorting True/False.
     * @param category Category to filter by.
     * @param subcategory Subcategory to filter by.
     * @param date Date to filter by.
     * @return Sorted entries that matches the filters.
     */
    @GetMapping(value="/list", produces = "application/json")
    @ResponseBody
    public String getListOfLogEntries(
            final @RequestParam(value = "s", defaultValue = "date")
                    String sortType,
            final @RequestParam(value = "r", defaultValue = "false")
                    String reverse,
            @RequestParam(value = "c", required = false) String category,
            final @RequestParam(value = "cd", required = false)
                    String subcategory,
            final @RequestParam(value = "d", required = false) String date) {

        SortConfiguration sortConfiguration = null;

        try {
            sortConfiguration =
                    SortConfiguration.valueOf(sortType.toUpperCase());
        } catch (IllegalArgumentException IA) {
        }

        EntryManager.SortedIteratorBuilder iteratorBuilder =
                new EntryManager.SortedIteratorBuilder(
                        getfitService.getEntryManager(),
                        sortConfiguration);
        if (category != null) {
            category = category.toUpperCase();
            try {
                ExerciseCategory categories =
                        ExerciseCategory.valueOf(category);
                iteratorBuilder =
                        iteratorBuilder.filterExerciseCategory(categories);

                Subcategory subcategories = null;

                switch (category) {
                    case "STRENGTH" -> {
                        subcategories =
                                StrengthSubCategory.valueOf(
                                        category);
                    }
                    case "SWIMMING", "CYCLING", "RUNNING" -> {
                        subcategories =
                                CardioSubCategory.valueOf(category);
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

        JSONObject returnJSON = new JSONObject();
        JSONArray entryArray = new JSONArray();

        for (LogEntry entry : returnList) {
            entryArray.put(entry.toHash());
        }

        returnJSON.put("entries", entryArray);

        return returnJSON.toString();
    }

    /**
     * It adds the entry to the entry manager in GetfitService.
     * @param logEntry the entry to add.
     * @return The id of the added entry.
     */
    @PostMapping(value="/add", produces = "application/json")
    public String addLogEntry(final @RequestBody String logEntry) {

        String id = getfitService.getEntryManager().addEntry(stringToEntry(logEntry));
        getfitService.save();
        return "{\"id\":\"" + id + "\" }";
    }

    /**
     * Replaces the already existing entry with this entry,
     *  but keeps the same id.
     * @param id the id of the entry to swap.
     * @param logEntry the new entry.
     */
    @PostMapping(value="edit/{entryId}", produces = "application/json")
    public void editLogEntry(final @PathVariable("entryId") String id,
                             final @RequestBody String logEntry) {
        getfitService.getEntryManager().swapEntry(id, stringToEntry(logEntry));
        getfitService.save();
    }

    /**
     *  The logEntry with the same id as the input,
     *  is removed from the entryManager.
     * @param id The id of the entry to remove.
     */
    @PostMapping(value="remove/{entryId}", produces = "application/json")
    public void removeLogEntry(final @PathVariable("entryId") String id) {
        if (getfitService.getEntryManager().removeEntry(id)) {
            getfitService.save();
        } else {
            throw new NoSuchElementException(HttpStatus.NOT_FOUND + "Entry not found");
        }

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

    /**
     * Handles IllegalArgumentException
     * @param ia IllegalAccessException
     * @return The exception message as a String.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIllegalArgumentException(
            final IllegalAccessException ia) {
        return ia.getMessage();
    }

    /**
     * Handles IOException.
     * @param io IOException.
     * @return The exception message as a String.
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIOException(final IOException io) {
        return io.getMessage();
    }

    /**
     * Handles IllegalArgumentException.
     * @param rse NoSuchElementException
     * @return The exception message as a String.
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleIllegalArgumentException(
            final NoSuchElementException rse) {
        return rse.getMessage();
    }
}
