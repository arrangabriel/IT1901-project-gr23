# Branches and merging

Through some trial and error we have arrived at the following workflow for branches and merging:

- Create a feature branch
- Develop feature on feature branch
- Write tests and ensure minimum code coverage
- Merge into dev through a merge request where tests and code coverage analysis is automatically performed
- Once enough features have accumulated and are of good quality and polish merge dev into master through another merge request with aditional testing

Extensive use of branches redices the risk of merge conflicts during development, and keeps the project more structured. When multiple members are actively working on the same feature secondary branches are often created to leverage the aforementioned benefits.

Sometimes a feature branch is so broad that it is under active development all the way through the SCRUM cycle. This is especially true when the feature is very complicated, with multiple parts, like the server architecture. Where adding the server doesn't make sense without modifying the App, and where modifying the App doesn't make sense without creating the server. For situations like these, offshoot branches are prevalent.

Merge requests have proven exceptionally useful as it prompts the entire team to do a thorough check of the code, and discuss any issues that may arise. [Automatic Jobs](#Jobs) are run on any merge request going into the dev or master branches. This further prompts the team to discuss the feedback given by checkstyle or spotbugs.

# Workflow

We use the SCRUM framework for efficently producing and delivering high quality code. The issues related to the milestone are used to divide different tasks between us. We use the milestones for grouping issues with releases, as we have been instructed to. The issues are labeled to easily sort and distinguish them from eachother. Pair programing is something we practice a lot, even though we divide the issues to individual members of the group. The group is divided into pairs, and those pairs tackle different challenges and take turn programming. By doing this all of the group members can learn from each other. It also makes us less vulnerable to typos and other errors, and it is easier to solve complex problems in pairs. Even though we use pair programming a lot, we do some of the easier work indiviually and make the other team members review the work. This is to make the development process more efficent.

The group has as a goal to commit more, rather than less. This makes revertion, cherrypicking, and finding breaking changes easier. Smaller commits also enable the use of more specific commit messages, which serve as a comment or explanation on what was done, and why it was done. Everyone in the group also has tools to view these comments inline while coding, which facilitates teamwork even when two members aren't working directly together. This practice also makes it easy to track down who worked on a specific part of the code, letting a different member ask them for help if they are going to make changes in that area of the codebase.

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

An attempt has been made in making the jobs more efficient, by reusing compilation and test results in the later jobs, but gitlab does not provide great support for this, meaning that a job using the verify goal will have to re-compile and re-test the entire project even though it was allready done by a different job.

Aditionally we have a job for compiling the app to an executable, though because it happens on a Linux machine windows, macOs, and even users of differing Linux distributions will have to compile from source, or have someone else do it and distribute that binary.