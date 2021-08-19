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
import mmm.coffee.mojo.api.Generator;
import mmm.coffee.mojo.catalog.CatalogEntry;
import mmm.coffee.mojo.catalog.TemplateCatalog;
import mmm.coffee.mojo.library.DependencyCatalog;
import mmm.coffee.mojo.restapi.generator.endpoint.EndpointLexicalScopeFactory;
import mmm.coffee.mojo.restapi.generator.project.ProjectKeys;
import mmm.coffee.mojo.restapi.generator.project.spring.SpringWebMvcProjectGenerator;
import mmm.coffee.mojo.restapi.shared.Environment;
import mmm.coffee.mojo.restapi.shared.MojoProperties;
import mmm.coffee.mojo.restapi.shared.SupportedFeatures;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
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
class TestWebMvcCatalog {

    private static final String BASE_PACKAGE = "mmm.coffee.example";
    private static final String BASE_PACKAGE_PATH = "mmm/coffee/example";

    private TemplateCatalog catalog;

    final private Map<String,Object> projectProperties = new HashMap<>();
    private Map<String,Object> endpointProperties;

    final private Configuration freemarkerConfiguration = ConfigurationFactory.defaultConfiguration();

    @BeforeEach
    public void setUpEachTime() throws Exception {
        catalog = loadWebMvcCatalog();

        projectProperties.clear();

        // These are the properties in the lexical scope of the project-scope templates
        projectProperties.put("basePackage", BASE_PACKAGE);
        projectProperties.put("basePackagePath", BASE_PACKAGE_PATH);
        projectProperties.put("springBootVersion", "2.2.4.RELEASE");
        projectProperties.put("springDependencyManagementVersion", "1.0.4");
        projectProperties.put("springCloudVersion", "2.2.4.RELEASE");
        projectProperties.put("problemSpringWebVersion", "1.2.3");
        projectProperties.put("applicationName", "example");
        projectProperties.put("javaVersion", "11");
        projectProperties.put(ProjectKeys.SCHEMA, "widgets");
        projectProperties.put(ProjectKeys.BASE_PATH, "/widgets");
        projectProperties.put(ProjectKeys.FRAMEWORK, SupportedFramework.WEBMVC.name());

        populateDependencyKeys();

        // -----------------------------------------------------------
        // Define the lexical scope consumed by the endpoint templates
        // -----------------------------------------------------------

        // the (hypothetical) command line options when constructing an endpoint
        final String resource = "Widget";
        final String route = "/widget";
        Map<String,Object> cmdLineOptions = toCommandLineOptions("--resource " + resource + " --route " + route);

        // The (fake) content of the mojo.properties file
        org.apache.commons.configuration2.Configuration mojoProps = buildFakeMojoProperties(BASE_PACKAGE, BASE_PACKAGE_PATH);

        // Now build the lexical scope
        EndpointLexicalScopeFactory factory = new EndpointLexicalScopeFactory();
        factory.setMojoProps(mojoProps);
        factory.setCommandLineOptions(cmdLineOptions);
        endpointProperties = factory.createLexicalScope();

        // These are the properties in the lexical scope of the endpoint-scope templates
        endpointProperties.putAll(projectProperties);
        endpointProperties.put("entityName", "Widget");
        endpointProperties.put("entityVarName", "widget");
        endpointProperties.put("entityLowerCaseName", "Widget".toLowerCase());
        endpointProperties.put("packageName", BASE_PACKAGE + ".endpoint.widget");
        endpointProperties.put("packagePath", BASE_PACKAGE_PATH + "/endpoint/widget");
        endpointProperties.put("basePath", "/widget");
        endpointProperties.put("tableName", "Widget");
    }

    /**
     * Load a TemplateCatalog suitable for testing
     */
    private TemplateCatalog loadWebMvcCatalog() {
        // Arbitrarily pick this generator to have sample templates
        Generator g = new SpringWebMvcProjectGenerator();
        g.loadTemplates();
        if (g.getCatalog().isPresent())
            return g.getCatalog().get();
        throw new NullPointerException("No catalog was returned by the SpringWebMvcProjectGenerator");
    }

