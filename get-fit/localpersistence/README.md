# Localpersistence

Module for handling local persistence. Provides access to the following classes:

- EntrySaverJson

## EntrySaverJson

Handles saving and loading to and from a json file

### Methods

- save(EntryManager)-> void: Iterates over every entry in the provided EntryManager and adds their data as a string to a
  hashmap. It saves the hashmap to the file SavedData.json.

- save(EntryManager, String) -> void: Iterates over every entry in the provided EntryManager and adds their data as a
  string to a hashmap.

- load(EntryManager) -> void: Loads SavedData.json and constructs LogEntries which it then appends to the provided
  EntryManager.

- load(EntryManager, String) -> void: Loads a specified JSON file and constructs LogEntries which it appends to the
  provided EntryManager.

# Structure

The EntryManager is saved as a json file structured as a hashmap with EntryLog-ids as keys, and information about the
EntryLog as the content. Said content is put into a hashmap with the following fields:

- date
- duration
- distance
- exerciseSubCategory
- comment
- feeling
- title
- exerciseCategory
- maxHeartRate
