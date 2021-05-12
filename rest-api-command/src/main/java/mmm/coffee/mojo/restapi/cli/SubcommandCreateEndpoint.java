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

import mmm.coffee.mojo.api.Generator;
import mmm.coffee.mojo.mixin.DryRunOption;
import mmm.coffee.mojo.restapi.generator.AbstractEndpointGenerator;
import mmm.coffee.mojo.restapi.generator.EndpointGeneratorFactory;
import mmm.coffee.mojo.restapi.generator.helpers.MojoProperties;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;
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
        Map<String,Object> map = new HashMap<>();
        map.put("resource", resourceName);
        map.put("route", baseRoute);

        if (dryRunOption.isDryRun()) {
            map.put(DryRunOption.DRY_RUN_KEY, Boolean.TRUE);
        }

        // TODO: Need to peek at mojo.properties to determine framework code
        Generator generator = EndpointGeneratorFactory.createGenerator(SupportedFramework.WEBMVC);
        generator.run(map);
        return 0;
    }
}