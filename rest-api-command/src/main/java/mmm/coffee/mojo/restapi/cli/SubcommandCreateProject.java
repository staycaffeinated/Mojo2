/*
 * Copyright 2020 Jon Caulfield
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mmm.coffee.mojo.restapi.cli;

import lombok.NonNull;
import mmm.coffee.mojo.exception.MojoException;
import mmm.coffee.mojo.mixin.DryRunOption;
import mmm.coffee.mojo.restapi.cli.validator.PackageNameValidator;
import mmm.coffee.mojo.restapi.generator.project.ProjectGeneratorFactory;
import mmm.coffee.mojo.restapi.generator.project.ProjectKeys;
import mmm.coffee.mojo.restapi.generator.helpers.NamingRules;
import mmm.coffee.mojo.restapi.shared.SupportedFeatures;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Generates boiler-plate code for a SpringMVC application.
 * The generated code is written in the user's current directory.
 *
 * Usage:
 * create-project -group-id=[GROUP-ID] -pkg=mmm.coffee.example -jpa -db=cassandra|postgres|mysql|mongodb
 *
 * Since we're just adding dependencies to the gradle file, I'm less worried about being strict about arg-groups.
 * For example, if -jpa is given but no database is given, the user can update their gradle file as needed;
 * the -jpa flag causes spring-starter-data-jpa to be included
 * the -db flag causes the appropriate dbms dependency to be included
 */
@CommandLine.Command(
        name="create-project",
        mixinStandardHelpOptions = true,
        headerHeading = "%nSynopsis:%n%n",
        header = "Generate a minimal, working implementation of a Spring REST application",
        descriptionHeading = "%nDescription:%n%n",
        description="Creates the starting project assets for a REST API application.",
        synopsisHeading = "%nUsage:%n%n",
        optionListHeading = "%nOptions:%n%n"
)

public class SubcommandCreateProject implements Callable<Integer> {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec commandSpec;  // injected by picocli

    @CommandLine.Mixin
    DryRunOption dryRunOption; // visible for testing

    // The groupId can be defaulted to match the base package 
    @CommandLine.Option(names = {"-g", "--group"},
            description = "The project's group-id (i.e., library coordinates)",
            paramLabel = "GROUP_ID")
    String groupId;  // package-visible for testing

    /**
     * The Programming Model determines whether the backend code will use Controllers or Handlers and Routers.
     * In the 'webmvc' programming model, the generated code is depends on Spring-MVC.
     * In the 'webflux' programming model, the generated code depends on Spring-WebFlux.
     *
     * The -f flag (f for 'flavor') indicates the programming model followed by the generated code.
     * Spring provides two base libraries: SpringMVC and Spring WebFlux.
     * SpringMVC is for applications that desire synchronous, or blocking, API calls, such as a typical CRUD application.
     * Spring WebFlux is for asynchronous (non-blocking) API calls, such as applications that continually refresh,
     * such as a stock ticker application or a monitoring application.
     * WebFlux has 2 programming models: Annotated Controllers; Functional Endpoints.
     * Annotated Controllers work like MVC controllers.
     * Functional Endpoints are lambda-based functional programming model. These employ Handlers and Routers.
     *
     * See https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html
     */
    @CommandLine.Option(names = { "-f", "--framework" },
            description = { "This indicates the underlying framework to use, such as spring-webmvc (-f webmvc) or spring-webflux (-f webflux)" },
            defaultValue = "webmvc",
            paramLabel = "FRAMEWORK")
    SupportedFramework framework; // package-visible for testing


    /**
     * This is the name of the artifact to be generated by the project.
     */
    @CommandLine.Option(names = {"-n", "--name"},
            description = "The application name, or artifact name (defaults to 'example-service')",
            defaultValue = "example-service",
            paramLabel = "APPLICATION_NAME")
    String applicationName; // visible for testing


    /**
     * This is the base Java package to apply to the generated Java files
     */
    @CommandLine.Option(names = {"-p", "--package"},
            description = "The base Java package for this project; for example: 'org.example.widget'",
            required = true,
            paramLabel = "JAVA_PACKAGE")
    String packageName; // visible for testing

    /**
     * This is the base URL to the running application.
     */
    @CommandLine.Option(names = {"-r", "--route", "--base-path"},
            description = "The base path (URL) of this application; for example: '/my-service.",
            defaultValue = "/",
            paramLabel = "BASE_PATH")
    String basePath; // visible for testing


    @CommandLine.Option(names = {"-S", "--schema"},
            description = "The database schema to be used by the application",
            defaultValue = "examples",
            paramLabel = "DATABASE_SCHEMA")
    String dbmsSchema; // visible for testing


    //
    // The Support option. These are the added capabilities supported by the generated application.
    // I've been back and forth with what to name this flag.  Spring Initialzr has the notion of
    // 'dependencies', but all Spring Initializr does is add those dependencies to the gradle/maven build file.
    // Micronaut uses the term 'feature', but added libraries aren't exactly 'features' of the
    // generated application. The term 'trait' seems in line: for example, a 'postgres trait' indicates
    // the traits of a Postgres database are enabled in the application. Along that line, 'support'
    // also seems like a good term: the generated application will 'support' postgres or liquibase or CORS
    // or heath checks or whatever
    // First, we have to provide an Iterable<String> that provides the list of valid candidates
    //
    // The declaration the additional library options
    // For example, ```--add postgres liquibase```
    @CommandLine.Option(names={"-s", "--support"},
                        arity="0..*",
                        paramLabel = "LIBRARY",
                        description = { "Add the support of additional libraries to the project. Currently supported libraries are: ${COMPLETION-CANDIDATES}." }
                        )
    private SupportedFeatures[] features;

    // A Command is used to designate the dependencies to add to the project in an effort to
    // make it simple to specify a handful of dependencies easily.  With this approach,
    // the end-user specifies something like:  ```-features postgres liquibase testcontainers```
    // The downside is, we don't have an obvious way to tell the user what's accepted as a feature.
    // For example, ```-features foobar``` isn't a dependency the generator knows about.

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     */
    @Override
    public Integer call() {
        validate();     // verify command-line argument values

        boolean dryRun = dryRunOption.isDryRun();

        // Set-up the ProjectInfo to send to the generator
        Map<String,Object> map = new HashMap<>();
        map.put(ProjectKeys.BASE_PACKAGE, nullSafeValue(packageName));
        map.put(ProjectKeys.BASE_PACKAGE, nullSafeValue(packageName));
        map.put(ProjectKeys.GROUP_ID, nullSafeValue(groupId));
        map.put(ProjectKeys.APPLICATION_NAME, nullSafeValue(applicationName));
        map.put(ProjectKeys.SCHEMA, nullSafeValue(NamingRules.toDatabaseSchemaName(dbmsSchema)));
        map.put(ProjectKeys.BASE_PATH, nullSafeValue(basePath));
        map.put(ProjectKeys.FRAMEWORK, framework);
        map.put("features", features);
        if (dryRun)
            map.put(DryRunOption.DRY_RUN_KEY, Boolean.TRUE);

        try {
            var projectGenerator = ProjectGeneratorFactory.createProjectGenerator(framework);
            projectGenerator.run(map);
            return 0;
        }
        catch (Exception e) {
            throw new MojoException(e.getMessage());
        }
    }

    private void validate() {
        if ( PackageNameValidator.isNotValid(packageName)) {
            throw new CommandLine.ParameterException( commandSpec.commandLine(),
                    String.format("The package name '%s' is not a legal Java package name", packageName));
        }
    }
    
    @NonNull private String nullSafeValue(String value) {
        if (value == null) return "";
        return value;
    }
}
