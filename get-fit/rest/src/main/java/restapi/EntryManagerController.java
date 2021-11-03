package restapi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import core.EntryManager;
import core.LogEntry;
import core.LogEntry.EXERCISECATEGORY;

//localhost:8080/api/v1/entries
//Run: mvn spring-boot:run

@RestController
@RequestMapping("/api/v1/entries")
public class EntryManagerController {

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
                    Arrays.toString(categories.getSubcategories()).toLowerCase());
        }
        return "categories: " + filters.toString().replace("=", ": ");
    }

    @GetMapping("/list")
    @ResponseBody
    public List<LogEntry> getListOfLogEntries(@RequestParam(value = "s") String sortType,
            @RequestParam(value = "r", defaultValue = "false") String reverse,
            @RequestParam(value = "c", required = false) String category,
            @RequestParam(value = "cd", required = false) String subcategory,
            @RequestParam(value = "d", required = false) String date) {

        LogEntry.SORTCONFIGURATIONS sortConfiguration = null;

        try {
            sortConfiguration = LogEntry.SORTCONFIGURATIONS.valueOf(sortType);
        } catch (IllegalArgumentException IA) {
        }
        EntryManager.SortedIteratorBuilder iteratorBuilder = new EntryManager.SortedIteratorBuilder(entryManager,
                sortConfiguration);
        try {
            LogEntry.EXERCISECATEGORY categories = LogEntry.EXERCISECATEGORY.valueOf(category);
            iteratorBuilder = iteratorBuilder.filterExerciseCategory(categories);

            LogEntry.Subcategory subcategories = null;

            switch (category) {
            case "STRENGTH":
                 subcategories = LogEntry.STRENGTHSUBCATEGORIES.valueOf(category);
                 break;

            case "SWIMMING", "CYCLING", "RUNNING":
                 subcategories = LogEntry.STRENGTHSUBCATEGORIES.valueOf(category);
                break;
            default:
                 break;
            }
            iteratorBuilder = iteratorBuilder.filterSubCategory(subcategories);
        } catch (IllegalArgumentException IA) {
            
        }

        List<LogEntry> returnList = new ArrayList<LogEntry>();
        iteratorBuilder.iterator(Boolean.valueOf(reverse)).forEachRemaining(returnList :: add);

        return returnList;
    }
    

}