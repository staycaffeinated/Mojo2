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
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

/**
 * Unit tests of the RestApi command
 */
public class CommandRestApiTests {

    final CommandRestApi commandRestApi = new CommandRestApi();
    final CommandLine cli = new CommandLine(commandRestApi);

    @BeforeEach
    public void setUpEachTime() {
        cli.clearExecutionResults();
    }

    @Test
    public void testUsage() {
        cli.usage(System.out);
    }
}
