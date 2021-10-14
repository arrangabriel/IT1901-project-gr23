# Branches and merging

Through some trial and error we have arrived at the following workflow for branches and merging:

- Create a feature branch
- Develop feature on feature branch
- Write tests and ensure minimum code coverage
- Merge into dev through a merge request where tests and code coverage analysis is automatically performed
- Once enough features have accumulated and are of good quality and polish merge dev into master through another merge request with aditional testing

# Jobs

The repository employs a multitude of jobs in order to ensure only valid code is merged into the branches dev and main. The jobs run an assortment of mvn goals and will give feedback if something goes wrong. The following jobs are run:

- `test` runs the unit tests of the project with mvn test. If any fail the merge request is blocked.
- `test-coverage-minimum` runs jacoco to ensure minimum (60%) code coverage. If it fails (under 60%) the merge request fails.
- `test-coverage-80` runs jacoco to test for more (80%) code coverage. Failure will raise a warning, but not block the merge request.
- `checkstyle` runs checkstyle to ensure propper code formatting. Failure will raise a warning, but not block the merge request.
- `spotbugs` runs spotbugs to catch possibly bug-inducing mistakes. Failure will raise a warning, but not block the merge request.