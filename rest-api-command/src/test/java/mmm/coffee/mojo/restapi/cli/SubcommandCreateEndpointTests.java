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
import mmm.coffee.mojo.restapi.shared.Environment;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import picocli.CommandLine;

import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

/**
 * Unit tests
 */
class SubcommandCreateEndpointTests {

    final SubcommandCreateEndpoint command = new SubcommandCreateEndpoint();
    final CommandLine cli = new CommandLine(command);

    @BeforeEach
    public void setUpEachTime() {
        cli.clearExecutionResults();
    }

    @Disabled("For debugging, not for validation")
    void testUsage() {
        cli.usage(System.out);
    }

    /**
     * There are no subcommands nested within the 'create-endpoint' command
     */
    @Test
    void shouldHaveNoSubcommands() {
        Map<String,CommandLine> subcommands = cli.getSubcommands();
        assertThat(subcommands).isEmpty();
    }

    @Test
    void shouldEnableHelpOption() {
        assertThat(cli.execute("--help")).isEqualTo(0);
    }

    @Test
    void shouldRejectInvalidResourceNames() {
        // Expect a non-zero return code when an error occurs.
        // 'import' is a reserved word in java, so we want the code generator to
        // catch errors like that early
        assertThat(cli.execute("--resource", "import", "--route", "/import")).isNotEqualTo(0);
    }

    @Test
    void shouldReturnSuccessWhenWellFormedCommand() throws Exception {
        // given
        // mock the DryRunOption to mark this as a dry-run to avoid generating files
        var mockDryRunOption = Mockito.mock(DryRunOption.class);
        when(mockDryRunOption.isDryRun()).thenReturn(true);

        // given these presumed command line arguments
        var mutable = new SubcommandCreateEndpoint();
        mutable.baseRoute = "/treasure";
        mutable.resourceName = "Treasure";
        mutable.dryRunOption = mockDryRunOption;

        // given the mojo.properties file (in usual circumstances) contains these properties
        // (for testing scope, properties expected to be in mojo.properties can be set as env vars instead)
        Environment.addVariable("framework", SupportedFramework.WEBFLUX.toString());
        Environment.addVariable("basePath", "/zork");
        Environment.addVariable("basePackage", "acme.adventures.zork");

        // when the command is executed
        int returnCode = mutable.call();

        // then expect a successful run
        assertThat(returnCode).isEqualTo(0);
    }
}
