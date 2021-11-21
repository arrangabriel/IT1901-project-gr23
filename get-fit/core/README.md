# Core

Module for handling log entries. The core module contains two packages, core and math. Core consists of the business logic and math consists of calculation logic. 

**Core**
Provides access to the following classes:
- EntryManager
- LogEntry
- Validity

Provides access to the following interface:
- Subcategory

Provides access to the following enums:
- CardioSubCategory
- ExerciseCategory
- SortConfigurations
- StrengthSubCategory

**Math**
Provides access to the following classes:
- Statistics


## EntryManager

Handles a hashMap of log entries. Every log entry is stored in the hashmap with a string as the key.  

### Methods

- EntryManager(): An entry manager instance is a wrapper for a list of logEntries. Functions as the API interface for the core-module.

- fromHash(HashMap<String, HashMap<String, String>, EntryManager>) -> void: Updates a provided EntryManager with the LogEntries represented in the HashMap.

- addEntry(LogEntry) -> String: Adds a new LogEntry to the hashmap. The parameter is the LogEntry that is added. It generates a string based on the size on the hashmap. This will give the added LogEntry the number of n-1 elements as the key in the hashmap as a String. The function returns the String. It throws an IllegalArgumentException if the input is invalid.

- addEntry(String, LogEntry) -> void: Adds a new log entry to the hashmap. If the hashMap contains the same String as the String parameter it throws an IllegalArgumentException.

- updateHashPosition(int) -> void: Updates the position for id hashing.
 
- getEntry(String) -> LogEntry: Returns the object with the same key as the String parameter. 
If the String is null or if the key does not exist it throws an IllegalArgumentException. 

- removeEntry(String) -> Boolean: Removes the LogEntry with the same key as the parameter String. It returns true if the LogEntry was removed or false otherwise. It throws an IllegalArgumentException if the String is null. 

- entryCount() -> int: Returns the number of LogEntries in the hashMap.

- toHashMap() -> HashMap<String, HashMap<String, String>>: Represents this EntryManager as a HashMap with LogEntries, also represented as HashMaps.

- Iterator() -> Iterator<LogEntry> : Returns an iterator for the LogEntries in an arbitrary order. 

**class SortedIteratorBuilder**

Is a builder for LogEntry iterator. It has an internally modifiable stream of LogEntries.

- SortedIteratorBuilder(EntryManager, LogEntry.SORTCONFIGURATIONS): The SortedIteratorBuilder constructor. It sorts the LogEntries in the given EntryManager based on the given LogEntry.SORTCONFIGURATIONS, which can be by date, duration or title. It throws an IllegalArgumentException if the sortconfigurations is null. 

- filterExerciseCategory(LogEntry.EXERCISECATEGORY) -> SortedIteratorBuilder: It filters the entries in the Stream based on the given exercisecategory. It returns itself. It throws an IllegalArgumentException if the LogEntry.EXERCISECATEGORY is null.

- filterSubCategory(LogEntry.SubCategory) -> SortedIteratorBuilder: It filters the entries in the Stream based on the given exercisecategory. It returns itself. It throws an IllegalArgumentException if the LogEntry.SubCategory is null.

- filterTimeInterval(LocalDate, LocalDate) -> SortedIteratorBuilder: It filters the entries from a given time interval. It returns itself. It throws an IllegalArgumentException if any of the dates are null. 

-  iterator(boolean) -> Iterator<LogEntry>: It returns the iterator that make it possible to iterate over the given values in the Stream stored as a List<LogEntry>. If the boolean is True the list the iterator will iterate over the reversed List<LogEntry>.

## LogEntry

 A logEntry instance represents a single workout-entry internally. Has fields for the elements of a workout-entry, getters for them, and methods for de/serialization and validation. It is mostly immutable, save for the id, which can be set exactly once.

### Methods

- LogEntry(EntryBuilder): The constructor of LogEntry. Validates the EntryBuilder with the validate(EntryBuilder) method by creating a Validity object. If the validity object is valid it sets the internal variables. If it is not valid it throws an IllegalArgumentException.

- validate(EntryBuilder) -> Validity: Validate the EntryBuilder by checking the validity of the variables in the builder. It returns a Validity object.

- stringToSubcategory(String) -> Subcategory: Converts a String representation of a Subcategory into a subcategory. It returns the actual subcategory or null if no match.

- fromHash(HashMap<String, String>) -> LogEntry: Parses a HashMap to create a logEntry. It returns the parsed logEntry.