    private void populateDependencyKeys() {
        DependencyCatalog catalog = new DependencyCatalog(Catalogs.THIRD_PARTY_LIBRARY_CATALOG);
        catalog.loadTemplateKeys(projectProperties);
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
                        .configuration(freemarkerConfiguration)
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
            assertThat(optional.isPresent()).isTrue();

            // FreeMarker doesn't evaluate the value of the property;
            // it only evaluates whether the property exists.
            projectProperties.put(SupportedFeatures.LIQUIBASE.toString(), "true");

            // Set up the freemarker template
            TemplateHandler template = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(freemarkerConfiguration)
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
            assertThat(optional.isPresent()).isTrue();

            // FreeMarker doesn't evaluate the value of the property;
            // it only evaluates whether the property exists.
            projectProperties.put(SupportedFeatures.POSTGRES.toString(), "true");

            // Set up the freemarker template
            TemplateHandler template = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(freemarkerConfiguration)
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
        /**
         * The scope of this test is to load every spring web-mvc template for endpoints
         * and attempt to render them.  This test helps reveal syntax errors, misspelled
         * template variable names, and incorrect template paths.  Syntax errors that
         * are later revealed by compile errors in the generated code -will not- be
         * detected here; those syntax errors are best detected by end-to-end testing.
         */
        @Test
        void shouldParseTemplatesSuccessfully() {
            List<CatalogEntry> templates = catalog.filterByContext("endpoint");
            for (CatalogEntry entry : templates) {
                // System.out.printf("DEBUG: Loading template %s%n", entry.getTemplate());
                TemplateHandler template = TemplateHandler.builder()
                        .catalogEntry(entry)
                        .properties(endpointProperties)
                        .configuration(freemarkerConfiguration)
                        .build();

                String content = template.render();
                assertThat(content).isNotNull();
            }
        }
    }

    /**
     * These tests verify behavior when the ```--support postgres```
     * option is given on the command line.
     */
    @Nested
    class TestApplicationPropertiesTemplateWithPostgresOption {
        /**
         * Verify the use case of the user entering something like:
         *
         * rest-api create-project --schema=passengers \
         *                         --name=taxi-service \
         *                         --package=org.example \
         *                         --support postgres
         *
         * Expected behavior: the postgresql driver is added
         * to application.properties, and the jdbc.url will honor the schema
         */
        @Test
        void shouldHonorSchemaOptionWhenSpecified() {
            Optional<CatalogEntry> optional = getApplicationDotPropertiesTemplate();
            assertThat(optional).isNotNull();
            assertThat(optional.isPresent()).isTrue();

            final String schema = "passengers";
            projectProperties.put(ProjectKeys.SCHEMA, schema);
            projectProperties.put(ProjectKeys.APPLICATION_NAME, "taxi-service");
            projectProperties.put(SupportedFeatures.POSTGRES.toString(), SupportedFeatures.POSTGRES.toString() );

            TemplateHandler handler = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(freemarkerConfiguration)
                    .build();

            String content = handler.render();
            assertThat(content).isNotNull();
            assertThat(content).contains("spring.datasource.driver-class-name=org.postgresql.Driver");
            assertThat(content).contains("spring.datasource.url=jdbc:postgresql://localhost:5432/"+schema);
        }

        /**
         * Verify the use case of the user entering something like:
         *
         * rest-api create-project --name=taxi-service \
         *                         --package=org.example \
         *                         --support postgres
         *
         * Expected behavior:  the postgresql driver is added
         * to application.properties, and the jdbc.url sets schema to match application name.
         */
        @Test
        void shouldUseAppNameAsSchemaNameIfSchemaIsUndefined() {
            Optional<CatalogEntry> optional = getApplicationDotPropertiesTemplate();
            assertThat(optional).isNotNull();
            assertThat(optional.isPresent()).isTrue();

            projectProperties.put(ProjectKeys.SCHEMA, "taxi-db");
            projectProperties.put(ProjectKeys.APPLICATION_NAME, "taxi-service");
            projectProperties.put(SupportedFeatures.POSTGRES.toString(), SupportedFeatures.POSTGRES.toString() );

            TemplateHandler handler = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(freemarkerConfiguration)
                    .build();

            String content = handler.render();
            assertThat(content).isNotNull();
            // Since postgres database support was selected, expect to see the postgres driver property
            assertThat(content).contains("spring.datasource.driver-class-name=org.postgresql.Driver");
            // Since a schema name was given, expect to see that schema name in the datasource URL
            assertThat(content).contains("spring.datasource.url=jdbc:postgresql://localhost:5432/taxi-db");
        }

