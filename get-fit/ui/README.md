# UI

Module for handling frontend. Provides access to the following classes:

- StartPageController
- AddNewSessionController

## StartPageController

Handles controll over the start page

### Methods

- addSessionButtonPushed(ActionEvent) -> void: Switches the view to AddNewSession.

- addToList() -> void: Iterates over the EntryManager of the app and adds the titles to listOfEntries.



## AddNewSessionController

Handles controll over log creation page

### Methods

- createSessionButtonPushed(ActionEvent) -> void Adds an entry to the app EntryManager and switches the view to StartPage.

- returnButtonPushed(ActionEvent) -> void: Switches the view back to startPage

- handleTagsSelector(ActionEvent) -> void: Changes ui according to the selected exercise category.



