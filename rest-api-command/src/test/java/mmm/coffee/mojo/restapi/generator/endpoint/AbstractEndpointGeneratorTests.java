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
package mmm.coffee.mojo.restapi.generator.endpoint;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests
 */
class AbstractEndpointGeneratorTests {

    AbstractEndpointGenerator generatorUnderTest;

    @BeforeEach
    public void setUp() {
        generatorUnderTest = new FakeEndpointGenerator();
    }

    // @Test
    void testOne() {
        var commandLineOptions = new HashMap<String,Object>();
        commandLineOptions.put("resource", "Treasure");
        commandLineOptions.put("route", "/treasure");

        // todo: set up env vars for mojo.props keys
        
        generatorUnderTest.setUpLexicalScope(commandLineOptions);

        var lexicalScope = generatorUnderTest.getLexicalScope();

        assertThat(lexicalScope).isNotNull();

        // iterate thru required keys and confirm they're defined
    }

    @Test
    void whenArgumentIsNull_expectNullPointerException() {
        // given
        var configuration = Mockito.mock(PropertiesConfiguration.class);

        // then
        assertThrows(NullPointerException.class, () -> generatorUnderTest.setUpLexicalScope(null));
        assertThrows(NullPointerException.class, () -> generatorUnderTest.setUpLexicalScope(null, configuration));
    }


    // ------------------------------------------------------------------
    // Helper classes
    // ------------------------------------------------------------------

    class FakeEndpointGenerator extends AbstractEndpointGenerator {
        
    }
}
