# Release 2

For release 2 we inteded to expand the MVP from the first release with new features and to improve the quality of the existing code. We have also expanded the test package with more tests and implemented testing of the ui. The project is still modular and consist of a full three-lair architecture. 

## New features

There was a handfull of new features planned for this release. These include the ability to sort the sessions saved on the start page list view. They can be sorted by date, duration or title. We completed the add new session page to handle more data. It is also possible to view older sessions from the start page and individual sessions can be deleted. As a whole this featureset was grouped under the GitLab milestone release 2, with single components divided into issues under the release 2 umbrella.

## Code improvements
The LogEntry class is changed to implement a builder pattern. This is because the old code was messy with a lot of inputs and long code lines. With this new builder pattern the logEntry object can be created with just the necessary inputs and the optional inputs can be left out completly when creating an object. This also changed the code in Localpresistency and in EntryManager. The core module was also expanded to handle more inputs. In general the code has been changed to achiev a higher level of quality.

We have also changed the way we switch scenes in the ui module. This is because we used a global variable "root", and this made testing difficult because we could not change the root variable.

## New project plugins
- Spotbugs
- Checkstyle
- Jacoco

## Test

We used Jacoco to decide wether the code was thoroghly tested. If the test coverage of a class was under 60% it did not satisfy the criteria for release 2. If it over 60% but under 80% it would get a warning. Read more about the jobs here: [workdetails](/gr2123/workdetails.md). We have used testFX to test ui module. For this release we have written more tests for the backend and adapted the old tests to fit the new builder pattern of LogEntry. 



## Planned implementations that was not ready for release 2
We did plan to be able to show and calculate statistics for this release. The statistics class was created to handle different type of calculations and the statistics fxml file was also created to display data. We did however find out that it was better to focus on improving test coverage and code quality rather than to implement more features. US3 and US4 were created and added to the project and some of the functionality displayed here are found in the code that we decided to delay for release 3. 







