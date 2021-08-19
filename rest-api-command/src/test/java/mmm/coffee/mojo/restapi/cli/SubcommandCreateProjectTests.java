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
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import picocli.CommandLine;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * Unit tests
 */
class SubcommandCreateProjectTests {

    final SubcommandCreateProject command = new SubcommandCreateProject();
    final CommandLine cli = new CommandLine(command);

    @BeforeEach
    public void setUpEachTime() {
        cli.clearExecutionResults();
    }

    @Disabled("for debugging, not for validation")
    void testUsage() {
        cli.usage(System.out);
    }

    @Test
    void shouldEnableHelpOption() {
        assertThat (cli.execute("--help")).isEqualTo(0);
    }

    @Test
    void shouldThrowParameterException() {
        var mockSpec = Mockito.mock(CommandLine.Model.CommandSpec.class);
        var mockCommandLine = Mockito.mock(CommandLine.class);
        when(mockSpec.commandLine()).thenReturn(mockCommandLine);

        SubcommandCreateProject mutable = new SubcommandCreateProject();
        mutable.commandSpec = mockSpec;
        mutable.groupId = "acme.sample";
        mutable.framework = SupportedFramework.WEBFLUX;
        mutable.packageName = "123.acme.example.service";
        mutable.basePath = "/example/api/v1";
        mutable.applicationName = "rocket-sled";

        assertThrows(Exception.class, () -> mutable.call());
    }

    @Test
    void shouldReturnSuccess() {
        var mockSpec = Mockito.mock(CommandLine.Model.CommandSpec.class);
        var mockCommandLine = Mockito.mock(CommandLine.class);
        when(mockSpec.commandLine()).thenReturn(mockCommandLine);

        var mockDryRunOption = Mockito.mock(DryRunOption.class);
        when(mockDryRunOption.isDryRun()).thenReturn(true);

        SubcommandCreateProject mutable = new SubcommandCreateProject();
        // use a mock DryRunOption to force dryRun = true so that no files are generated
        mutable.dryRunOption = mockDryRunOption;
        // Since PicoCLI annotations don't come into play for this test, mock the commandSpec
        mutable.commandSpec = mockSpec;

        // arbitrary test values for a well-formed command line
        mutable.groupId = "acme.sample";
        mutable.framework = SupportedFramework.WEBFLUX;
        mutable.packageName = "acme.example.service";
        mutable.basePath = "/example/api/v1";
        mutable.applicationName = "rocket-sled";
        mutable.dbmsSchema = "acme";

        // invoke the code generation pipeline
        int returnCode = mutable.call();
        assertThat(returnCode).isEqualTo(0);
    }
}
