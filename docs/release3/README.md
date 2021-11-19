# Release 3
For release 3 we have created a REST-API with springboot and implemented the planned implementation for release 2 to expand the functionality of the application. The ui has changed to be compatible with the REST-API. The project has also been thoroughly tested to cover all the new expansions and refinements.

## REST-API

## Statistics page extension
The statistics page was an extension we planned on implementing for release 2, but we decided to delay it for this release. The extension lets the user access a statistics page from the front page. Here the user can filter the sessions by date and on the left side of the page there is a section where the user can get more spesific data for the different types of exercise categories. This data includes count, total duration, average duration, average feeling, maximum heart rate and average speed (unless strength is chosen). When the user press enter a bar chart containing the number of sessions and the data page for the given category is displayed, both filtered for the time interval. We have added Statistics controller in the ui module and an accompanying fxml file. The core module was also extended with a second package called math, which contains the static class statictics that handles all of the calculations. 

## Code improvements

## Test

## Project plugins


## Expanded Project Architecture
Link to the planUML diagrams.

