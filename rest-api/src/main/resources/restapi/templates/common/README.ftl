

## Notes for Developers

### To Compile the Application

To build the application, run ```./gradlew build```.

To run the application, run ```./gradlew bootRun```.  Naturally, if there are any external dependencies,
such as a PostgreSQL database, those have to be started first.


### To Export Results to SonarQube

Before code metrics can be exported to SonarQube, the ```gradle.properties``` file
must be updated with configuration values that match your environment.

A local SonarQube instance can be acquired by starting up a SonarQube docker instance.
See https://docs.sonarqube.org/latest/setup/get-started-2-minutes/ and https://hub.docker.com/_/sonarqube for
specifics.  Generally, this command will suffice:

```docker run -d --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest```

Once the Sonarqube instance is running, log in to ```http://localhost:9000``` using the default credentials admin/admin.
You will be prompted to change the default password. Whatever new password you select, update the ```gradle.properties```
with that password.  When that's done, code metrics from this project can be exported to that Sonarqube server.

To build, test, report test coverage, and export the test coverage to Sonarqube, run

```./gradlew clean build jacocoTestReport sonarqube```

### To Check for Latest Library Versions

The build includes the _gradle-versions_ plugin, which will report on any outdated libraries.
To run this report, run this gradle task:

```./gradlew dependencyUpdates```

which prints its report to the console.  See the [plugin documentation](https://github.com/ben-manes/gradle-versions-plugin)
for a deep dive.