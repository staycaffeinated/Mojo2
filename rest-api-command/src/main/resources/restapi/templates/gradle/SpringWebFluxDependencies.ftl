dependencies {
    annotationProcessor libs.springBootConfigProcessor
    annotationProcessor libs.lombok

    compileOnly libs.lombok

    developmentOnly libs.springDevTools

    implementation libs.springBootStarterActuator
    implementation libs.springBootStarterWebFlux
    implementation libs.springBootStarterValidation
    implementation libs.problemSpringWebFlux
    implementation libs.problemJacksonDataType

    testImplementation (libs.springBootStarterTest){
        exclude group: 'org.junit.vintage', module: 'junit-vintage-engine'
    }
    testImplementation (platform( libs.junitBillOfMaterial ))
    testImplementation libs.junitJupiter
    testImplementation libs.reactorTest
}