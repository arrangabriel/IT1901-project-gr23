# Core

Module for handling log entries. Provides access to the following classes:

- EntryManager
- LogEntry

## EntryManager

Handles a hashmap of log entries

### Methods

- addEntry(String, String, LocalDate, Duration) -> String
- getEntry(String) -> LogEntry
- removeEntry(String)
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