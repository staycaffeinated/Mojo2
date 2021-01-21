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
import mmm.coffee.mojo.catalog.TemplateCatalog;
import mmm.coffee.mojo.restapi.shared.SupportedFeatures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;

/**
 * Integration tests of the CodeTemplate.
 *
 * <h2>Some hints on how to debug issues</h2>
 *
 * The ST library will print an error message if an illegal character or syntax
 * confused it.  The line number printed in the message is usually off by 1 or 2;
 * take the line in the error message, add 1 or 2, and check _that_ line number
 * in the ST file.
 *
 * If a NullPointerException is thrown without any syntax warnings, the top suspects are:
 * 1) the path to the template is wrong (e.g., the filename is wrong)
 * 2) the first line of the ST file does not exactly match the ST file's filename.
 *    For example, if the file is 'Application.st', the first line in that ST file
 *    must be: <code>Application() ::=</code> or <code>Application(args) ::=</code>.
 *    Any mismatch (e.g., spelling error) prevents the ST library from loading the
 *    template.
 *
 */
class CodeTemplateIT {

    private static final String BASE_PACKAGE = "mmm.coffee.example";
    private static final String BASE_PACKAGE_PATH = "mmm/coffee/example";

    private TemplateCatalog catalog;

    private Map<String,Object> projectProperties = new HashMap<>();
    private Map<String,Object> endpointProperties = new HashMap<>();

    private Configuration configuration= ConfigurationFactory.defaultConfiguration();

    @BeforeEach
    public void setUpEachTime() {
        catalog = new TemplateCatalog(TemplateCatalog.CATALOG_NAME);

        projectProperties.clear();
        endpointProperties.clear();

        projectProperties.put("basePackage", BASE_PACKAGE);
        projectProperties.put("basePackagePath", BASE_PACKAGE_PATH);
        projectProperties.put("springBootVersion", "2.2.4.RELEASE");
        projectProperties.put("springDependencyManagementVersion", "1.0.4");
        projectProperties.put("springCloudVersion", "2.2.4.RELEASE");
        projectProperties.put("problemSpringWebVersion", "1.2.3");
        projectProperties.put("applicationName", "example");
        projectProperties.put("javaVersion", "11");
        projectProperties.put(ProjectKeys.SCHEMA, "widgets");

        endpointProperties.putAll(projectProperties);
        endpointProperties.put("entityName", "Widget");
        endpointProperties.put("entityVarName", "widget");
        endpointProperties.put("entityLowerCaseName", "Widget".toLowerCase());
        endpointProperties.put("packageName", BASE_PACKAGE + ".endpoint.widget");
        endpointProperties.put("packagePath", BASE_PACKAGE_PATH + "/endpoint/widget");
        endpointProperties.put("basePath", "/widget");
        endpointProperties.put("tableName", "Widget");
    }

    @Nested
    class TestProjectTemplates {
        @Test
        void shouldParseTemplatesSuccessfully() {
            List<CatalogEntry> templates = catalog.filterByContext("project");
            for (CatalogEntry entry : templates) {
                // System.out.printf("DEBUG: Loading template %s%n", entry.getTemplate());
                TemplateHandler template = TemplateHandler.builder()
                        .catalogEntry(entry)
                        .properties(projectProperties)
                        .configuration(configuration)
                        .build();

                String content = template.render();
                assertThat(content).isNotNull();
            }
        }
    }

    /**
     * These tests verify SupportedFeatures that apply to project-scope templates
     * work as expected.
     */
    @Nested
    class TestBuildDotGradleTemplate {
        @Test
        void shouldAddLiquibaseDependencyWhenLiquibaseFeatureIsSpecified() {
            Optional<CatalogEntry> optional = getBuildDotGradleTemplate();
            assertThat(optional.isPresent());

            // FreeMarker doesn't evaluate the value of the property;
            // it only evaluates whether the property exists.
            projectProperties.put(SupportedFeatures.LIQUIBASE.toString(), "true");

            // Set up the freemarker template
            TemplateHandler template = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(configuration)
                    .build();

            String content = template.render();
            assertThat(content).isNotNull();
            // verify the liquibase library was included in the build.gradle
            // we won't assert what the specific syntax of the line is.
            // if the output has something that looks like an implementation/runtime
            // dependency on liquibase, its sufficient for a unit test.
            // e2e tests will verify if the build.gradle is truly correct
            assertThat(content).contains("libs.liquibaseCore");
        }

        @Test
        void shouldAddPostgresDependencyWhenPostgresFeatureIsSpecified() {
            Optional<CatalogEntry> optional = getBuildDotGradleTemplate();
            assertThat(optional.isPresent());

            // FreeMarker doesn't evaluate the value of the property;
            // it only evaluates whether the property exists.
            projectProperties.put(SupportedFeatures.POSTGRES.toString(), "true");

            // Set up the freemarker template
            TemplateHandler template = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(configuration)
                    .build();

            String content = template.render();
            assertThat(content).isNotNull();
            // verify the postgresql library was included in the build.gradle
            // we won't assert what the specific syntax of the line is.
            // if the output has something that looks like an implementation/runtime
            // dependency on liquibase, its sufficient for a unit test.
            // e2e tests will verify if the build.gradle is truly correct
            assertThat(content).contains("libs.postgresql");
        }
    }

    @Nested
    class TestEndpointTemplates {
        @Test
        void shouldParseTemplatesSuccessfully() {
            List<CatalogEntry> templates = catalog.filterByContext("endpoint");
            for (CatalogEntry entry : templates) {
                // System.out.printf("DEBUG: Loading template %s%n", entry.getTemplate());
                TemplateHandler template = TemplateHandler.builder()
                        .catalogEntry(entry)
                        .properties(endpointProperties)
                        .configuration(configuration)
                        .build();

                String content = template.render();
                assertThat(content).isNotNull();
            }
        }
    }

    /**
     * Fetches the template that produces the build.gradle file
     */
    private Optional<CatalogEntry> getBuildDotGradleTemplate() {
        List<CatalogEntry> entries = catalog.filterByNameLike("BuildDotGradle");
        return entries.stream().findFirst();
    }
}
