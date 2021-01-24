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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests
 */
class MojoTest {

    MojoApplication application = new MojoApplication();
    CommandLine cli = new CommandLine(application);

    @BeforeEach
    public void setUpEachTime() {
        cli.clearExecutionResults();
    }


    /**
     * This is useful for verifying the picocli Command, Option, etc.
     * annotations are configured correctly. Calling usage() causes
     * the annotations to be evaluated, so any misconfigurations are detected.
     */
    @Disabled("For debugging, not for validation")
    void testUsage() {
        cli.usage(System.out);
    }

    /**
     * confirm the mojo application contains the rest-api subcommand
     */
    @Test
    void shouldRecognizeRestApiSubCommand() {
        // The Mojo application has at least the rest-api subcommand
        Map<String,CommandLine> subcommands = cli.getSubcommands();
        assertThat(subcommands).isNotEmpty();
        CommandLine restApi = subcommands.get("rest-api");
        assertThat(restApi).isNotNull();

        // The rest-api subcommand contains 2 subcommands itself,
        // create-project and create-endpoint
        Map<String,CommandLine> restApiSubCommands = restApi.getSubcommands();
        assertThat(restApiSubCommands).isNotEmpty();
    }

    @Test
    void shouldHaveHelpOption() {
        int rc = cli.execute(toArgV("--help"));
        assertThat(rc).isEqualTo(0);
    }
    
    /**
     * The cli.execute() command expects a varargs {@code String...} value,
     * so its our responsibility to separate the command-line args
     * into the appropriate String array.
     */
    private String[] toArgV(String s) {
        return s.split("\\s");
    }
}
