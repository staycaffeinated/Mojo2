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
import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.checkerframework.checker.nullness.Opt;
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
        map.put("dryRun", true);
        generatorUnderTest.run(map);
        assertThat(true).isTrue();
    }
    
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

}
