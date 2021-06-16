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

import mmm.coffee.mojo.restapi.generator.ProjectKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.lang.reflect.Field;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * Integration tests of the rest-api command and, specifically, of the webflux framework option.
 */
class RestApiWebFluxIntegrationTests {
    MojoApplication application = new MojoApplication();
    CommandLine cli = new CommandLine(application);

    @BeforeEach
    public void setUpEachTime() {
        cli.clearExecutionResults();
    }

    @Nested
    class CreateProjectTests {
        @Test
        @DisplayName("Generate a webflux project withextra features")
        void shouldCreateProjectWithMinimumFeatures() {
            int rc = cli.execute(toArgV("rest-api create-project --dry-run --package oops.delete_me.ima_mistake --framework webflux --name beta --base-path /beta"));
            assertThat(rc).isEqualTo(0);
        }
    }

    @Nested
    class CreateEndpointTests {
        @Test
        void shouldCreateWebFluxEndpoint() throws Exception {
            // Define properties usually found in mojo.properties needed by the generator
            updateEnv(ProjectKeys.BASE_PACKAGE, "org.example.webflux");
            updateEnv(ProjectKeys.BASE_PATH, "/flux");
            updateEnv(ProjectKeys.APPLICATION_NAME, "flux");

            int rc = cli.execute(toArgV("rest-api create-endpoint --dry-run --route /employee --resource Employee"));
            assertThat(rc).isEqualTo(0);
        }
    }

    /**
     * The cli.execute() command expects a varargs {@code String...} value,
     * so its our responsibility to separate the command-line args
     * into the appropriate String array.
     */
    private String[] toArgV(String s) {
        return s.split("\\s");
    }

    @SuppressWarnings({ "unchecked" })
    public static void updateEnv(String name, String val) throws ReflectiveOperationException {
        Map<String, String> env = System.getenv();
        Field field = env.getClass().getDeclaredField("m");
        field.setAccessible(true);
        ((Map<String, String>) field.get(env)).put(name, val);
    }

}
