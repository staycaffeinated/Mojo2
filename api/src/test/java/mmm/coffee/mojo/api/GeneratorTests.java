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
package mmm.coffee.mojo.api;

import mmm.coffee.mojo.catalog.TemplateCatalog;
import mmm.coffee.mojo.mixin.DryRunOption;
import org.apache.commons.configuration2.*;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit test Generator class
 */
@SuppressWarnings( {"squid:S5786","java:S5778" })
class GeneratorTests {

    private Generator generatorUnderTest = new FakeGenerator();

    static final int ZERO = 0;

    @Test
    public void shouldDisallowNullArgument() {
        assertThrows(NullPointerException.class, () -> generatorUnderTest.run(null));
    }

    @Test
    public void shouldDisallowNullProperties() {
        Configuration config = new BaseConfiguration();
        assertThrows(NullPointerException.class, () -> generatorUnderTest.run(null, config));
    }

    @Test
    void shouldHandleDryRun() {
        Map<String,Object> map = new HashMap<>();
        map.put(DryRunOption.DRY_RUN_KEY, true);
        generatorUnderTest.run(map);
        // when dryRun = true, sourceSink is-a NoOpTemplateWriter, but that's not visible
        assertThat(map.get(DryRunOption.DRY_RUN_KEY)).isEqualTo(true);
    }

    /**
     * Verify the pipeline defined in the Generator::run(Map) method completes successfully
     */
    @Test
    void whenRunWithOneArgIsInvokedWithWellFormedValues_expectSuccess() {
        // given
        var map = new HashMap<String,Object>();
        map.put(DryRunOption.DRY_RUN_KEY, false);

        // when
        int returnCode = generatorUnderTest.run(map);

        // then the return code should be zero, indicating success
        assertThat(returnCode).isEqualTo(ZERO);
    }

    /**
     * Verify the pipeline defined in the Generator::run(Map,Configuration) method completes successfully
     */
    @Test
    void whenRunWithTwoArgsIsInvokedWithWellFormedValues_expectSuccess() {
        // given
        var map = new HashMap<String,Object>();
        map.put(DryRunOption.DRY_RUN_KEY, false);

        var configuration = createFakeConfiguration();

        // when
        int returnCode = generatorUnderTest.run(map, configuration);

        // then the return code should be zero, indicating success
        assertThat(returnCode).isEqualTo(ZERO);
    }
    
    // --------------------------------------------------------------------------------------------------
    // Helper class
    // --------------------------------------------------------------------------------------------------

    class FakeGenerator implements Generator {
        @Override public void loadTemplates() {}
        @Override public void setUpLexicalScope(Map<String,Object> commandLineOptions) {}

        /**
         * Configure the lexical scope of the generator. This is where properties that are consumed
         * by templates during rendering are defined.
         *
         * @param commandLineOptions the command line options and anything the CLI might need to pass to the generator
         * @param configuration      properties acquired from a configuration file, such as the {@code mojo.properties} file
         */
        @Override
        public void setUpLexicalScope(@NonNull Map<String, Object> commandLineOptions, Configuration configuration) {}

        @Override public Map<String, Object> getLexicalScope() { return new HashMap<String,Object>(); }
        @Override public void setOutputStrategy(TemplateWriter writer) {}
        @Override public void generate() {}
        @Override public Optional<TemplateCatalog> getCatalog() { return Optional.empty(); }
    }

    /**
     * Returns a Configuration object containing env vars.
     */
    Configuration createFakeConfiguration() {
        var config = new PropertiesConfiguration();
        var immutableEnv = new EnvironmentConfiguration();
        ConfigurationUtils.copy(immutableEnv, config);
        return config;
    }
}
