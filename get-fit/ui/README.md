# UI

Module for handling frontend. Provides access to the following classes:

- StartPageController
- AddNewSessionController
- StatisticsController
- App

## StartPageController

Handles controll over the start page

### Methods

- addSessionButtonPushed(ActionEvent) -> void: Switches the view to AddNewSession.

- onStatisticsPage(ActionEvent) -> void: Switches the view to Statistics.

- closeView(ActionEvent) -> void: Hides entry view.

- sort(Event) -> void: Fill list with entries according to sort parameters. 

- updateList() -> void: Updates the log entry list by querying the server using selected params from the dropdown menues.

- replaceSubcategories(Event) -> void: Updates ui when main category is selected and updates the current sort.



## AddNewSessionController

Handles controll over log creation page

### Methods

- createSessionButtonPushed(ActionEvent) -> void Adds an entry to the app EntryManager and switches the view to StartPage.

- backButtonPushed(ActionEvent) -> void: Switches the view back to startPage.

- handleTagsSelector(ActionEvent) -> void: Changes ui according to the selected exercise category.

## StatisticsController
Handles controll over statistics page.

### Methods

- onReturn(ActionEvent) -> void: Switches the view to start page.

- onHandleData -> void: Updates Handles data for the data coloumn and updates bar chart with data.

## App
Class for starting the application. It extends Application.

### Methods
- main(String[]) -> void: Starts the application. The parameter is the app arguments.

- start(Stage) -> void: Starts the application. The parameter is the main stage.









