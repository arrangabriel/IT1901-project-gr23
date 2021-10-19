# UI

Module for handling frontend. Provides access to the following classes:

- StartPageController
- AddNewSessionController

## StartPageController

Handles controll over the start page

### Methods

- addSessionButtonPushed(ActionEvent) -> void: Switches the view to AddNewSession.

- closeView(ActionEvent) -> void: Hides entry view.

- sort(Event) -> void: Fill list with entries according to sort parameters. 

- addIteratorToView(Iterator) -> void: Place an iterator of enteries in the view.

- replaceSubcategories(Event) -> void: Updates ui when main category is selected and updates the current sort.

- reverse(ActionEvent) -> void: Updates ui sort with reversal




## AddNewSessionController

Handles controll over log creation page

### Methods

- createSessionButtonPushed(ActionEvent) -> void Adds an entry to the app EntryManager and switches the view to StartPage.

- backButtonPushed(ActionEvent) -> void: Switches the view back to startPage.

- handleTagsSelector(ActionEvent) -> void: Changes ui according to the selected exercise category.






