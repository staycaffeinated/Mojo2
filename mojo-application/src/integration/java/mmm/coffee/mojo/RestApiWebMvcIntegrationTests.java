package mmm.coffee.mojo;/*
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

import mmm.coffee.mojo.restapi.generator.ProjectKeys;
import mmm.coffee.mojo.restapi.generator.helpers.MojoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.lang.reflect.Field;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * The codeCoverageReport task fails if a sub-module does not contain
 * at least one integration test class. This class provides an empty
 * test to ensure codeCoverageReport can find its requisite integration test class.
 */
public class RestApiWebMvcIntegrationTests {

    MojoApplication application = new MojoApplication();
    CommandLine cli = new CommandLine(application);

    @BeforeEach
    public void setUpEachTime() throws Exception {
        cli.clearExecutionResults();

        // For integration testing, we don't have a mojo.properties file, so we'll
        // push properties into env variables. 
        updateEnv(ProjectKeys.BASE_PACKAGE, "org.example.widget");
        updateEnv(ProjectKeys.BASE_PATH, "/widgets/api/v2");
        updateEnv(ProjectKeys.BASE_PACKAGE_PATH, MojoUtils.convertPackageNameToPath("org.example.widget"));
    }

    @Test
    // @DisplayName("should generate a project without extra features")
    void shouldCreateProjectWithMinimumFeatures() {
        int rc = cli.execute(toArgV("rest-api create-project --dry-run --package oops.delete_me.ima_mistake --framework webmvc"));
        assertThat(rc).isEqualTo(0);
    }

    @Test
    // @DisplayName("should generate a project without extra features")
    void shouldCreateProjectWithPostgresql() {
        int rc = cli.execute(toArgV("rest-api create-project --dry-run -p=oops.delete_me.ima_mistake --framework=webmvc"));
        assertThat(rc).isEqualTo(0);
    }

    @Test
    // @DisplayName("should generate a project with postgres, liquibase, and test container support")
    void shouldCreateProjectWithPostgresqlLiquibaseTestContainers() {
        int rc = cli.execute(toArgV("rest-api create-project --dry-run -p=oops.delete_me.ima_mistake --framework=webmvc --support postgres liquibase testcontainers"));
        assertThat(rc).isEqualTo(0);
    }

    @Test
    // @DisplayName("should generate an endpoint with postgres, liquibase, and test container support")
    void shouldCreateEndpoint() {
        int rc = cli.execute(toArgV("rest-api create-endpoint --dry-run -e DeleteMe --route=/delete-me"));
        assertThat(rc).isEqualTo(0);
    }

    @Test
    void shouldReturnNonZeroIfPackageNameIsInvalid() {
        int rc = cli.execute(toArgV("rest-api create-project --dry-run --framework=webmvc --package=ABC.def.123foo"));
        assertThat(rc).isNotEqualTo(0);
    }

    /**
     * The cli.execute() command expects a varargs {@code String...} value,
     * so its our responsibility to separate the command-line args
     * into the appropriate String array.
     */
    private String[] toArgV(String s) {
        return s.split("\\s");
    }

    public static void updateEnv(String name, String val) throws ReflectiveOperationException {
        Map<String, String> env = System.getenv();
        Field field = env.getClass().getDeclaredField("m");
        field.setAccessible(true);
        ((Map<String, String>) field.get(env)).put(name, val);
    }
}
