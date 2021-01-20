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

import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * The rest-api command
 */
@CommandLine.Command(
        name="rest-api",
        descriptionHeading = "%nDescription:%n%n",
        description="Create a REST API project and its artifacts",
        version = "v1.0", mixinStandardHelpOptions = true,
        commandListHeading = "%nCommands:%n%n",
        subcommands = { SubcommandCreateProject.class, SubcommandCreateEndpoint.class }
)
public class CommandRestApi implements Callable<Integer> {
    @Override
    public Integer call() {
        return null;
    }
}
