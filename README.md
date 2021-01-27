# The Mojo Code Generator

This utility enables generating boilerplate code with simple
command line arguments. The utility currently supports:

* RESTful web services based on Spring
   
## FOR USER'S OF THE CODE GENERATOR

### Usage:

First, it is important to know the project assets will be created in your current
directory, so be sure to navigate to the folder in which 
you want the project assets created before running any command.

POSIX-style syntax is supported, allowing options to be specified as 
```-option value``` or as ```-option=value```.

The command             

    mojo rest-api create-project --help 

shows the different options for creating a project. The simplest way to
create a project is

    mojo rest-api create-project -p org.example.widgets

which creates the project assets in the current directory,
with the default Java package name of ```org.example.widgets```.

A base project will not have any controllers, only a Spring
application that boots up on port 8080.  To add controllers
to the project, use the ```create endpoint``` sub-command.
For example,

    mojo rest-api create-endpoint -resource Widget -route /widget

This will create a Spring controller that will accept the 
route ```localhost:8080/widget``` and return a sample Widget.

## FOR MAINTAINERS (DEVELOPERS)
                    
### To Compile the Application

To build the application, run ```./gradlew build```. 

To build, test, report test coverage, and export the test coverage to Sonarqube, run

```./gradlew clean build codeCoverageReport sonarqube```

### To Deploy the Application

Navigate to the ```mojo``` sub-project, then to the ```build/distributions``` folder.
(that is, ```cd mojo/build/distributions```). (DO NOT use the tar/zip file
created in the project root build/distributions folder; that is not the uber jar.)

Copy the ```mojo-shadow.tar``` (or ```zip```) to the desired location
and unpack it, then add the ```mojo/bin/mojo``` executable to your PATH. 
                      
### To Extend the Application

The general approach to extending Mojo by adding more
sub-commands is to create a module folder for the new
sub-command and add the appropriate code there. The
```mojo``` module can then be updated to link to the 
new sub-module. 

For example, to add a ```reactive-api``` sub-command, do
these steps:

1. create a ```reactive-api``` folder and the usual 
   assets (src/main/java; src/main/resources; etc)

2. Update the ```mojo/build.gradle``` to have a project 
   dependency on the ```reactive-api``` module.
   
3. Update the MojoApplication.java class to add
```reactive-api``` as a sub-command. The Picocli 
   library requires sub-commands to be explicitly
   set at compile-time.
   
4. Rebuild Mojo, deploy the new version of the Mojo
   application and do the appropriate testing of
the new ```reactive-api``` sub-command to ensure
   the generated code works as you expect. 
   

### To Maintain the Application

#### Keeping library versions up-to-date

The ```build.gradle``` imports the gradle-versions-plugin (See https://github.com/ben-manes/gradle-versions-plugin)
which adds tasks for library version management. 

##### Task: dependencyUpdates

This displays a report of the project dependencies that are up-to-date, exceed the latest
version found, have upgrades, or failed to resolve. 

To refresh the cache, use the flag ```--refresh-dependencies```.

Examples:

```./gradlew dependencyUpdates -Drevision=release -DoutputFormatter=json -DreportfileName=version-report```

The report is written to the ```[projectRoot]/build/dependencyUpdates``` directory. 

### Code Coverage and SonarQube

A ```codeCoverageReport``` task is available in the build.gradle. To get code coverage, run the command:

```./gradlew test integrationTest codeCoverageReport sonarqube```

#### Setting up SonarQube

Step 1:  Run this command:

```docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest```

Step 2: Login to the SonarQube console at localhost:9000. You'll be prompted to change
the default password (admin/admin). Pick something and update this repo's gradle.properties
file with the new password. See [Getting Started](#### Setting up SonarQube) to get up to
speed with SonarQube.