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
import mmm.coffee.mojo.restapi.generator.ProjectGenerator;
import mmm.coffee.mojo.restapi.generator.ProjectKeys;
import mmm.coffee.mojo.restapi.generator.SyntaxRules;
import mmm.coffee.mojo.restapi.shared.SupportedFeatures;
import picocli.CommandLine;

import java.util.*;
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
        name="project",
        description="Create a REST API project. By default, the project is created in the current directory",
        // TODO: support -d | --directory to specify the project's root directory
        mixinStandardHelpOptions = true
)
// TODO: add a -dryrun flag so we can test flags w/o invoking the code generator
public class SubcommandCreateProject implements Callable<Integer> {

    // The groupId can be defaulted to match the base package 
    @CommandLine.Option(names = {"-g", "--group"}, description = "the project's group-id (library coordinates)")
    private String groupId;

    @CommandLine.Option(names = {"-p", "--package"}, description = "the base package for this project", required = true)
    private String packageName;

    @CommandLine.Option(names = {"-n", "--name"}, description = "the application name", defaultValue = "widget-service")
    private String applicationName;

    @CommandLine.Option(names = {"-s", "--schema"}, description = "the database schema name", defaultValue = "widgets")
    private String dbmsSchema;

    //
    // The Feature option (should we rename this to 'dependency' ?)
    // First, we have to provide an Iterable<String> that provides the list of valid candidates
    //
//    private static class FeatureCandidates implements Iterable<String> {
//        private String[] candidates = { "postgres", "liquibase", "testcontainers" };
//
//        @Override
//        public Iterator<String> iterator() {
//            return Arrays.asList(candidates).iterator();
//        }
//    }
    // The declaration the additional library options
    // For example, ```--add postgres liquibase```
    @CommandLine.Option(names={"-a", "--add"},
                        arity="0..*",
                        description = "Additional libraries to include. Valid values: ${COMPLETION-CANDIDATES}"
                        )
    private SupportedFeatures[] features;

    // A Command is used to designate the dependencies to add to the project in an effort to
    // make it simple to specify a handful of dependencies easily.  With this approach,
    // the end-user specifies something like:  ```-features postgres liquibase testcontainers```
    // The downside is, we don't have an obvious way to tell the user what's accepted as a feature.
    // For example, ```-features foobar``` isn't a dependency the generator knows about.
    // TODO: Explore how to present help for this specific command to allow us to tell a user what we expect
    // e.g: supporting ```-features --help``` would be really helpful.
//    private List<String> featureList = new ArrayList<>();
//    @CommandLine.Command(name = "-features",
//            description = "Additional libraries to include as dependencies. For example: postgres, liquibase"
//    )
//    void subCommandFeatureList(
//            @CommandLine.Parameters(
//                    arity = "1..*",
//                    paramLabel = "<library>",
//                    description = "A library to include") String[] args) {
//        // Copy the list of features into the featureList
//        Arrays.stream(args).sequential().forEach(it -> featureList.add(it));
//    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Integer call() throws Exception {
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
