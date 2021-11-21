# Release 3

For release 3 we have created a REST-API with springboot and implemented the planned implementation for release 2 to
expand the functionality of the application. The ui has changed to be compatible with the REST-API. The project has also
been thoroughly tested to cover all the new expansions and refinements.

## REST-API

For this release we implemented a REST-API with spring-boot. The server is set up using spring in the new restserver
module. This module is dependent on core and handles requests from a new client module. This makes ui and core
independet, and the data between them are handled by the client module (Data access layer) and the restserver (Service
layer).

We have chosen to use Spring because Spring is widely used by other java developers and therefore it is well documented.
Spring Boot made it easier to set up the Spring server.

## Statistics page extension

The statistics page was an extension we planned on implementing for release 2, but we decided to delay it for this
release. The extension lets the user access a statistics page from the front page. Here the user can filter the sessions
by date and on the left side of the page there is a section where the user can get more spesific data for the different
types of exercise categories. This data includes count, total duration, average duration, average feeling, maximum heart
rate and average speed (unless strength is chosen). When the user press enter a bar chart containing the number of
sessions and the data page for the given category is displayed, both filtered for the time interval. We have added
Statistics controller in the ui module and an accompanying fxml file. The core module was also extended with a second
package called math, which contains the static class statictics that handles all of the calculations.

We have chosen to extend the application with statistics, because it was a natural expansion to the application. This
extension gives the application a purpose, when actually displaying useful calculated data, instead of just displaying
user input. This statistics page could be extended fourther with different charts with data and other sorting methods.
We decided to keep it reasonably simple to make sure we would have enough time for the other implementations and
improvements that needed to be done for release 3. This includes setting up the API and thoroughly testing the
application.

## Code improvements

A large focus of release 3 has been to improve code quality and readbility across the board. To implement this in a
effective and systematic way we employed the use of [**checkstyle**](https://checkstyle.sourceforge.io/)
and [**spotbugs**](https://spotbugs.github.io/). These are both code analysis tools. The former being used to format in
compliance with a given style, and the latter as a way to avoid common Java voulnerabilities.

There has also been an effort amongst the team to employ more modern Java features and syntax, such as streams, lambdas
and switch assignments. Doing this has helped make many parts of the codebase more succinct and clear when reading. As
well as make coding patterns more uniform throughout.

In accordance with the previous point, as preparation for implementation of the server-client architecture the team
collectively decided on an API scheme. Included in the discussions was how the server would translate its information to
HTTP requests. Closely mirroring the implementation when interpreting requests again made the code base more uniform,
allowing for easier 'full-stack' work.

## Test

As in previous releases we use a tool called Jacoco, which determines how much of the projects source code is covered by testing. As well as Spotbugs and Checkstyle for code quality beyond testing that it works.

In addition to expand our tests to accomodate the extra features in this release we have also introduced integration testing. This comes now as the server architecture ruins some of the automatic verification that the Java compiler does. As such it became necessary to test wether the client and the server "spoke the same language" so to speak. Even if the server and client were consistent internally, and with the other Java classes they interacted directly with, there was no guarantee that they would cooperate with each other.

We chose to place the integration tests in a separate module so that it could depend on both the client and the server, to test them, without having the server or client depending on the other. This is important mostly when considering what gets packaged when bundling the application.

## Project plugins

- Checkstyle:  
  We are using version 3.1.2 of the maven-checkstyle plugin, running **sun-checks** on checkstyle version 9.1
- Spotbugs:
  We are using version 4.4.1 of the maven-spotbugs plugin, running with spotbugs version 4.4.2
- JaCoCo:
  We are using version 0.8.7 of the maven-jacoco plugin.

## Expanded Project Architecture

Project architecture for release 3 is documented in
[**project-architecture**](/design-documentation/project-architecture).
