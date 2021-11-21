[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2123/gr2123)

# Get Fit - group 2123 project 

Get Fit is a workout-tracking application, written in Java. It uses a webserver in the background so the workout logs can be accessed from multiple computers.

This is the project made by group 2123 in IT1901. The project is git podified and it will open in gitpod when you press the button above.

## Running, building and installing the project

### Building

With Java 16 installed `mvn install` in the **get-fit** direcotry can be used to prepare the project. Running this command will first compile the project, test it, and ensure that a sizable portion of the code is tested. The tests take quite some time, and the UI tests will also take controll of you mouse. You can use the -DnoTestUIOtrue flag to disable UI tests, or -DskipTests to skipp all tests.

**Note** that this won't actually install the application on your system. For that see [Installing](#Installing) 

### Running

Running the application consists of two steps

- Start the server: To start the server either navigate to the **get-fit/restserver** directory and run `mvn spring-boot:run`, or run `mvn -pl restserver spring-boot:run` from the **get-fit** directory. This will start up the underlying server and make it ready ta take requests from the appp.
- Start the app: Starting the app is much like starting the server. Either navigate to **get-fit/ui** and run `mvn javafx:run` or from **get-fit** run `mvn -pl ui javafx:run`. This will boot the actual app. If you started the server you should see the application in front of you!

**Note** that running both server and application requires them to be built first. So before to do the [build](#Building) step first.

### Installing

Installing the application involves creating a binary executable, and then depending on your system adding that binary to your applications folder.

Navigate to the **getfit-ui** directory and running `mvn compile javafx:jlink jpackage:jpackage`. After the command has completed the executable to the application may be found in the **get-fit/ui/target/dist** directory. The executable can the be moved about as you wish! Either add it to you applications folder, or keep it as an executable elsewhere. Exactly how to do the former depends from system to system, but is not specific to the project.

**Note** that even though the app may be run from anywhere, even without Java installed, the server still needs to run for it to work. Attempting to run the application without an active server will result in a popup informing you that the server could not be reached.


## Structure and Workflow

The project architecture is documented in **[project architecture](/design-documentation/project-architecture/structure.md)**. And how the team works on the project is detailed in the **[workflow](workflow.md)** file. The **design-documentation** directory contains many resources and explanations as to why things are done the way the are, and can be a worthwhile read if the project interests you. Lastly, the **docs** directory contains information regarding each release of the project and further expands on the what and the why of the project.