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

import mmm.coffee.mojo.mixin.DryRunOption;
import mmm.coffee.mojo.restapi.cli.validator.ResourceNameValidator;
import mmm.coffee.mojo.restapi.generator.EndpointGeneratorFactory;
import mmm.coffee.mojo.restapi.generator.ProjectKeys;
import mmm.coffee.mojo.restapi.shared.MojoProperties;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * This subcommand generates the code assets for a specific resource.
 */
@CommandLine.Command(
        name="create-endpoint",
        mixinStandardHelpOptions = true,
        headerHeading = "%nSynopsis:%n%n",
        header = "Generate a REST endpoint implementation in the current project",
        synopsisHeading = "%nUsage:%n%n",
        descriptionHeading = "%nDescription:%n%n",
        description="Create a REST API endpoint implementation",
        optionListHeading = "%nOptions:%n%n"
)
public class SubcommandCreateEndpoint implements Callable<Integer> {

    @CommandLine.Spec
    CommandLine.Model.CommandSpec commandSpec;  // injected by picocli

    // adds support for the --dry-run option
    @CommandLine.Mixin
    private DryRunOption dryRunOption;

    @CommandLine.Option(
            names={"-e", "--resource"},
            description="The resource (entity) available at this endpoint (e.g: --resource Coffee)",
            paramLabel = "ENTITY")
    private String resourceName;

    @CommandLine.Option(
            names={"-p", "--route"},
            description="The route (path) to the resource (e.g: --route /coffee)",
            paramLabel = "ROUTE")
    private String baseRoute;

    @Override
    public Integer call() {
        validate();

        Map<String,Object> map = new HashMap<>();
        map.put("resource", resourceName);
        map.put("route", baseRoute);

        if (dryRunOption.isDryRun()) {
            map.put(DryRunOption.DRY_RUN_KEY, Boolean.TRUE);
        }
        // If a mojo.properties file is not found, env variables will be queried
        var configuration = new MojoProperties().getConfiguration();

        SupportedFramework framework = determineFramework(dryRunOption.isDryRun());
        var generator = EndpointGeneratorFactory.createGenerator(framework);
        generator.run(map, configuration);
        return 0;
    }

    /**
     * Validate the inputs. In particular, ensure the resourceName will yield a valid Java class name.
     * For example, if the resourceName is 'import', that should not be allowed since 'import' is a
     * reserved word and cannot be used as a classname and will cause the generator to emit code
     * that won't compile. 
     */
    private void validate() {
        if ( !ResourceNameValidator.isValid(resourceName)) {
            throw new CommandLine.ParameterException( commandSpec.commandLine(),
                    String.format("%nERROR: %n\tThe resource name '%s' cannot be used; it will not produce a legal Java class name.%n\tResource names must not be Java reserved words and must begin with a letter.%n", resourceName));
        }
    }

    /**
     * Side-bar task to look up the project's framework, since the framework
     * affects whether the code generator produces WebMVC or WebFlux controllers
     *
     * @param isDryRun when doing a dry-run, the env variable 'mojo.framework'
     *                 is inspected (used for testing)
     */
    private SupportedFramework determineFramework(boolean isDryRun) {
        if (isDryRun) {
            Optional<SupportedFramework> framework = getFrameworkFromEnv();
            return framework.orElse(SupportedFramework.WEBMVC);
        }
        else {
            var stringValue = new MojoProperties().getConfiguration().getString(ProjectKeys.FRAMEWORK);
            return SupportedFramework.valueOf(stringValue);
        }
    }

    /**
     * Returns the supported framework based on the env variable, 'mojo.framework'.
     * If that env variable is undefined, an empty Optional is returned.
     *
     * @return an Optional that wraps the project's framework
     */
    private Optional<SupportedFramework> getFrameworkFromEnv() {
        var s = System.getenv("mojo.framework");
        if (isEmpty(s)) {
            return Optional.empty();
        }
        try {
            return Optional.of(SupportedFramework.valueOf(s));
        }
        catch (IllegalArgumentException | NullPointerException e) {
            return Optional.empty();
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }
}