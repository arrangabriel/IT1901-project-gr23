# Client
Module for handling data transfer to and from the presentation layer.

Provides access to the following classes:
- LogClient
- HttpResponses
- ServerResponseException

## LogClient
Representation of a connection to a get-fit server. Attributes are a String for URL to remote server and an int for the port to the remote server.

### Methods
- LogClient(String, int): Constructs a LogClient from a builder.

- getLogEntry(String) -> HashMap<String, String>: Gets a LogEntry from the server. It throws an URISyntaxException if the id ruins the URI syntax, an InterruptedException if the request was interrupted before retreiving the http response, an ExecutionException if the request completed exceptionally or a ServerResponseException.

- getLogEntryList() -> List<HashMap<String, String>>: Get a list of log entries from the server without sorting or filtering. The method returns a list of log entries from the server represented as a hash map. It throws an URISyntaxException if the id ruins the URI syntax, an InterruptedException if the request was interrupted before retreiving the http response, an ExecutionException if the request completed exceptionally or a ServerResponseException.

- getLogEntryList(ListBuilder) -> List<HashMap<String, String>>: Gets a list of log entries from the server. The method returns a list of log entries from the server represented as a hash map. It throws an URISyntaxException if the id ruins the URI syntax, an InterruptedException if the request was interrupted before retreiving the http response, an ExecutionException if the request completed exceptionally or a ServerResponseException.

- addLogEntry(entry) -> void: Adds a log entry on the server. The parameter is the HashMap representing the log entry to add. It throws an URISyntaxException if the id ruins the URI syntax, an InterruptedException if the request was interrupted before retreiving the http response, an ExecutionException if the request completed exceptionally or a ServerResponseException.

- updateLogEntry(String, HashMap<String, String>) -> String: Updates a log entry on the server. The parameters are the id and the entry HashMap representing the log entry to update. The id of the updateed log entry. It throws an URISyntaxException if the id ruins the URI syntax, an InterruptedException if the request was interrupted before retreiving the http response, an ExecutionException if the request completed exceptionally or a ServerResponseException.

- getExerciseCategories() -> HashMap<String, List<String>>: Retrieves a list of exercise categories from the server. It returns a list of exercise categories from the server represented as a hash map. It throws an URISyntaxException if the id ruins the URI syntax, an InterruptedException if the request was interrupted before retreiving the http response, an ExecutionException if the request completed exceptionally or a ServerResponseException.

- deleteLogEntry(String) -> void: Deletes a log entry from the server. The param is the id of the log entry to delete. It throws an URISyntaxException if the id ruins the URI syntax, an InterruptedException if the request was interrupted before retreiving the http response, an ExecutionException if the request completed exceptionally or a ServerResponseException.


**ListBuilder class**

Builder for filtering and sorting the list of log entries.

**Methods**
- reverse() -> ListBuilder: Set reverse. Returns a ListBuilder instance.

- sort(String) -> ListBuilder: Set sorting. Parameter is the sorting configuration. Returns a ListBuilder instance.

- category(String) -> ListBuilder: Set category filtering. The category to filter by. It returns a a ListBuilder instance.

- subCategory(String) -> ListBuilder: Set subCategory filtering. The parameter is subCategory. It returns a ListBuilder instance.

- date(String) -> ListBuilder: Set date filtering. The parameter is date and it returns a ListBuilder instance.

## HttpResponses
Http responses. Consists of a Map<Integer, String> with responses: 200: ok, 400: Bad Request, 404: Not Found, 500: Internal Server Error.

### Methods
- getResponseText(int) -> String: Returns the response matching with the parameter code.

## ServerResponseException
Extends the Exception interface. Containes an int with the code.

- ServerResponseException(String, int): Innherets the constructor from Exception and gives it the message as a String. The int is set as the code.

- getCode() -> int: Returns the code.

