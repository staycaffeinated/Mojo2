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
package mmm.coffee.mojo;

import mmm.coffee.mojo.restapi.cli.CommandRestApi;
import picocli.CommandLine;

import java.util.concurrent.Callable;

/**
 * The main application for the code generator
 */
@CommandLine.Command(
        name="mojo",
        description="Code generation mojo for programmers",
        version = "0.1.5",
        mixinStandardHelpOptions = true,
        subcommands = { CommandRestApi.class }
)
public class MojoApplication implements Callable<Integer> {


    @Override
    public Integer call() {
        return 0;
    }

    /**
     * Main
     * @param args command line args
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new MojoApplication()).execute(args);
        System.exit(exitCode);
    }
}
