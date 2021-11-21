package restserver;

import core.CardioSubCategory;
import core.EntryManager;
import core.ExerciseCategory;
import core.LogEntry;
import core.SortConfiguration;
import core.StrengthSubCategory;
import core.Subcategory;
import math.Statistics;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/entries")
public class GetFitController {
    /**
     * Length of one piece of date format.
     */
    private static final int DATE_FORMAT_LENGTH = 10;
    /**
     * Core accessor.
     */
    private final GetFitService getfitService = new GetFitService();

    /**
     * Gives an entry by its id.
     *
     * @param id an integer.
     * @return a HTTP request.
     */
    @GetMapping(value = "/{entryId}", produces = "application/json")
    public String getLogEntry(final @PathVariable("entryId") String id) {
        JSONObject returnObject;
        try {
            returnObject = new JSONObject(
                getfitService.getEntryManager().getEntry(id).toHash());
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException(
                HttpStatus.NOT_FOUND + "Entry not found" + e);
        }
        return returnObject.toString();
    }

    /**
     * Gets all possible sortings/filters.
     *
     * @return a HTTP request.
     */
    @GetMapping(value = "/filters", produces = "application/json")
    public String getFilters() {

        JSONObject filters = new JSONObject();
        JSONObject categories = new JSONObject();

        for (core.ExerciseCategory category : core.ExerciseCategory.values()) {
            JSONArray subCategories = new JSONArray();
            for (core.Subcategory subcategory : category.getSubcategories()) {
                subCategories.put(subcategory.toString().toLowerCase());
            }
            categories.put(category.toString().toLowerCase(), subCategories);
        }

        filters.put("categories", categories);
        return filters.toString();
    }

