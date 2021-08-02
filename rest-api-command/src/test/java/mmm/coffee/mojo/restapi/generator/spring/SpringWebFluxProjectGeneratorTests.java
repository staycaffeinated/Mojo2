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
package mmm.coffee.mojo.restapi.generator.spring;

import mmm.coffee.mojo.api.Generator;
import mmm.coffee.mojo.restapi.generator.project.ProjectKeys;
import mmm.coffee.mojo.restapi.generator.project.spring.SpringWebFluxProjectGenerator;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests
 */
class SpringWebFluxProjectGeneratorTests {

    @Test
    void shouldLoadWebFluxTemplates() {
        Generator generator = new SpringWebFluxProjectGenerator();
        generator.loadTemplates();
        // getCatalog returns an Optional, never null
        assertThat(generator.getCatalog()).isNotNull();
        // the template catalog was found
        assertThat(generator.getCatalog().isPresent()).isTrue();
        // the catalog is not empty; at least one template was found
        assertThat(generator.getCatalog().isEmpty()).isFalse();
    }

    @Test
    void shouldContainLexicalScope() {
        Generator generator = new SpringWebFluxProjectGenerator();

        // LexicalScope
        Map<String,Object> lexicalScope = new HashMap<>();
        lexicalScope.put(ProjectKeys.FRAMEWORK, SupportedFramework.WEBFLUX );
        lexicalScope.put(ProjectKeys.BASE_PATH, "/home");
        lexicalScope.put(ProjectKeys.APPLICATION_NAME, "flux-service");
        lexicalScope.put(ProjectKeys.BASE_PACKAGE, "org.example.reactive.flux");
        lexicalScope.put(ProjectKeys.GROUP_ID, "org.example.reactive");

        generator.setUpLexicalScope( lexicalScope );

        // We test for the presence of properties set here, plus properties
        // auto-determined by our explict values
        var actualMap = generator.getLexicalScope();
        assertThat(actualMap).isNotEmpty();
        assertThat(actualMap.containsKey(ProjectKeys.FRAMEWORK)).isTrue();
        assertThat(actualMap.containsKey(ProjectKeys.BASE_PATH)).isTrue();
        assertThat(actualMap.containsKey(ProjectKeys.BASE_PACKAGE)).isTrue();
        assertThat(actualMap.containsKey(ProjectKeys.BASE_PACKAGE_PATH)).isTrue();
        assertThat(actualMap.containsKey(ProjectKeys.APPLICATION_NAME)).isTrue();
        assertThat(actualMap.containsKey(ProjectKeys.GROUP_ID)).isTrue();
    }
}
