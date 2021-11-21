## Project architecture

This project contains six modules, core, ui, localpersistence, client, restserver and integration. Each module includes these source code directories:

**src/main/java** contains code for application
**test/java/module name** contains test code for application

The ui module also include a resource directory:
**src/main/resources** contains FXML files

### Core

**[core-module](/get-fit/core)** contains two package math and core, **[core](/get-fit/core/src/main/java/core)** and **[math](/get-fit/core/src/main/java/math)**. This package includes all the classes that handle logic

### Local Persistence
**[localpersistence-module](/get-fit/localpersistence)** contains one package, **[localpersistence](/get-fit/localpersistence/src/main/java/localpersistence)**.  This package includes classes implemented with JSON. These classes write and load objects to and from file.
**[Notes on implementation-decisions for saving (impicit/explicit).](/design-documentation/project-architecture/persitence-metaphor.md)**

### UI
**[ui-module](/get-fit/ui)** contains one package, **[ui](/get-fit/ui/src/main/java/ui)**. This package includes controllers that handle interactions between user and interface

The user interface is created with JavaFx an FXML. The FXML directory is located in **[ui-resources](/get-fit/ui/src/main/resources/ui)** All FXML files are connected to a controller.

### Client
**[client-module](/get-fit/client)** contains one package, **[client](/get-fit/client/ui/src/main/java/client)**. This package constitutes the presentation layer and transfer date to and from the client.   

### Restserver
**[restserver-module](/get-fit/restserver/)** contains one packages, **[restserver](/get-fit/restserver/src/main/java/restserver)**. This packages make up the service layer and transfer data to and from the server. 

### Integration 
**[integration-module](/get-fit/integration)** . 

## PlantUML diagram
This PlantUML diagram that represents the architecture of the Get Fit application. It shows how the components and packages relate to each other. The PlantUML code can be found her: [Get Fit PlantUML architecture file](/get-fit/project-architecture/PUML-diagrams/packageDiagram.puml)

![Design documentation](/design-documentation/project-architecture/PUML-diagrams/packageDiagram.png)
