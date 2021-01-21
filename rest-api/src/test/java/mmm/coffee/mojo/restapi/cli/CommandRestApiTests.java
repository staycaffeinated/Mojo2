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


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests of the RestApi command
 */
class CommandRestApiTests {

    final CommandRestApi commandRestApi = new CommandRestApi();
    final CommandLine cli = new CommandLine(commandRestApi);

    @BeforeEach
    public void setUpEachTime() {
        cli.clearExecutionResults();
    }

    // This test is handy for debugging the command set-up.
    // If any picocli annotations are badly configured, this test will expose those.
    @Disabled("For debugging, not for validation")
    void testUsage() {
        cli.usage(System.out);
    }

    @Test
    void shouldHaveNoOpCallMethod() {
        assertThat(commandRestApi.call()).isNull();
    }

    @Test
    void shouldHaveHelpOption() {
        assertThat( cli.execute(toArgV("--help"))).isEqualTo(0);
    }

    @Test
    void shouldHaveSubcommands() {
        Map<String, CommandLine> subcommands = cli.getSubcommands();
        assertThat(subcommands).isNotEmpty();
        // These are the subcommands of rest-api
        assertThat(subcommands.get("create-project")).isNotNull();
        assertThat(subcommands.get("create-endpoint")).isNotNull();
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
