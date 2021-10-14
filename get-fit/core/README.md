# Core

Module for handling log entries. Provides access to the following classes:

- EntryManager
- LogEntry

## EntryManager

Handles a hashMap of log entries. Every log entry is stored in the hashmap with a string as the key.  

### Methods

- addEntry(LogEntry) -> String : Adds a new LogEntry to the hashmap. The parameter is the LogEntry that is added.
It generates a string based on the size on the hashmap. This will give the added LogEntry the number of n-1 elements as the key in the hashmap as a String.
The function returns the String. It throws an IllegalArgumentException if the input is invalid.

- addEntry(String, LogEntry) : Adds a new log entry to the hashmap. If the hashMap contains the same String as the
String parameter it throws an IllegalArgumentException.
 
- getEntry(String) -> LogEntry : It returns the object with the same key as the String parameter. 
If the String is null or if the key does not exist it throws an IllegalArgumentException. 

- removeEntry(String) -> Boolean : Removes the LogEntry with the same key as the parameter String. It returns true if the LogEntry was removed or false otherwise. It throws an IllegalArgumentException if the String is null. 

- entryCount() -> int : Return the number of LogEntries in the hashMap.

- entryIds() -> Set<String> : Return the Set<String> of all the keys in the hashMap.

- swapEntry() : Removes 
- length() -> int 
- iterator() -> Iterator<String>
- getEntryList() -> Set<Entry<String, LogEntry>>


## LogEntry

Represents a log entry

### Methods

- getTitle() -> String
- getComment() -> String
- getDate() -> LocalDate
- getDuration() -> Duration
- getID() -> String
- setTitle(String) -> Void
- setComment(String) -> Void
- setDate(LocalDate) -> Void
- setDuration(LocalDate) -> Void
