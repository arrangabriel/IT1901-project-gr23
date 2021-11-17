#Rest Server

This modules handles the Service layer (rest api). Its purpose is to transfer data to and from the service layer. The module provide access to the following classes:

- GetfitApplication
- GetfitController
- GetfitService

##GetfitApplication
Spring boot application class. It starts the server.

###Methods
- main(String...): Main method for starting the application. It access the method run in SpringApplication with GetfitApplication.class and the given strings as arguments.

##GetfitController
Controller class for handling the get and post requests. It constists of a GetfitService.

##Methods

@GetMapping(value="/{entryId}", produces = "application/json")
- getLogEntry(@PathVariable("entryId") String id)
