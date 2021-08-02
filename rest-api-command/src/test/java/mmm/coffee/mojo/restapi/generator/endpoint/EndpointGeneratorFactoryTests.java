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

import mmm.coffee.mojo.restapi.generator.endpoint.spring.SpringWebFluxEndpointGenerator;
import mmm.coffee.mojo.restapi.generator.endpoint.spring.SpringWebMvcEndpointGenerator;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests
 */
class EndpointGeneratorFactoryTests {

    @Test
    void shouldReturnWebMvcGenerator() {
        var generator = EndpointGeneratorFactory.createGenerator(SupportedFramework.WEBMVC);
        assertThat(generator).isInstanceOf(SpringWebMvcEndpointGenerator.class);
    }

    @Test
    void shouldReturnWebFluxGenerator() {
        var generator = EndpointGeneratorFactory.createGenerator(SupportedFramework.WEBFLUX);
        assertThat(generator).isInstanceOf(SpringWebFluxEndpointGenerator.class);
    }

    @Test
    void shouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> EndpointGeneratorFactory.createGenerator(SupportedFramework.UNDEFINED));
    }
}
