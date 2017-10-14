# Sonarqube

Starting with Mapbox Java 3.0 we use [SonarQube](https://www.sonarqube.org/) for tracking of static analysis issues, code coverage, project stat history, etc. This doc will walk through local setup and some of the features Sonarqube offers.

The project is public accessible through [SonarCloud](https://sonarcloud.io/dashboard?id=mapbox-java-services).

## Setup

### SonarCloud Account
To get admin access on SonarCloud, [sign in](https://sonarcloud.io/sessions/new) using your Github account and ping in the `#android` slack channel that you'd like access to the Mapbox organization.

**Important:** Once signed in, you'll want to generate a security token and add it to your `bash_profile` so you can locally run the `sonarqube` gradle task. Once logged in, goto `My Account → Security`, enter a token name, and generate. Note that running the gradle command locally will update the project on SonarCloud using your current branch status, thus, it's best to let CI run all updates and only running locally if you have a valid reason.

### SonarLint
Intellij has a plugin which helps quickly catch and resolve issues in real-time. To install, goto `Preferences → Plugins → Browse Repositories` and search SonarLint.

To follow the project rules in place, make sure to connect the plugin following the [official instructions](http://www.sonarlint.org/intellij/index.html#Connected). 


### Long-lived Non-branch analysis
In the case you want to maintain analysis of the canonical project in SonarQube, say the "master" branch, and also publish in SonarQube ongoing analysis of a secondary branch. The typical case is a release branch. In this scenario, you add the sonar.branch=[branch key] analysis property to the release branch to create a second, independent project in SonarQube.

### Github PR Analysis
TODO Needs to be setup once 3.0 is merged into master

## Features

### Issues
The key feature of SonarQube is the issue tracker. The Mapbox organization contains a set of rules or "ways" which help eliminate common Java errors. These run each time the `sonarqube` gradle task is run and if new issues are found, a new issue in the queue will be open with detailed information on how to resolve

In some cases, these issues can be a false positive and can be resolved by using the `Open` dropdown menu and marking the issue appropriately and giving a reason. 

### Method/Function tracking
For Android users, method count is critical information to know and keep track of. SonarQube allows you to track this information over time under the `measures` tab in the project. Click on `Size → Functions` and you'll see a list of the project files and the number of methods in each file. Above the list, you'll find a graph icon which can show progress and changes over time.

By default, this shows the entire projects method count which is misleading since each module is published separately. To fix this and display individual module stats, locate the hamburger menu icon above the list of files and switch from `List` to `Tree`. This will list all the modules within the project and the method count. 

**Note:** These number don't include dependency method count.