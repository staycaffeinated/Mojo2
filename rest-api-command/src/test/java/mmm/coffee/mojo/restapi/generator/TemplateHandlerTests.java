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

import freemarker.template.Configuration;
import mmm.coffee.mojo.catalog.CatalogEntry;
import mmm.coffee.mojo.restapi.generator.endpoint.EndpointKeys;
import mmm.coffee.mojo.restapi.generator.project.ProjectKeys;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests for CodeTemplate class
 */
class TemplateHandlerTests {
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog().muteForSuccessfulTests();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog().muteForSuccessfulTests();
    
    Map<String,Object> properties;

    private static final String BASE_PACKAGE = "mmm.coffee.example";
    private static final String BASE_PACKAGE_PATH = "mmm/coffee/example";
    private static final String ENTITY_NAME = "Widget";
    private static final String DOMAIN_CLASS = "Widget";
    private static final String EJB_CLASS = "WidgetEJB";
    private static final String ENTITY_VAR_NAME = "widget";
    private static final String BASE_PATH = "/widget";
    private static final String LOWER_CASE_ENTITY_NAME = ENTITY_NAME.toLowerCase();
    private static final String PACKAGE_NAME = BASE_PACKAGE + ".endpoint.widget";
    
    private static final Configuration CONFIGURATION = ConfigurationFactory.defaultConfiguration();

    @BeforeEach
    public void setUpEachTime() {
        properties = new HashMap<>();
        
        properties.put(EndpointKeys.ENTITY_NAME, ENTITY_NAME);
        properties.put(EndpointKeys.ENTITY_VAR_NAME, ENTITY_VAR_NAME);
        properties.put(EndpointKeys.ENTITY_POJO, DOMAIN_CLASS);
        properties.put(EndpointKeys.ENTITY_EJB, EJB_CLASS);
        properties.put(EndpointKeys.ENTITY_LOWER_CASE_NAME, LOWER_CASE_ENTITY_NAME);
        properties.put(ProjectKeys.BASE_PACKAGE, BASE_PACKAGE);
        properties.put(ProjectKeys.BASE_PACKAGE_PATH, BASE_PACKAGE_PATH);
        properties.put(EndpointKeys.BASE_PATH, BASE_PATH);
        properties.put(EndpointKeys.PACKAGE_NAME, PACKAGE_NAME);
    }
    
    @Nested
    class LoadMethodTests {
        /**
         * If the ST library has any problems loading or reading the template,
         * the library throws an NPE. This includes badly-formatted templates.
         */
        @Test
        void shouldLoadTemplate() {
            TemplateHandler template = TemplateHandler.builder()
                    .catalogEntry(createFakeControllerEntry())
                    .properties(properties)
                    .configuration(CONFIGURATION)
                    .build();
            String content = template.render();
            // System.out.println(content);
            assertThat(content).isNotNull();
        }
    }
    
    @Nested
    class ParseMethodTests {
        /**
         * If the ST library has any problems loading or reading the template,
         * the library throws an NPE. This includes badly-formatted templates.
         */
        @Test
        void shouldParseTemplate() {
            TemplateHandler template = TemplateHandler.builder()
                    .catalogEntry(createFakeControllerEntry())
                    .properties(properties)
                    .configuration(CONFIGURATION)
                    .build();
            String content = template.render();
            assertThat(content).isNotNull();
            assertThat(content.indexOf("package mmm.coffee.example")).isGreaterThan(0);
        }
    }
    
    /**
     * Returns a mimic of an entry from the webmvc-catalog.yaml.
     * @return a catalog entry
     */
    private CatalogEntry createFakeControllerEntry() {
        CatalogEntry entry = new CatalogEntry();
        entry.setTemplate("/spring/webmvc/main/endpoint/RestController.ftl");
        entry.setDestination("src/main/java/{{basePackagePath}}/endpoint/{{entityName}}Controller.java");
        entry.setContext("endpoint");
        return entry;
    }
}
