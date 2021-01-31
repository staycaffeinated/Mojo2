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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import static com.google.common.truth.Truth.assertThat;

/**
 * The codeCoverageReport task fails if a sub-module does not contain
 * at least one integration test class. This class provides an empty
 * test to ensure codeCoverageReport can find its requisite integration test class.
 */
public class RestApiIntegrationTests {

    MojoApplication application = new MojoApplication();
    CommandLine cli = new CommandLine(application);

    @BeforeEach
    public void setUpEachTime() {
        cli.clearExecutionResults();
    }

    @Test
    @DisplayName("Generate a project without extra features")
    void shouldCreateProjectBasicProject() {
        int rc = cli.execute(toArgV("rest-api create-project --dry-run -p=oops.delete_me.ima_mistake"));
        assertThat(rc).isEqualTo(0);
    }

    @Test
    @DisplayName("Generate a project without extra features")
    void shouldCreateProjectWithPostgresql() {
        int rc = cli.execute(toArgV("rest-api create-project --dry-run -p=oops.delete_me.ima_mistake"));
        assertThat(rc).isEqualTo(0);
    }

    @Test
    @DisplayName("Generate a project with postgres, liquibase, and test container support")
    void shouldCreateProjectWithPostgresqlLiquibaseTestContainers() {
        int rc = cli.execute(toArgV("rest-api create-project --dry-run -p=oops.delete_me.ima_mistake --support postgres liquibase testcontainers"));
        assertThat(rc).isEqualTo(0);
    }

    @Test
    @DisplayName("Generate a project with postgres, liquibase, and test container support")
    void shouldCreateEndpoint() {
        int rc = cli.execute(toArgV("rest-api create-endpoint --dry-run -resource=DeleteMe -route=/delete-me"));
        assertThat(rc).isEqualTo(0);
    }

    @Test
    void shouldReturnNonZeroIfPackageNameIsInvalid() {
        int rc = cli.execute(toArgV("rest-api create-project --dry-run --package=ABC.def.123foo"));
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
}
