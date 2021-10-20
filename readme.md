[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2123/gr2123)

# Get Fit - group 2123 project 

This is the coding project to group 2123 in the course IT1901. The project is git modified and it will open in gitpod when you push the lable above this README-file 

## Running, building and testing the project 

The project is built and run using maven. Use `mvn install` from  the **get-fit**-folder to build the project. The project must be built before you can run the tests.

To run the project you must be in the **ui**-module. This can be done by first using the command `cd ui` and then `mvn javafx:run`. Another option is to just use the command `mvn -pl ui javafx:run`

To test the project you must be in the get-fit directory and use the command `mvn test`. You can also use the command `mvn verify` to both test the project and run Jacoco. To run Checkstyle use the command `mvn checkstyle:check` and use the command `mvn spotbugs:check` to run Spotbugs.

## Structure 

This project contains three modules, core, ui and localpersistence. Each module includes these source code directories: 

**scr/main/java** contains code for application 
**test/java/core** contains test code for application 

The ui module also include a resource directory:
 **scr/main/resources** contains FXML files 

### Core 

**[core-module](/get-fit/core)** contains one package, **[core](/core/scr/main/java/core)**.  This package includes all the classes that handle logic 

### Local Persistence 
**[localpersistence-module](/get-fit/localpersistence)** contains one package, **[localpersistence](/localpersistence/scr/main/java/localpersistence)**.  This package includes classes implemented with JSON. These classes write and load objects to and from file.  

### UI
**[ui-module](/get-fit/ui)** contains one package, **[ui](/ui/scr/main/java/ui)**. This package includes controllers that handle interactions between user and interface 

The user interface is created with JavaFx an FXML. The FXML directory is located in **[ui-resources](/ui/scr/main/java/resources/ui)** All FXML files are connected to a controller. 

## PlantUML diagram
Under follows a rendered PlantUML diagram that represents the architecture of the Get Fit application. It shows how the components and packages relate to each other. The PlantUML code can be found her: [Get Fit PlantUML architecture file](/get-fit/architecture.puml)

![Design documentation](/get-fit/Get_Fit_Architecture.png)








