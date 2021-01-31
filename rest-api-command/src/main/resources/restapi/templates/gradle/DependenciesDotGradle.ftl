ext {
    versions = [
        assertJ              : '3.15.0',            // Assertion library for test cases
        junitSystemRules     : '1.19.0',            // JUnit extensions
        junit                : '5.7.0',             // JUnit Jupiter
        liquibase            : '4.2.2',             // Database schema initialization & evolution
        lombok               : '1.18.16',           // Lombok annotation processor
        log4j                : '2.14.0',            // Logging
        mockito              : '3.10.0',            // Mocking library
        springBoot           : '${project.springBootVersion}',     // Spring Boot
        springCloud          : "2.2.5.RELEASE",     // Spring Cloud
        problemSpringWeb     : '0.26.2',            // Zalando's Problem API
        testContainers       : '1.15.1',            // Test containers for integration testing
        truth                : '1.1'                // Google's assertion library
        ]
<#noparse>
    libs = [
        assertJ                     : "org.assertj:assertj-core:$versions.assertJ",
        h2                          : "com.h2database:h2",
        junitBillOfMaterial         : "org.junit:junit-bom:$versions.junit",
        junitJupiter                : "org.junit.jupiter:junit-jupiter",
        junitPlatformRunner         : "org.junit.platform:junit-platform-runner",

        // See https://stefanbirkner.github.io/system-rules/
        junitSystemRules            : "com.github.stefanbirkner:system-rules:$versions.junitSystemRules",

        log4j                       : "org.apache.logging:log4j:log4j-core:$versions.log4j",
        lombok                      : "org.projectlombok:lombok:$versions.lombok",
        liquibaseCore               : "org.liquibase:liquibase-core:$versions.liquibase",
        mockito                     : "org.mockito:mockito-core:$versions.mockito",
        postgresql                  : "org.postgresql:postgresql",
        problemSpringWeb            : "org.zalando:problem-spring-web-starter:$versions.problemSpringWeb",
        truth                       : "com.google.truth:truth:$versions.truth",

        // Spring's dependency management plugin will auto-resolve the Spring library versions
        springBootConfigProcessor   : "org.springframework.boot:spring-boot-configuration-processor",
        springBootStarterActuator   : "org.springframework.boot:spring-boot-starter-actuator",
        springBootStarterWeb        : "org.springframework.boot:spring-boot-starter-web",
        springBootStarterDataJpa    : "org.springframework.boot:spring-boot-starter-data-jpa",
        springBootStarterTest       : "org.springframework.boot:spring-boot-starter-test",
        springCloud                 : "org.springframework.cloud:spring-cloud-starter:$versions.springCloud",
        springDevTools              : "org.springframework.boot:spring-boot-devtools",

        testContainersBom           : "org.testcontainers:testcontainers-bom:$versions.testContainers",
        testContainersPostgres      : "org.testcontainers:postgresql",
        testContainersJupiter       : "org.testcontainers:junit-jupiter"
        ]
}
</#noparse>