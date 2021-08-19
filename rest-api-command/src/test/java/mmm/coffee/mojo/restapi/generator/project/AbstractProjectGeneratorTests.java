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
package mmm.coffee.mojo.restapi.generator.project;

import mmm.coffee.mojo.restapi.shared.SupportedFeatures;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * unit tests
 */
class AbstractProjectGeneratorTests {

    FakeProjectGenerator generatorUnderTest;

    @BeforeEach
    public void setUp() {
        generatorUnderTest = new FakeProjectGenerator();
    }

    @Test
    void shouldNotAcceptNullArgument() {
        assertThrows(NullPointerException.class, () -> generatorUnderTest.setOutputStrategy(null));
        assertThrows(NullPointerException.class, () -> generatorUnderTest.setUpLexicalScope(null));
        assertThrows(NullPointerException.class, () -> generatorUnderTest.setUpLexicalScope(null, null));
    }

    @Test
    void whenWellFormedCommandLine_expectWellFormedLexicalScope() {

        // our hypothetical command line options:
        //  "--framework webmvc
        //  --name anvils
        //  --base-path /anvils/api/v2
        //  --package acme.tools.anvils
        //  --support postgres liquibase testcontainers
        Map<String,Object> cmdLineOptions = new HashMap<>();

        SupportedFeatures[] features = new SupportedFeatures[3];
        features[0] = SupportedFeatures.LIQUIBASE;
        features[1] = SupportedFeatures.POSTGRES;
        features[2] = SupportedFeatures.TESTCONTAINERS;

        cmdLineOptions.put(ProjectKeys.FRAMEWORK, SupportedFramework.WEBMVC);
        cmdLineOptions.put(ProjectKeys.APPLICATION_NAME, "anvils");
        cmdLineOptions.put(ProjectKeys.BASE_PATH, "/anvils/api/v2");
        cmdLineOptions.put(ProjectKeys.BASE_PACKAGE, "acme.tools.anvils");
        cmdLineOptions.put("features", features);

        // when
        generatorUnderTest.setUpLexicalScope(cmdLineOptions);

        // then expect lexicalScope to be set up
        var lexicalScope = generatorUnderTest.getLexicalScope();

        assertThat(lexicalScope.containsKey(ProjectKeys.APPLICATION_NAME)).isTrue();
        assertThat(lexicalScope.containsKey(ProjectKeys.BASE_PATH)).isTrue();
        assertThat(lexicalScope.containsKey(ProjectKeys.BASE_PACKAGE)).isTrue();
        assertThat(lexicalScope.containsKey(ProjectKeys.BASE_PACKAGE_PATH)).isTrue();
        assertThat(lexicalScope.containsKey(ProjectKeys.FRAMEWORK)).isTrue();
    }

    // ----------------------------------------------------------------
    // Helper methods
    // ----------------------------------------------------------------

    class FakeProjectGenerator extends AbstractProjectGenerator {

        /**
         * Loads the templates to be rendered
         */
        @Override
        public void loadTemplates() {

        }
    }
}
