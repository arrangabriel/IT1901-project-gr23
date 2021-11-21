## Project architecture

This project contains six modules: core, ui, localpersistence, client, restserver and integration. Each module includes these source code directories:

**src/main/java** contains code for application
**src/test/java** contains test code for application

The ui module also include a resource directory:
**src/main/resources** contains FXML files

### Core

**[core-module](/get-fit/core)** contains two packages **[core](/get-fit/core/src/main/java/core)** and **[math](/get-fit/core/src/main/java/math)**. This package includes all the classes that handle logic adn computation.

### Local Persistence
**[localpersistence-module](/get-fit/localpersistence)** contains one package, **[localpersistence](/get-fit/localpersistence/src/main/java/localpersistence)**.  This package includes classes that handle serialization and deserialization of classes in the [core-module](#Core). It also handles reading and writing to and from a file.
**[Notes on implementation-decisions for saving (impicit/explicit).](/design-documentation/project-architecture/persitence-metaphor.md)**

### UI
**[ui-module](/get-fit/ui)** contains one package, **[ui](/get-fit/ui/src/main/java/ui)**. This package includes controllers that handle interactions between the user and the underlying backend.

The user interface is created with JavaFx an FXML. The FXML directory is located in **[ui-resources](/get-fit/ui/src/main/resources/ui)** All FXML files are connected to a controller.

### Client
**[client-module](/get-fit/client)** contains one package, **[client](/get-fit/client/ui/src/main/java/client)**. This package makues up the transportation layer and transfers data between the server and the UI.   

### Restserver
**[restserver-module](/get-fit/restserver/)** contains one packages, **[restserver](/get-fit/restserver/src/main/java/restserver)**. This packages make up the service layer and handles requests from the UI. It is responsible for the logic and computation implemented in the [core-module](#Core) and the persistance implemented in the [localpersistance-module](#Local-Persistance).

### Integration 
**[integration-module](/get-fit/integration)** only exists to house the integration tests. Thus enabling us to test everything together without the front-end and back-end tepending directly on each other.

## PlantUML diagram
This PlantUML diagram that illustrates the architecture of the Get Fit application. It shows how the components and packages relate to each other. The PlantUML code can be found her: [Get Fit PlantUML architecture file](/get-fit/packageDiagram.puml)

![Design documentation](/design-documentation/project-architecture/PUML-diagrams/packageDiagram.png)
