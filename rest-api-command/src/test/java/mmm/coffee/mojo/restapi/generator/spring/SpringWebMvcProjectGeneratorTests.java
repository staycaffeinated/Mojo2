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
import mmm.coffee.mojo.restapi.generator.ProjectKeys;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests
 */
class SpringWebMvcProjectGeneratorTests {

    @Test
    void shouldLoadTemplates() {
        Generator generator = new SpringWebMvcProjectGenerator();
        generator.loadTemplates();
        // List<?> templates = generator.getTemplates();
        // assert templates isNotEmpty
        // assert templates.contains ( GradleTemplates )
        // assert templates.contains ( CommonTemplates )
        // assert templates.contains ( SpringBootTemplates )
        // assert templates.contains ( SpringWebMvcTemplates )
    }

    @Test
    void shouldContainLexicalScope() {
        Generator generator = new SpringWebMvcProjectGenerator();

        // LexicalScope
        Map<String,Object> lexicalScope = new HashMap<>();
        lexicalScope.put(ProjectKeys.FRAMEWORK, SupportedFramework.WEBMVC );
        lexicalScope.put(ProjectKeys.BASE_PATH, "/home");
        lexicalScope.put(ProjectKeys.APPLICATION_NAME, "hello-service");
        lexicalScope.put(ProjectKeys.BASE_PACKAGE, "org.example.service.hello");
        lexicalScope.put(ProjectKeys.GROUP_ID, "org.example.service");

        generator.setUpLexicalScope( lexicalScope );

        // We test for the presence of properties set here, plus properties
        // auto-determined by our explict values
        var actualMap = generator.getLexicalScope();
        assertThat(actualMap).isNotEmpty();
        assertThat(actualMap.containsKey(ProjectKeys.FRAMEWORK));
        assertThat(actualMap.containsKey(ProjectKeys.BASE_PATH));
        assertThat(actualMap.containsKey(ProjectKeys.BASE_PACKAGE));
        assertThat(actualMap.containsKey(ProjectKeys.BASE_PACKAGE_PATH));
        assertThat(actualMap.containsKey(ProjectKeys.APPLICATION_NAME));
        assertThat(actualMap.containsKey(ProjectKeys.GROUP_ID));

    }
}
