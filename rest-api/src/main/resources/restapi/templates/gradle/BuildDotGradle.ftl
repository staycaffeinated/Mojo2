plugins {
    id 'org.springframework.boot' version '${project.springBootVersion}'
    id 'io.spring.dependency-management' version '${project.springDependencyManagementVersion}'
    id 'com.coditory.integration-test' version '1.1.11'
    id 'java'
    id 'idea'
    id 'jacoco'
    id 'org.sonarqube' version '3.1.1'
    id 'com.github.ben-manes.versions' version '0.36.0'
}

apply from: "gradle/dependencies.gradle"

apply plugin: 'io.spring.dependency-management'

sourceCompatibility = '${project.javaVersion}'
targetCompatibility = '${project.javaVersion}'

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
    // Note: libs = rootProject.ext.libs

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

// Make all tests use JUnit5
tasks.withType(Test) {
    useJUnitPlatform()
    testLogging { events "passed", "skipped", "failed" }
}

// Configure & enable lint
tasks.withType(JavaCompile).all {
    options.compilerArgs << "-Xlint:unchecked" << "-Xlint:deprecation" << "-Xlint:rawtypes"
}

<#noparse>
// --------------------------------------------------------------------------------
// Lint options (defaulting to flag everything; turn off what you don't want)
// --------------------------------------------------------------------------------
compileJava.options*.compilerArgs = [
    "-Xlint:serial",    "-Xlint:varargs",     "-Xlint:cast",        "-Xlint:classfile",
    "-Xlint:dep-ann",   "-Xlint:divzero",     "-Xlint:empty",       "-Xlint:finally",
    "-Xlint:overrides", "-Xlint:path",        "-Xlint:-processing", "-Xlint:static",
    "-Xlint:try",       "-Xlint:fallthrough", "-Xlint:rawtypes",    "-Xlint:deprecation",
    "-Xlint:unchecked", "-Xlint:-options"
    ]

compileTestJava.options*.compilerArgs = [
    "-Xlint:serial",     "-Xlint:varargs",      "-Xlint:cast",        "-Xlint:classfile",
    "-Xlint:dep-ann",    "-Xlint:divzero",      "-Xlint:empty",       "-Xlint:finally",
    "-Xlint:overrides",  "-Xlint:path",         "-Xlint:-processing", "-Xlint:static",
    "-Xlint:try",        "-Xlint:-fallthrough", "-Xlint:-rawtypes",   "-Xlint:-deprecation",
    "-Xlint:-unchecked", "-Xlint:-options"
    ]
</#noparse>
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
        // Exclude: exception classes, EJB bean classes, test classes
        property 'sonar.coverage.exclusions', '**/*Exception.java,**/*Test*.java,**/*IT.java,**/*Resource.java,**/*Application.java,**/ServletInitializer.java'
    }
}
tasks['sonarqube'].dependsOn test
tasks['sonarqube'].dependsOn integrationTest

