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

import mmm.coffee.mojo.restapi.generator.helpers.MojoUtils;
import mmm.coffee.mojo.restapi.generator.helpers.NamingRules;
import mmm.coffee.mojo.restapi.generator.project.ProjectKeys;
import mmm.coffee.mojo.restapi.shared.Environment;
import mmm.coffee.mojo.restapi.shared.MojoProperties;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import org.apache.commons.configuration2.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;


/**
 * Unit tests
 */
class EndpointLexicalScopeFactoryTests {

    EndpointLexicalScopeFactory factoryUnderTest;
    Configuration configuration;

    final String basePackage = "org.example.widgets";
    final String basePath = "/widgets/api/v1";

    @BeforeEach
    void setUpEachTime() throws Exception {
        factoryUnderTest = new EndpointLexicalScopeFactory();
        configuration = buildFakeMojoProperties(basePackage, basePath);
        factoryUnderTest.setMojoProps(configuration);
    }

    @Test
    void shouldSetAllEndpointKeys() {
        // given
        final String resource = "Wine";
        final String route = "/wine";
        Map<String,Object> cmdLineOptions = toCommandLineOptions("--resource " + resource + " --route " + route);

        factoryUnderTest.setCommandLineOptions(cmdLineOptions);

        Map<String,Object> lexicalContext = factoryUnderTest.createLexicalScope();

        final String expectedEjbName = NamingRules.toEjbClassName(resource);
        final String expectedPojoName = NamingRules.toPojoClassName(resource);
        final String expectedLowerCaseName = resource.toLowerCase();
        final String expectedVarName = NamingRules.toEntityVariableName(resource);
        final String expectedEndpointPackageName = NamingRules.toEndpointPackageName(basePackage, resource);
        final String expectedPath = MojoUtils.convertPackageNameToPath(expectedEndpointPackageName);
        final String expectedTableName = NamingRules.toTableName(resource);

        assertThat(lexicalContext.get(EndpointKeys.BASE_PATH)).isEqualTo(route);
        assertThat(lexicalContext.get(EndpointKeys.ENTITY_EJB)).isEqualTo(expectedEjbName);
        assertThat(lexicalContext.get(EndpointKeys.ENTITY_POJO)).isEqualTo(expectedPojoName);
        assertThat(lexicalContext.get(EndpointKeys.ENTITY_LOWER_CASE_NAME)).isEqualTo(expectedLowerCaseName);
        assertThat(lexicalContext.get(EndpointKeys.ENTITY_VAR_NAME)).isEqualTo(expectedVarName);
        assertThat(lexicalContext.get(EndpointKeys.PACKAGE_NAME)).isEqualTo(expectedEndpointPackageName);
        assertThat(lexicalContext.get(EndpointKeys.PACKAGE_PATH)).isEqualTo(expectedPath);
        assertThat(lexicalContext.get(EndpointKeys.TABLE_NAME)).isEqualTo(expectedTableName);
    }

    // ----------------------------------------------------------------------------------------------
    // Helper methods
    // ----------------------------------------------------------------------------------------------

    /**
     * Returns a Configuration equivalent to what is found in a mojo.properties file
     */
    private Configuration buildFakeMojoProperties(String basePackage, String basePath) throws Exception {
        Environment.addVariable(ProjectKeys.BASE_PACKAGE, basePackage);
        Environment.addVariable(ProjectKeys.BASE_PATH, basePath);
        Environment.addVariable(ProjectKeys.FRAMEWORK, SupportedFramework.WEBFLUX.toString());

        return new MojoProperties().getConfiguration();
    }

    /**
     * Converts the command line string into a Map equivalent to the one produced by SubcommandCreateEndpoint
     * and passed to the concrete EndpointGenerator.
     *
     * @param commandLine a string containing values equivalent to what a user enters on the command line
     * @return a Map representation of those command line arguments
     */
    private Map<String,Object> toCommandLineOptions(String commandLine) {
        String[] inputs = toArgV(commandLine);

        Map<String,Object> map = new HashMap<>();

        for (int i = 0; i < inputs.length; i++) {
            if (inputs[i].equals("--resource")) {
                map.put("resource", inputs[i+1]);
                i++;
            }
            if (inputs[i].equals("--route")) {
                map.put("route", inputs[i+1]);
                i++;
            }
        }
        return map;
    }

    private String[] toArgV(String s) {
        return s.split("\\s");
    }
}
