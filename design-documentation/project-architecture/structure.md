## Structure

This project contains three modules, core, ui and localpersistence. Each module includes these source code directories:

**src/main/java** contains code for application
**test/java/core** contains test code for application

The ui module also include a resource directory:
**src/main/resources** contains FXML files

### Core

**[core-module](/get-fit/core)** contains one package, **[core](/get-fit/core/src/main/java/core)**.  This package includes all the classes that handle logic

### Local Persistence
**[localpersistence-module](/get-fit/localpersistence)** contains one package, **[localpersistence](/get-fit/localpersistence/src/main/java/localpersistence)**.  This package includes classes implemented with JSON. These classes write and load objects to and from file.
**[Notes on implementation-decisions for saving (impicit/explicit).](/design-documentation/project-architecture/persitence-metaphor.md)**

### UI
**[ui-module](/get-fit/ui)** contains one package, **[ui](/get-fit/ui/src/main/java/ui)**. This package includes controllers that handle interactions between user and interface

The user interface is created with JavaFx an FXML. The FXML directory is located in **[ui-resources](/get-fit/ui/src/main/resources/ui)** All FXML files are connected to a controller.

## PlantUML diagram
This PlantUML diagram that represents the architecture of the Get Fit application. It shows how the components and packages relate to each other. The PlantUML code can be found her: [Get Fit PlantUML architecture file](/get-fit/architecture.puml)

![Design documentation](/design-documentation/project-architecture/get_Fit_Architecture.png)