- getId() -> String: Returns the id field of this logEntry.

- setId(String) -> Sets the id for the LogEntry. Can only be done once. It throws an IllegalStateException if the id had already been set.

- toHash() -> HashMap<String, String>: Represents this LogEntry as a HashMap with values converted to Strings. It returns the HashMap representing this LogEntry.

- getTitle() -> String: Returns the title field og this logEntry.

- getComment() -> String: Returns the comment field(main text body) of this logEntry.

- getDate() -> LocalDate: Returns the date of this logEntry.

- getDuration() -> Duration: Returns the duration of this logEntry.

- getFeeling() -> int: Returns the feeling field.

- getDistance() -> Double: Returns the distance field.

- getMaxHeartRate() -> Integer: Returns the maxHeartRate field.

- getExerciseCategory() -> EXERCISECATEGORY: Returns the main exercise category of this logEntry.

- getExerciseSubCategory() -> Subcategory: Returns the subcategory of this logEntry. 

- getExerciseSubCategories() -> Subcategory[]: Returns the subcategories for the logEntry's main category as an array.

**class EntryBuilder**

The builder class for the LogEntry. Consist of the required fields of type String, LocalDate, Duration, EXERCISECATEGORY, int. It also consist of the optional fields String, Subcategory, Double, Integer.

- EntryBuilder(String, LocalDate, Duration, EXERCISECATEGORY, int): Sets the internal variables based on the inputs. Throws an IllegalArgumentException if any of the inputs are illegal. 

- comment(String) -> EntryBuilder: Sets the comment for the builder. Throws an IllegalArgumentException if the String is illegal.

- exerciseSubCategory(Subcategory) -> EntryBuilder: Sets the exercise subcategory for the builder. It returns itself after the subcategory is set. Throws new IllegalArgumentException if the subcategory is illegal.

- distance(Double) -> EntryBuilder: Sets the distance for the builder. It returns itself after the distance is set. Throws new IllegalArgumentException if the distance is illegal.

- maxHeartRate(Integer) -> EntryBuilder: Sets the maxHeartRate for the builder. It returns itself after the maxHeartRate is set. Throws a new IllegalArgumentException if the maxHeartRate is illegal.

- build() -> LogEntry: Constructs the LogEntry object. It creates a new LogEntry object from the builder and returns it. It throws an IllegalArgumentException if any of the arguments are invalid.

## Validity
Consist of a boolean and String field.

### Methods
- Validity(boolean, String) -> void: Representing the validity of an EntryBuilder.

- valid() -> boolean: Returns wether the builder is valid.

- reason() -> String: Returns the reason for the validity of the builder.

## interface SubCategory
Defines a subcategory.
### Methods
- getValueOf(String) -> Subcategory: Used to get the subcategory value of a String.

## enum SORTCONFIGURATIONS
Configuration by which to sort LogEntries. DATE, DURATION, TITLE.

## enum EXERCISECATEGORY
The categories that an exercise can fall under: ANY, STRENGTH, RUNNING, CYCLING, SWIMMING.

### Methods
- getSubcategories() -> Subcategory[]. Returns a copy of the Subcategories as an array.

## enum STRENGTHSUBCATEGORIES
The Subcategories for the Strength category: PUSH, PULL, LEGS, FULL BODY. It implements Subcategory.
 
### Methods
- getValueOf(String) -> Subcategory: Return the Subcategory that match the given String.

## enum CARDIOSUBCATEGORIES
The Subcategories for the Cardio categories(RUNNING, CYCLING, SWIMMING). It implements Subcategory. 

### Methods 
- getValueOf(String) -> Subcategory: Return the Subcategory that match with the given String.

## Statistics
A static class that consists of calculation methods.

- getCount(EntryManager, String, String) -> int: Returns the count of entries in the EntryManager filtered by category and date.

- getTotalDuration(EntryManager, String, String) -> double: Returns the total duration of all LogEntries in the EntryManager filtered by category and date.

- getAverageDuration(EntryManager, String, String) -> double: Returns the average duration across all LogEntries in the EntryManager filtered by category and date.

- getAverageSpeed(EntryManager, String, String) -> double: Returns the average speed across all LogEntries in the EntryManager filtered by category and date.

- getAverageFeeling(EntryManager, String, String) -> double: Returns the average feeling of all LogEntries in the EntryManager filtered by category and date.

- getMaximumHr(EntryManager, String, String) -> double: Returns the maximum heart rate across all LogEntries in the EntryManager filtered by category and date.




