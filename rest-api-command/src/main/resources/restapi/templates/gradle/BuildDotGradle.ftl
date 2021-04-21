plugins {
    id 'org.springframework.boot' version '${project.springBootVersion}'
    id 'io.spring.dependency-management' version '${project.springDependencyManagementVersion}'
    id 'com.coditory.integration-test' version '1.2.1'
    id 'java'
    id 'idea'
    id 'jacoco'
    id 'org.sonarqube' version '3.1.1'
    id 'com.github.ben-manes.versions' version '${project.benManesPluginVersion}'
    id 'com.google.cloud.tools.jib' version '${project.jibPluginVersion}'
    id 'com.diffplug.spotless' version '${project.spotlessVersion}'
}

apply from: "gradle/dependencies.gradle"            // our library dependencies
apply from: "gradle/jib.gradle"                     // the Google Jib plugin to create Docker images
apply from: "gradle/sonarqube.gradle"               // the Sonarqube and Jacoco plugins for code coverage
apply from: "gradle/lint.gradle"                    // the lint flags for compilation
apply from: "gradle/spotless.gradle"                // the Eclipse Spotless plugin for code formatting

apply plugin: 'io.spring.dependency-management'

sourceCompatibility = '${project.javaVersion}'
targetCompatibility = '${project.javaVersion}'

version='0.0.1'

configurations {
    developmentOnly
    runtimeClasspath {
        extendsFrom developmentOnly
    }
    compileOnly {
        extendsFrom annotationProcessor
    }
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor libs.springBootConfigProcessor
    annotationProcessor libs.lombok

    compileOnly libs.lombok

    developmentOnly libs.springDevTools

    implementation libs.springBootStarterActuator
    implementation libs.springBootStarterWeb
    implementation libs.springBootStarterDataJpa
    implementation libs.problemSpringWeb
<#if (project.liquibase)??>
    implementation libs.liquibaseCore
</#if>

<#if (project.postgres)??>
    runtimeOnly libs.postgresql
<#else>
    runtimeOnly libs.h2
</#if>

<#if (project.testcontainers)??>
    testImplementation libs.springCloud
    testImplementation platform( libs.testContainersBom )
    testImplementation libs.testContainersJupiter
    <#if (project.postgres)??> <#-- if (testcontainers && postgres) -->
    testImplementation libs.testContainersPostgres
    </#if>
<#else>
    <#-- if testcontainers aren't in use, default to using H2 to enable -->
    <#-- out-of-the-box tests to work until a QA DB is set up by the developer. -->
    testRuntimeOnly libs.h2
</#if>

    testImplementation (libs.springBootStarterTest){
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }
    testImplementation (platform( libs.junitBillOfMaterial ))
    testImplementation (libs.junitJupiter)

}

// --------------------------------------------------------------------------------
// Make all tests use JUnit 5
// --------------------------------------------------------------------------------
tasks.withType(Test) {
    useJUnitPlatform()
    testLogging { events "passed", "skipped", "failed" }
}
