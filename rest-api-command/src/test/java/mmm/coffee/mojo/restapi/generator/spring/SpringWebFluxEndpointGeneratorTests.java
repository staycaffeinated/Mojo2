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
import mmm.coffee.mojo.api.NoOpTemplateWriter;
import mmm.coffee.mojo.mixin.DryRunOption;
import mmm.coffee.mojo.restapi.generator.endpoint.EndpointKeys;
import mmm.coffee.mojo.restapi.generator.endpoint.spring.SpringWebFluxEndpointGenerator;
import mmm.coffee.mojo.restapi.generator.project.ProjectKeys;
import mmm.coffee.mojo.restapi.shared.MojoProperties;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import org.apache.commons.configuration2.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests
 */
class SpringWebFluxEndpointGeneratorTests {

    Generator generatorUnderTest;

    @BeforeEach
    public void setUpEachTime() {
        generatorUnderTest = new SpringWebFluxEndpointGenerator();
        generatorUnderTest.initialize();
    }

    @Test
    void shouldLoadWebFluxEndpointTemplates() {
        // when
        generatorUnderTest.loadTemplates();

        // then
        // getCatalog returns an Optional, never null
        assertThat(generatorUnderTest.getCatalog()).isNotNull();
        // the template catalog was found
        assertThat(generatorUnderTest.getCatalog().isPresent()).isTrue();
        // the catalog is not empty; at least one template was found
        assertThat(generatorUnderTest.getCatalog().isEmpty()).isFalse();
    }

    /**
     * For endpoint generation, the lexical scope built up
     * from command line arguments and properties defined
     * at project-create time (In the real world, these latter
     * properties come from the mojo.properties file).
     *
     * The Generator's {@code setUpLexicalScope} combines the
     * configuration and command line to flesh out all the
     * properties that are needed by templates.
     */
    @Test
    void shouldProduceWellFormedLexicalScope() throws Exception {
        // set up
        var lexicalScope = buildSampleLexicalScope();
        var configuration = buildSampleConfiguration();

        // when
        generatorUnderTest.setUpLexicalScope(lexicalScope, configuration);
        
        System.out.printf(String.format("===> Framework: %s%n", configuration.getString(ProjectKeys.FRAMEWORK)));

        // We test for the presence of properties set here, plus properties
        // auto-determined by our explict values
        var actualMap = generatorUnderTest.getLexicalScope();
        assertThat(actualMap).isNotEmpty();
        assertThat(actualMap.containsKey(ProjectKeys.FRAMEWORK)).isTrue();
        assertThat(actualMap.containsKey(ProjectKeys.BASE_PACKAGE)).isTrue();
        assertThat(actualMap.containsKey(ProjectKeys.BASE_PACKAGE_PATH)).isTrue();
        assertThat(actualMap.containsKey(ProjectKeys.APPLICATION_NAME)).isTrue();
        assertThat(actualMap.containsKey(ProjectKeys.GROUP_ID)).isTrue();

        assertThat(actualMap.containsKey(EndpointKeys.BASE_PATH)).isTrue();
        assertThat(actualMap.containsKey(EndpointKeys.ENTITY_NAME)).isTrue();
        assertThat(actualMap.containsKey(EndpointKeys.ENTITY_LOWER_CASE_NAME)).isTrue();
        assertThat(actualMap.containsKey(EndpointKeys.PACKAGE_NAME)).isTrue();
        assertThat(actualMap.containsKey(EndpointKeys.PACKAGE_PATH)).isTrue();
    }

    @Test
    void shouldRunThroughWorkflow() throws Exception {
        var templateWriter = new NoOpTemplateWriter();
        var commandLine = buildSampleCommandLineInputs();
        var mojoPropsContent = buildSampleConfiguration();

        int returnCode = generatorUnderTest.run(commandLine, mojoPropsContent);
        assertThat(returnCode).isEqualTo(0);
    }

    private Map<String,Object> buildSampleLexicalScope() {
        Map<String,Object> lexicalScope = new HashMap<>();

        // In real world, these come from the mojo.properties file
        lexicalScope.put(ProjectKeys.FRAMEWORK, SupportedFramework.WEBFLUX );
        lexicalScope.put(ProjectKeys.BASE_PATH, "/home");
        lexicalScope.put(ProjectKeys.APPLICATION_NAME, "flux-service");
        lexicalScope.put(ProjectKeys.BASE_PACKAGE, "org.example.reactive.flux");
        lexicalScope.put(ProjectKeys.GROUP_ID, "org.example.reactive");

        // In the real world, these are command line args
        lexicalScope.put(EndpointKeys.CMDLINE_ROUTE_ARG, "/widget");
        lexicalScope.put(EndpointKeys.CMDLINE_RESOURCE_ARG, "Widget");
        return lexicalScope;
    }

    private Map<String,Object> buildSampleCommandLineInputs() {
        Map<String,Object> commandLineInputs = new HashMap<>();
        commandLineInputs.put(EndpointKeys.CMDLINE_RESOURCE_ARG, "TestMetric");
        commandLineInputs.put(EndpointKeys.CMDLINE_ROUTE_ARG, "/metric");
        commandLineInputs.put(DryRunOption.DRY_RUN_KEY, Boolean.TRUE);
        return commandLineInputs;
    }

    private Configuration buildSampleConfiguration() throws Exception {
        // Since we don't have a real mojo.properties when running unit tests,
        // we can set the properties usually acquired from that properties file
        // as env variables to acquire a well-formed configuration
        updateEnv(ProjectKeys.BASE_PACKAGE, "org.example.reactive.flux");
        updateEnv(ProjectKeys.BASE_PATH, "/reactive/api/v1");
        updateEnv(ProjectKeys.FRAMEWORK, SupportedFramework.WEBFLUX.toString());

        return new MojoProperties().getConfiguration();
    }

    @SuppressWarnings({ "unchecked" })
    public static void updateEnv(String name, String val) throws ReflectiveOperationException {
        Map<String, String> env = System.getenv();
        Field field = env.getClass().getDeclaredField("m");
        field.setAccessible(true);
        ((Map<String, String>) field.get(env)).put(name, val);
    }
}
