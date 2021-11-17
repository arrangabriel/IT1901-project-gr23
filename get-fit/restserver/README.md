# Rest Server

This modules handles the Service layer (rest api). Its purpose is to transfer data to and from the service layer. The module provide access to the following classes:

- GetfitApplication
- GetfitController
- GetfitService

## GetfitApplication
Spring boot application class. It starts the server.

### Methods
- main(String...): Main method for starting the application. It access the method run in SpringApplication with GetfitApplication.class and the given strings as arguments.

## GetfitController
Controller class for handling the get and post requests. It constists of a GetfitService.

## Methods
The payload, response and endpoints shown in **[Schema](/get-fit/schema.md/)**.
The methods are divided into post, get and exception. The tags for the categories are simplified and they are not equal to the tags in the code. 

@Get
- getLogEntry(String) -> String: Return the entry given by the inputed id. It is returned as a string according to Schema.md.

- getFilters() -> String: Returns the possible ways to filter logEntry as a string according to Schema.md.

- getListOfLogEntries(String, String, String, String, String) -> String: It gets the entries which fit into the inputed filters and sorting criteria. The parameters are sorting type (default date), reverse (default false), category (not required), subcategory (not reguired) and date (for filtering, not required). It returns a string with the different entries according to Schema.md.

@Post
- addLogEntry(String) -> String: The inputed string is an entry in as a String and it adds the entry to the entry manager in the GetfitService.

- editLogEntry(String, String): The method gets an entry id and a logEntry as a String (according to Schema.md payload) and replaces the already existing entry with this entry, but keeps the same id.

- removeLogEntry(String): The logEntry with the same id as the input, is removed from the entryManager in GetfitService.

@ExceptionHandler
- handleIllegalArgumentException(IllegalAccessException) -> String: Returns the exception message as a String.
- handleIOException(IOException) -> String: Returns the exception message as a String.
- handleIllegalArgumentException(NoSuchElementException) -> String: Returns the exception message as a String.
