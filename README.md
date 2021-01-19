# The Mojo Code Generator

This utility enables generating boilerplate code with simple
command line arguments. The utility currently supports:

* RESTful web services based on Spring
   
# FOR USER'S OF THE CODE GENERATOR

## Usage:

First, it is important to know the project assets will be created in your current
directory, so be sure to navigate to the folder in which 
you want the project assets created before running any command.

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

# FOR MAINTAINERS (DEVELOPERS)
                    
## To Compile the Application

To build the application, run ```./gradlew :mojo:build```. 
The subproject ```mojo``` must be declared as shown. Alas,
the shadowJar task gets confused when only ```./gradlew build``` is used.
Building the application via
```./gradlew :mojo:build``` is an acceptable work-around. The ```mojo```
sub-project is the main entry point of the application, so it's a suitable
starting point. 

## To Deploy the Application

Navigate to the ```mojo``` sub-project, then to the ```build/distributions``` folder.
(that is, ```cd mojo/build/distributions```). (DO NOT use the tar/zip file
created in the project root build/distributions folder; that is not the uber jar.)

Copy the ```mojo-shadow.tar``` (or ```zip```) to the desired location
and unpack it, then add the ```mojo/bin/mojo``` executable to your PATH. 
                      
## To Extend the Application

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
