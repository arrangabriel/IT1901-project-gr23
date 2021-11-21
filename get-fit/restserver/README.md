# Restserver

This modules handles the Service layer (rest api). Its purpose is to transfer data to and from the service layer. The module provide access to the following classes:

- GetfitApplication
- GetfitController
- GetfitService

## GetfitApplication
Spring boot application class. It starts the server.

### Methods
- main(String...): Main method for starting the application. It access the method run in SpringApplication with GetfitApplication.class and the given strings as arguments.

- dummy() -> void: Has no function other than to pass checkstyle. Alternative breaks springboot.

## GetfitController
Controller class for handling the get and post requests. It constists of a GetfitService.

### Methods
The payload, response and endpoints shown in **[Schema](/get-fit/schema.md/)**.
The methods are divided into post, get and exception. The tags for the categories are simplified and they are not equal to the tags in the code. 

@Get
- getLogEntry(String) -> String: Gets the entry given by the inputed id. It is returned as a string according to Schema.md.

- getFilters() -> String: Gets the possible ways to filter logEntry as a string according to Schema.md.

- getListOfLogEntries(String, String, String, String, String) -> String: It gets the entries which fit into the inputed filters and sorting criteria. The parameters are sorting type (default date), reverse (default false), category (not required), subcategory (not reguired) and date (for filtering, not required). It returns a string with the different entries according to Schema.md.

- getStatisticsData(String, String) -> String: Get statistics based on the date and the category. The parameters are the date to filter by and the category to filter by. The category is not required. 

- getChartData(String) -> String: Get the entry count for the statistics chart. The parameter is date and it returns the count for each category filtered by the given date. 

@Post
- addLogEntry(String) -> String: It adds the entry to the entry manager in GetfitService.

- editLogEntry(String, String): The method gets an entry id and a logEntry as a String (according to Schema.md payload) and replaces the already existing entry with this entry, but keeps the same id.

- removeLogEntry(String): The logEntry with the same id as the input, is removed from the entryManager in GetfitService.

@ExceptionHandler
- handleIllegalArgumentException(IllegalAccessException) -> String: Returns the exception message as a String.
- handleIOException(IOException) -> String: Returns the exception message as a String.
- handleIllegalArgumentException(NoSuchElementException) -> String: Returns the exception message as a String.

## GetfitService
Gives the server access to methods from core and local-persistence. Contains an EntryManager.

### Methods
- GetFitService(): Create a new EntryManager object on initialization and loads data from local-persistence.

- load() -> void: Use EntrySaverJson from local-persistence to load the content of the save file to the entryManager.

- save() -> void: Use EntrySaverJson from local-persistence to save the state of the EntryManager to file.

- getEntryManager() -> EntryManager: Allows other classes to access the EntryManager of this GitFitService. It returns the EntryManager from this GetFitService.


