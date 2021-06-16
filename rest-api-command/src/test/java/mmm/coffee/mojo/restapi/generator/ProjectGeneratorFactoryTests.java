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
package mmm.coffee.mojo.restapi.generator;

import mmm.coffee.mojo.api.Generator;
import mmm.coffee.mojo.restapi.generator.spring.SpringWebFluxProjectGenerator;
import mmm.coffee.mojo.restapi.generator.spring.SpringWebMvcProjectGenerator;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests. 
 */
class ProjectGeneratorFactoryTests {
    @Test
    void shouldProvideSpringWebFluxGenerator() {
        Generator generator = ProjectGeneratorFactory.createProjectGenerator(SupportedFramework.WEBFLUX);
        assertThat(generator).isNotNull();
        assertThat(generator).isInstanceOf(SpringWebFluxProjectGenerator.class);
    }

    @Test
    void shouldProvideSpringWebMvcGenerator() {
        Generator generator = ProjectGeneratorFactory.createProjectGenerator(SupportedFramework.WEBMVC);
        assertThat(generator).isNotNull();
        assertThat(generator).isInstanceOf(SpringWebMvcProjectGenerator.class);
    }
}
