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

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

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
        assertThrows(NullPointerException.class, () -> generatorUnderTest.run(null, new NoOpTemplateWriter()));
    }

    @Test
    void shouldDisallowNullWriterArgument() {
        assertThrows(NullPointerException.class, () ->  generatorUnderTest.run(new HashMap<>(), null));
    }

    @Test
    void shouldHandleLiveRun() {
        Map<String,Object> map = new HashMap<>();
        generatorUnderTest.run(map);
        assertThat(true).isTrue();
    }
    
    class FakeGenerator implements Generator {
        @Override public void initialize() {}
        @Override public void configure(Map<String,Object> properties) {}
        @Override public void outputStrategy(TemplateWriter sourceSink) {}
        @Override public void generate() {}
    }

}
