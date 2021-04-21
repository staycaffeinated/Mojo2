// --------------------------------------------------------------------------------
// The Jib plugin for Docker
// --------------------------------------------------------------------------------
jib {
    from {
        image = 'openjdk:17-jdk-alpine'
    }
    to {
        image = '${project.applicationName}'
        tags = [ 'latest', 'jdk-17', '0.0.1-SNAPSHOT' ]
    }
    container {
        format = 'OCI'
    }
}
// This next line causes the Docker image to be rebuilt after every `gradlew build` command.
// Its safe to comment out this line if you do not want this behavior.
tasks.build.dependsOn tasks.jibDockerBuild