        /**
         * Verify the use case of the user entering something like:
         *
         * rest-api create-project --package=org.example --support postgres
         *
         * Expected behavior: the postgresql driver is added to
         * application.properties, and the jdbc.url applies the default schema
         */
        @Test
        void shouldUseDefaultSchemaWhenOtherChoicesAreUndefined() {
            Optional<CatalogEntry> optional = getApplicationDotPropertiesTemplate();
            assertThat(optional).isNotNull();
            assertThat(optional.isPresent()).isTrue();

            projectProperties.remove(ProjectKeys.APPLICATION_NAME);
            projectProperties.remove(ProjectKeys.SCHEMA);
            projectProperties.put(SupportedFeatures.POSTGRES.toString(), SupportedFeatures.POSTGRES.toString() );

            TemplateHandler handler = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(freemarkerConfiguration)
                    .build();

            String content = handler.render();
            assertThat(content).isNotNull();
            assertThat(content).contains("spring.datasource.driver-class-name=org.postgresql.Driver");
            // Since no schema name was given, expect the default 'testdb' schema in the datasource URL
            assertThat(content).contains("spring.datasource.url=jdbc:postgresql://localhost:5432/testdb");
        }
        
    }

    /**
     * These tests verify behavior when no specific database was picked
     * (the generator defaults to using the H2 database).
     */
    @Nested
    class TestApplicationPropertiesTemplateWithoutPostgresOption {

        /**
         * Verify the use case of the user typing something like
         *
         *  rest-api create-project --schema=passengers --package=org.example --name=taxi-service
         *
         *  Expected behavior: when the schema name is given,
         *  the code generator uses that schema name in the JDBC URL.
         */
        @Test void shouldHonorSchemaOptionWhenSpecified() {
            Optional<CatalogEntry> optional = getApplicationDotPropertiesTemplate();
            assertThat(optional).isNotNull();
            assertThat(optional.isPresent()).isTrue();

            projectProperties.put(ProjectKeys.SCHEMA, "passengers");
            projectProperties.put(ProjectKeys.APPLICATION_NAME, "taxi-serivce");
            projectProperties.remove("features");

            TemplateHandler handler = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(freemarkerConfiguration)
                    .build();

            String content = handler.render();
            assertThat(content).isNotNull();
            assertThat(content).contains("spring.datasource.driver-class-name=org.h2.Driver");
            assertThat(content).contains("spring.datasource.url=jdbc:h2:mem:passengers");
        }

        /**
         * Verify the use case when user enters something like:
         *
         *  rest-api create-project --name=taxi-service --package=org.example
         *
         *  Expected behavior: when no schema name is given,
         *  use the application name as the database schema name.
         */
        @Test void shouldUseApplicationNameWhenSchemaIsUndefined() {
            Optional<CatalogEntry> optional = getApplicationDotPropertiesTemplate();
            assertThat(optional).isNotNull();
            assertThat(optional.isPresent()).isTrue();

            final String appName = "taxi-service";

            projectProperties.remove(ProjectKeys.SCHEMA);   // schema is undefined
            projectProperties.put(ProjectKeys.APPLICATION_NAME, appName);    // app Name is given
            projectProperties.remove("features");   // no specific database picked

            TemplateHandler handler = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(freemarkerConfiguration)
                    .build();

            String content = handler.render();
            assertThat(content).isNotNull();
            assertThat(content).contains("spring.datasource.driver-class-name=org.h2.Driver");
            assertThat(content).contains("spring.datasource.url=jdbc:h2:mem:"+appName);
        }

