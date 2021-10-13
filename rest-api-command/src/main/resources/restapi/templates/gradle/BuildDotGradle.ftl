plugins {
    id 'org.springframework.boot' version '${project.springBootVersion}'
    id 'io.spring.dependency-management' version '${project.springDependencyManagementVersion}'
    id 'com.coditory.integration-test' version '1.3.0'
    id 'java'
    id 'idea'
    id 'jacoco'
    id 'org.sonarqube' version '${project.sonarqubeVersion}'
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

// project.framework: ${project.framework}

<#if (project.framework == 'WEBFLUX')>
    <#include "SpringWebFluxDependencies.ftl">
<#else>
    <#include "SpringWebMvcDependencies.ftl">
</#if>


// --------------------------------------------------------------------------------
// Make all tests use JUnit 5
// --------------------------------------------------------------------------------
tasks.withType(Test) {
    useJUnitPlatform()
    testLogging { events "passed", "skipped", "failed" }
}

// --------------------------------------------------------------------------------
// Jib specific configuration for this application
// --------------------------------------------------------------------------------
jib {
    to {
        image = '${project.applicationName}'
        tags = [ 'latest', 'jdk-17', '0.0.1' ]
    }
}
