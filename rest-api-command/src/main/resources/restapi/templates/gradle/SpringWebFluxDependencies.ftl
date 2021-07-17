dependencies {
    annotationProcessor libs.springBootConfigProcessor
    annotationProcessor libs.lombok

    compileOnly libs.lombok

    developmentOnly libs.springDevTools

    // TODO: Add to dependencies.gradle
    implementation('io.r2dbc:r2dbc-h2:0.8.4.RELEASE')
    implementation('org.springframework.boot:spring-boot-starter-data-r2dbc')
    implementation('org.springframework.boot:spring-boot-starter-aop')
    runtimeOnly('com.h2database:h2:1.4.200')
    implementation('com.fasterxml.jackson.datatype:jackson-datatype-jsr310')

    implementation libs.springBootStarterActuator
    implementation libs.springBootStarterWebFlux
    implementation libs.springBootStarterValidation
    implementation libs.problemSpringWebFlux
    implementation libs.problemJacksonDataType

    // TODO: Add to dependencies.gradle
    testAnnotationProcessor libs.lombok
    testCompileOnly libs.lombok
    testImplementation (libs.springBootStarterTest){
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }
    testImplementation (platform( libs.junitBillOfMaterial ))
    testImplementation libs.junitJupiter
    testImplementation libs.reactorTest
}