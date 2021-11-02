package restapi;


import java.util.Arrays;
import java.util.HashMap;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    /*@GetMapping("/list")
    @ResponseBody
    public String getListOfLogEntries(
        @RequestParam(value = "s", required = false) String sortType, 
        @RequestParam("r") String revert, 
        @RequestParam(value ="c", required = false) String category, 
        @RequestParam(value ="cd", required = false) String subcategory,
        @RequestParam(value ="d", required = false) String date) {

        if (!sortType.isEmpty()) {
            
        }
    }
    */

}