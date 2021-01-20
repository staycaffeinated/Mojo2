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
import mmm.coffee.mojo.restapi.cli.validator.PackageNameValidator;
import mmm.coffee.mojo.restapi.generator.ProjectGenerator;
import mmm.coffee.mojo.restapi.generator.ProjectKeys;
import mmm.coffee.mojo.restapi.generator.SyntaxRules;
import mmm.coffee.mojo.restapi.shared.SupportedFeatures;
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
        header = "Generate a basic implementation of a Spring REST application",
        descriptionHeading = "%nDescription:%n%n",
        description="Create a working area for a REST API application.",
        synopsisHeading = "%nUsage:%n%n",
        optionListHeading = "%nOptions:%n%n"
)
// TODO: add a -dryrun flag so we can test flags w/o invoking the code generator
public class SubcommandCreateProject implements Callable<Integer> {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec commandSpec;  // injected by picocli

    // The groupId can be defaulted to match the base package 
    @CommandLine.Option(names = {"-g", "--group"},
            description = "The project's group-id (i.e., library coordinates)",
            paramLabel = "GROUP_ID")
    private String groupId;

    @CommandLine.Option(names = {"-p", "--package"},
            description = "The base Java package for this project; for example: 'org.example.widget'",
            required = true,
            paramLabel = "PACKAGE")
    private String packageName;

    @CommandLine.Option(names = {"-n", "--name"},
            description = "The application name",
            defaultValue = "example",
            paramLabel = "APPLICATION_NAME")
    private String applicationName;

    @CommandLine.Option(names = {"--schema"},
            description = "The database schema to be used by the application",
            defaultValue = "examples",
            paramLabel = "DATABASE_SCHEMA")
    private String dbmsSchema;

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
                        description = "Add the support of additional libraries to the project. Valid values: ${COMPLETION-CANDIDATES}"
                        )
    private SupportedFeatures[] features;

    // A Command is used to designate the dependencies to add to the project in an effort to
    // make it simple to specify a handful of dependencies easily.  With this approach,
    // the end-user specifies something like:  ```-features postgres liquibase testcontainers```
    // The downside is, we don't have an obvious way to tell the user what's accepted as a feature.
    // For example, ```-features foobar``` isn't a dependency the generator knows about.
    // TODO: Explore how to present help for this specific command to allow us to tell a user what we expect
    // e.g: supporting ```-features --help``` would be really helpful.

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     */
    @Override
    public Integer call() {
        validate();

        Map<String,Object> map = new HashMap<>();

        // todo: declare a type, ProjectSpec to have type-safe values?

        map.put(ProjectKeys.BASE_PACKAGE, nullSafeValue(packageName));
        map.put(ProjectKeys.GROUP_ID, nullSafeValue(groupId));
        map.put(ProjectKeys.APPLICATION_NAME, nullSafeValue(applicationName));
        map.put(ProjectKeys.SCHEMA, nullSafeValue(SyntaxRules.schemaSyntax(dbmsSchema)));
        map.put("features", features);

        try {
            ProjectGenerator projectGenerator = new ProjectGenerator();
            projectGenerator.run(map);
            return 0;
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }

    private void validate() {
        if ( !PackageNameValidator.isValid(packageName)) {
            throw new CommandLine.ParameterException( commandSpec.commandLine(),
                    String.format("The package name '%s' is not a legal Java package name", packageName));
        }
    }

    /**
     * Determine if {@code s} is either null or an empty string
     * @param s the value to check
     * @return true if {@code s} is either null or an empty string
     */
    private boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    @NonNull private String nullSafeValue(String value) {
        if (value == null) return "";
        return value;
    }
    @NonNull private String nullSafeBoolean(Boolean value) {
        if (value == null) return "false";
        return value.toString();
    }
}
