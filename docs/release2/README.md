# Release 2

For release 2 we inteded to expand the MVP from the first release with new features and to improve the quality of the existing code. We have also expanded the test package with more tests and implemented testing of the ui. The project is still modular and consist of a full three-lair architecture. 

## New features

For this release we implemented a lot of new features. This includes the ability to sort the sessions(enteries) saved on the start page list view. They can be sorted by date, duration or title. We also added the ability to complete the add new session page to handle more data. It is also possible to view older sessions (enteries) from the start page and individual sessions(entries) can be deleted. 

## Code improvements
The LogEntry class is changed to implement a builder pattern. This is because the old code was messy with a lot of inputs and long code lines. With this new builder pattern the logEntry object can be created with just the necessary inputs and the optional can be left out completly when creating an object. This also changed the code structure in the Localpresistency class and in EntryManager. The core module was also expanded to handle more inputs. In general the code has been changed to achiev a higher level of quality.

- Changed switching scenes method.

## New project plugins
- Spotbugs
- Checkstyle
- Jacoco

## Test




## Planned implementations that was not ready for release 2
We did plan to be able to show and calculate statistics for this release. The statistics class was created to handle different type of calculations and the statistics fxml file was also created to display data. We did however find out that it was better to focus on improving test coverage and code quality rather than to implement more features. US3 and US4 were created and added to the project and some of the functionality displayed here are found in the code that we decided to delay for release 3. 







