# Localpersistence

Module for handling local persistence. Provides access to the following classes:

- EntrySaverJson

## EntrySaverJson

Handles saving and loading to and from a json file

### Methods

- save(EntryManager)-> void
- save(EntryManager, String) -> void
- load(EntryManager) -> void
- load(EntryManager, String) -> void

# Structure

The EntryManager is saved as a json file structured as a hashmap with EntryLog-ids as keys, and information about the EntryLog as the content. Said content is put into a hashmap with the following fields:

- date
- duration
- distance
- exerciseSubCategory
- comment
- feeling
- title
- exerciseCategory
- maxHeartRate