        /**
         * Verify the use case when the user enters something like:
         *
         *  rest-api create-project --package org.example
         *
         *  Expected behavior: when no schema name nor application name is given,
         *  the code generator uses a default schema name
         */
        @Test void shouldUseDefaultNameWhenNothingElseToPick() {
            Optional<CatalogEntry> optional = getApplicationDotPropertiesTemplate();
            assertThat(optional).isNotNull();
            assertThat(optional.isPresent()).isTrue();

            projectProperties.remove(ProjectKeys.SCHEMA);   // schema is undefined
            projectProperties.remove(ProjectKeys.APPLICATION_NAME);    // appName is undefined
            projectProperties.remove("features");   // no specific database picked

            TemplateHandler handler = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(freemarkerConfiguration)
                    .build();

            String content = handler.render();
            assertThat(content).isNotNull();
            assertThat(content).contains("spring.datasource.driver-class-name=org.h2.Driver");
            assertThat(content).contains("spring.datasource.url=jdbc:h2:mem:testdb");
        }
    }

    @Nested
    class TestBasePathOption {
        /**
         * If the basePath is defined, the context-path = basePath
         */
        @Test
        void shouldMatchServerServletContextPath() {
            // preliminary
            Optional<CatalogEntry> optional = getApplicationDotPropertiesTemplate();
            assertThat(optional).isNotNull();
            assertThat(optional.isPresent()).isTrue();

            final String basePath = "/my-application";
            // when
            projectProperties.put(ProjectKeys.BASE_PATH, basePath);

            TemplateHandler handler = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(freemarkerConfiguration)
                    .build();

            // then
            String content = handler.render();
            assertThat(content).isNotNull();
            assertThat(content).contains("server.servlet.context-path="+basePath);
        }

        /**
         * If the basePath is undefined, the context-path should be '/'
         */
        @Test
        void shouldDefaultToSlashRoute() {
            // preliminary
            Optional<CatalogEntry> optional = getApplicationDotPropertiesTemplate();
            assertThat(optional).isNotNull();
            assertThat(optional.isPresent()).isTrue();

            // when
            projectProperties.remove(ProjectKeys.BASE_PATH);

            TemplateHandler handler = TemplateHandler.builder()
                    .catalogEntry(optional.get())
                    .properties(projectProperties)
                    .configuration(freemarkerConfiguration)
                    .build();

            // then
            String content = handler.render();
            assertThat(content).isNotNull();
            // If the user does not specify a base-path, the default is '/'.
            // This is the default path used by Spring, so we don't have to
            // define this property, but having the property present enables
            // users to easily change this by having the entry already available.
            assertThat(content).contains("server.servlet.context-path=/");
        }
    }

    // ---------------------------------------------------------------------------------------------------
    // Helper functions
    // ---------------------------------------------------------------------------------------------------
    
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

    /**
     * Converts a String into an array of tokens, using spaces as the token delimiter
     */
    private String[] toArgV(String s) {
        return s.split("\\s");
    }


    /**
     * Fetches the template that produces the build.gradle file
     */
    private Optional<CatalogEntry> getBuildDotGradleTemplate() {
        List<CatalogEntry> entries = catalog.filterByNameLike("BuildDotGradle");
        return entries.stream().findFirst();
    }

    /**
     * Fetches the template for application.properties
     */
    private Optional<CatalogEntry> getApplicationDotPropertiesTemplate() {
        List<CatalogEntry> entries = catalog.filterByNameLike("ApplicationDotProperties");
        return entries.stream().findFirst();
    }

    /**
     * Returns a org.apache.commons.configuration2.Configuration equivalent to what is found in a mojo.properties file
     * (Not to be confused with a FreeMarker Configuration)
     */
    private org.apache.commons.configuration2.Configuration buildFakeMojoProperties(String basePackage, String basePath) throws Exception {
        Environment.addVariable(ProjectKeys.BASE_PACKAGE, basePackage);
        Environment.addVariable(ProjectKeys.BASE_PATH, basePath);
        Environment.addVariable(ProjectKeys.FRAMEWORK, SupportedFramework.WEBFLUX.toString());

        return new MojoProperties().getConfiguration();
    }
}