    /**
     * Gets a sorted list of entries.
     *
     * @param sortType    the sorting configuration.
     * @param reverse     whether to reverse the list.
     * @param category    the category to filter by.
     * @param subCategory the sub-category to filter by.
     * @param date        the date interval to filter by.
     * @return a HTTP request.
     */
    @GetMapping(value = "/list", produces = "application/json")
    @ResponseBody
    public String getListOfLogEntries (
            final @RequestParam(value = "s", defaultValue = "date")
                    String sortType,
            final @RequestParam(value = "r", defaultValue = "false")
                    String reverse,
            final @RequestParam(value = "c", required = false) 
                    String category,
            final @RequestParam(value = "sc", required = false)
                    String subCategory,
            final @RequestParam(value = "d", required = false)
                    String date)
            throws IllegalAccessException {
        SortConfiguration sortConfiguration = null;

        try {
            sortConfiguration =
                    SortConfiguration.valueOf(sortType.toUpperCase());
        } catch (IllegalArgumentException ignored) {}

        EntryManager.SortedIteratorBuilder iteratorBuilder =
                new EntryManager.SortedIteratorBuilder(
                        getfitService.getEntryManager(),
                        sortConfiguration);
        if (category != null) {
            String categoryUpper = category.toUpperCase();
            try {
                ExerciseCategory categories =
                        ExerciseCategory.valueOf(categoryUpper);
                iteratorBuilder =
                        iteratorBuilder.filterExerciseCategory(categories);

                Subcategory subcategories;

                if (subCategory != null) {
                    subcategories = switch (categoryUpper) {
                        case "STRENGTH" -> StrengthSubCategory.valueOf(
                                subCategory.toUpperCase());
                        case "SWIMMING", "CYCLING", "RUNNING" ->
                                CardioSubCategory.valueOf(
                                subCategory.toUpperCase());
                        default -> null;
                    };

                    iteratorBuilder =
                            iteratorBuilder.filterSubCategory(subcategories);
                }
            } catch (IllegalArgumentException ignored) {
            }

        } else {
            if (subCategory != null) {
                throw new IllegalAccessException();
            }
        }

        try {
            if (date != null) {
                iteratorBuilder = iteratorBuilder.filterTimeInterval(
                        LocalDate.parse(date.substring(0,
                                DATE_FORMAT_LENGTH)),
                        LocalDate.parse(
                                date.substring(DATE_FORMAT_LENGTH + 1)));
            }
        } catch (IllegalArgumentException ignored) {
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
     * Gets statistic data about saved entries.
     *
     * @param date      the date interval to filter by.
     * @param eCategory the category to filter by.
     * @return a HTTP request.
     */
    @SuppressWarnings("checkstyle:FinalParameters")
    @GetMapping(value = "/stats", produces = "application/json")
    @ResponseBody
    public String getStatisticsData(
            final @RequestParam(value = "d") String date,
            @RequestParam(value = "c", required = false)
            final String eCategory) {

        String requestCategory = eCategory;
        if (requestCategory != null) {
            requestCategory = requestCategory.toUpperCase();
        }
        HashMap<String, String> map = new HashMap<>();

        if (getfitService.getEntryManager().entryCount() == 0) {
            map.put("empty", "True");
        } else {
            map.put("empty", "False");
        }

        map.put("count", Integer.toString(Statistics.getCount(
                getfitService.getEntryManager(),
                requestCategory,
                date)));

        map.put("totalDuration", GetFitService.convertFromSecondsToHours(
                Statistics.getTotalDuration(
                        getfitService.getEntryManager(),
                        requestCategory,
                        date)));

        map.put("averageDuration", GetFitService.convertFromSecondsToHours(
                Statistics.getAverageDuration(
                        getfitService.getEntryManager(),
                        requestCategory,
                        date)));


        map.put("averageFeeling", Double.toString(Statistics.getAverageFeeling(
                getfitService.getEntryManager(),
                requestCategory,
                date)));

        double speed = Statistics.getAverageSpeed(
                getfitService.getEntryManager(),
                requestCategory, date);

        map.put("averageSpeed", Double.toString(speed));

        map.put("maximumHr", Double.toString(Statistics.getMaximumHr(
                getfitService.getEntryManager(),
                requestCategory,
                date)));

        JSONObject jsonReturn = new JSONObject(map);


        return jsonReturn.toString();
    }

    /**
     * Gets chart data for statistics.
     *
     * @param date the date interval to filter by.
     * @return a HTTP request.
     */
    @GetMapping(value = "/chart", produces = "application/json")
    @ResponseBody
    public String getChartData(
            final @RequestParam(value = "d") String date) {

        List<String> categorylist = Arrays.asList(
                "swimming", "running", "strength", "cycling");

        HashMap<String, String> map = new HashMap<>();

        for (String category : categorylist) {
            map.put(category, Integer.toString(Statistics.getCount(
                    getfitService.getEntryManager(),
                    category.toUpperCase(),
                    date)));
        }
        JSONObject jsonReturn = new JSONObject(map);

        return jsonReturn.toString();
    }

    /**
     * Saves a logEntry to the server.
     *
     * @param logEntry the json represented logEntry.
     * @return a HTTP request.
     */
    @PostMapping(value = "/add", produces = "application/json")
    public String addLogEntry(final @RequestBody String logEntry) {

        String id = getfitService.getEntryManager()
                .addEntry(stringToEntry(logEntry));
        getfitService.save();
        return "{\"id\":\"" + id + "\" }";
    }

    /**
     * Deletes an entry by its id.
     *
     * @param id the entry id to delete by.
     */
    @PostMapping(value = "remove/{entryId}", produces = "application/json")
    public void removeLogEntry(final @PathVariable("entryId") String id) {
        if (getfitService.getEntryManager().removeEntry(id)) {
            getfitService.save();
        } else {
            throw new NoSuchElementException(
                    HttpStatus.NOT_FOUND + "Entry not found");
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
     * Handles IllegalAccessExceptions.
     *
     * @param ia the exception.
     * @return the exception message.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIllegalAccessException(
            final IllegalAccessException ia) {
        return ia.getMessage();
    }

    /**
     * Handles IOExceptions.
     *
     * @param io the exception.
     * @return the exception message.
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIOException(final IOException io) {
        return io.getMessage();
    }

    /**
     * Handles NoSuchElementException.
     *
     * @param rse the exception.
     * @return the exception message.
     */
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNoSuchElementException(
            final NoSuchElementException rse) {
        return rse.getMessage();
    }
}
