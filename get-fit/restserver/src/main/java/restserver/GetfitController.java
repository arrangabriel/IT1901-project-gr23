package restserver;

import core.EntryManager;
import core.LogEntry;
import core.LogEntry.EXERCISECATEGORY;
import math.Statistics;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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

    @GetMapping(value="/{entryId}", produces = "application/json")
    public String getLogEntry(final @PathVariable("entryId") String id) {
        JSONObject returnObject;
        try {
            returnObject = new JSONObject(getfitService.getEntryManager().getEntry(id).toHashMap());
        } catch (IllegalArgumentException e) {
            throw new NoSuchElementException(HttpStatus.NOT_FOUND + "Entry not found" + e);
        }
        return returnObject.toString();
    }

    @GetMapping(value="/filters", produces="application/json")
    public String getFilters() {
        //HashMap<String, String> filters = new HashMap<>();
        JSONObject filters = new JSONObject();
        JSONObject categories = new JSONObject();

        for (EXERCISECATEGORY category : EXERCISECATEGORY.values()) {
            JSONArray subCategories = new JSONArray();
            for (LogEntry.Subcategory subcategory : category.getSubcategories()) {
                subCategories.put(subcategory.toString().toLowerCase());
            }
            categories.put(category.toString().toLowerCase(),
                    subCategories);
        }
        filters.put("categories", categories);
        return filters.toString();
    }

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

        LogEntry.SORTCONFIGURATIONS sortConfiguration = null;

        try {
            sortConfiguration =
                    LogEntry.SORTCONFIGURATIONS.valueOf(sortType.toUpperCase());
        } catch (IllegalArgumentException IA) {
        }

        EntryManager.SortedIteratorBuilder iteratorBuilder =
                new EntryManager.SortedIteratorBuilder(
                        getfitService.getEntryManager(),
                        sortConfiguration);
        if (category != null) {
            category = category.toUpperCase();
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
                                LogEntry.CARDIOSUBCATEGORIES.valueOf(category);
                    }
                    default -> {
                    }
                }
                iteratorBuilder =
                        iteratorBuilder.filterSubCategory(subcategories);
            } catch (IllegalArgumentException IA) {
            }

            try{
                if(date != null){
                    iteratorBuilder = iteratorBuilder.filterTimeInterval(
                    LocalDate.parse(date.substring(0,10)),LocalDate.parse(date.substring(11)));
                }
            }catch(IllegalArgumentException IA){

            }
        }

        List<LogEntry> returnList = new ArrayList<>();
        iteratorBuilder.iterator(Boolean.parseBoolean(reverse))
                .forEachRemaining(returnList::add);

        JSONObject returnJSON = new JSONObject();
        JSONArray entryArray = new JSONArray();

        for (LogEntry entry : returnList) {
            entryArray.put(entry.toHashMap());
        }

        returnJSON.put("entries", entryArray);

        return returnJSON.toString();
    }

    @GetMapping("/stats")
    @ResponseBody
    public String getStatisticsData(
            final @RequestParam(value = "d") String date,
            final @RequestParam(value = "c", required = false)
                    String eCategory) {


        HashMap<String, String> map = new HashMap<>();

        if (getfitService.getEntryManager().entryCount() == 0) {
                map.put("empty", "True");
        }

        else {
                map.put("empty", "False");
        }

        map.put("count", Integer.toString(Statistics.getCount(
                getfitService.getEntryManager(), 
                eCategory,
                date)));

        map.put("totalDuration", GetfitService.convertFromSecondsToHours(
                Statistics.getTotalDuration(
                getfitService.getEntryManager(), 
                eCategory, 
                date)));

        map.put("averageDuration", GetfitService.convertFromSecondsToHours(
                Statistics.getAverageDuration(
                getfitService.getEntryManager(), 
                eCategory, 
                date)));
        

        map.put("averageFeeling", Double.toString(Statistics.getAverageFeeling(
                getfitService.getEntryManager(), 
                eCategory, 
                date)));
        
        double speed = Statistics.getAverageSpeed(
                getfitService.getEntryManager(),
                eCategory, date);

        map.put("averageSpeed", Double.toString(speed));

        map.put("maximumHr", Double.toString(Statistics.getMaximumHr(
                getfitService.getEntryManager(), 
                eCategory, 
                date)));

        JSONObject JSONreturn = new JSONObject(map);


        return JSONreturn.toString();
    }

    @GetMapping(value="/chart", produces = "application/json")
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
        JSONObject JSONreturn = new JSONObject(map);

        return JSONreturn.toString();
    }

    @PostMapping(value="/add", produces = "application/json")
    public String addLogEntry(final @RequestBody String logEntry) {

        String id = getfitService.getEntryManager().addEntry(stringToEntry(logEntry));
        getfitService.save();
        return "{\"id\":\"" + id + "\" }";
    }


    @PostMapping(value="edit/{entryId}", produces = "application/json")
    public void editLogEntry(final @PathVariable("entryId") String id,
                             final @RequestBody String logEntry) {
        getfitService.getEntryManager().swapEntry(id, stringToEntry(logEntry));
        getfitService.save();
    }


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

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleIllegalArgumentException(
            final NoSuchElementException rse) {
        return rse.getMessage();
    }
}
