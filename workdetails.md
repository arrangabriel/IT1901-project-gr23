# Branches and merging

Through some trial and error we have arrived at the following workflow for branches and merging:

- Create a feature branch
- Develop feature on feature branch
- Write tests and ensure minimum code coverage
- Merge into dev through a merge request where tests and code coverage analysis is automatically performed
- Once enough features have accumulated and are of good quality and polish merge dev into master through another merge request with aditional testing

# Workflow

We use the SCRUM framework for efficently producing and delivering high quality code. The issues related to the milestone are used to divide different tasks between us. We use the milestones for grouping issues with releases, as we have been instructed to. The issues are labeled to easily sort and distinguish them from eachother. Pair programing is something we practice a lot, even though we divide the issues to individual members of the group. The group is divided into pairs, and those pairs tackle different challenges and take turn programming. By doing this all of the group members can learn from each other. It also makes us less vulnerable to typos and other errors, and it is easier to solve complex problems in pairs. Even though we use pair programming a lot, we do some of the easier work indiviually and make the other team members review the work. This is to make the development process more efficent. 

# Jobs

The repository employs a multitude of jobs in order to ensure only valid code is merged into the branches dev and main. The jobs run an assortment of maven goals and will give feedback if something goes wrong. The following jobs are run:

- `build`
    - `validate` Runs maven validate goal. Ensuring that the pom and other required components are present and valid.
    - `compile` Runs maven compile goal. This not only ensures that the project compiles, and lets us skipp this step in following jobs.
- `test`
    - `test` Runs the unit tests of the project with mvn test. If any fail the merge request is blocked.
- `test-coverage`
    - `test-coverage-minimum` Runs jacoco to ensure minimum (60%) code coverage. If it fails (under 60%) the merge request fails.
    - `test-coverage-80` Runs jacoco to test for more (80%) code coverage. Failure will raise a warning, but not block the merge request.
- `quality`
    - `checkstyle` runs checkstyle to ensure propper code formatting. Failure will raise a warning, but not block the merge request.
- `bugs`
    - `spotbugs` runs spotbugs to catch possibly bug-inducing mistakes. Failure will raise a warning, but not block the merge request.

These jobs ensure that breaking code does not get merged into the dev or master branches. So far it has been a great success, pointing out errors in our code. However checkstyle and spotbugs can at times be overly sensitive. The main problem here is that if decide to ignore some of the output from checkstyle or spotbugs they will always fail. This in turn desensitizes us to seeing them fail. We have attempted to provide custom rules, but without luck. Still we go looking into the output of the jobs to evaluate wether some of the errors are something we care about. 

Aditionally we have a job for compiling the app to an executable, though because it happens on a Linux machine windows, macOs, and even users of differing Linux distributions will have to compile from source, or have someone else do it and distribute that binary.