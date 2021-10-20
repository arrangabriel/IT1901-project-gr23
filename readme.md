[![Gitpod Ready-to-Code](https://img.shields.io/badge/Gitpod-Ready--to--Code-blue?logo=gitpod)](https://gitpod.stud.ntnu.no/#https://gitlab.stud.idi.ntnu.no/it1901/groups-2021/gr2123/gr2123)

# Get Fit - group 2123 project 

This is the coding project to group 2123 in the course IT1901. The project is git modified and it will open in gitpod when you push the lable above this README-file 

## Running, building and testing the project 

The project is built and run using maven. Use `mvn install` from  the **get-fit**-folder to build the project. The project must be built before you can run the tests.

To run the project you must be in the **ui**-module. This can be done by first using the command `cd ui` and then `mvn javafx:run`. Another option is to just use the command `mvn -pl ui javafx:run`

To test the project you must be in the get-fit directory and use the command `mvn test`. You can also use the command `mvn verify` to both test the project and run Jacoco. To run Checkstyle use the command `mvn checkstyle:check` and use the command `mvn spotbugs:check` to run Spotbugs.

## Structure 

The project architecture is documented in **[project architecture.](/DesignDocumentation/Project-architecture/structure.md)**