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

import mmm.coffee.mojo.restapi.generator.EndpointGenerator;
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
    @CommandLine.Option(
            names={"-resource"},
            description="The resource available at this endpoint (e.g: -resource=Coffee)",
            paramLabel = "ENTITY-NAME")
    private String resourceName;

    @CommandLine.Option(
            names={"-route"},
            description="The base path to the resource (e.g: -route=/coffee)",
            paramLabel = "BASE-PATH")
    private String baseRoute;

    @Override
    public Integer call() throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("resource", resourceName);
        map.put("route", baseRoute);

        EndpointGenerator generator = new EndpointGenerator();
        generator.run(map);
        return 1;
    }
}