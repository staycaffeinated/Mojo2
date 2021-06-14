// --------------------------------------------------------------------------------
// Jacoco and Sonarqube configuration
// --------------------------------------------------------------------------------
test {
    finalizedBy jacocoTestReport
}

jacocoTestReport {
    dependsOn test
    dependsOn integrationTest
    reports {
        xml.enabled(true)   // sonarqube needs xml format
        html.enabled(true)  // for our local viewing pleasure
    }
}

sonarqube {
    properties {
        // Exclude: exceptions, tests, EJB beans, and the main application class
        property 'sonar.coverage.exclusions', '**/*Exception.java,**/*Test*.java,**/*IT.java,**/*Resource.java,**/*Application.java,**/ServletInitializer.java'
    }
}
tasks['sonarqube'].dependsOn test
tasks['sonarqube'].dependsOn integrationTest