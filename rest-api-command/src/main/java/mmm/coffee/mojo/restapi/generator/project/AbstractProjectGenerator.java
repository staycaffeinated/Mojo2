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
package mmm.coffee.mojo.restapi.generator.project;

import freemarker.template.Configuration;
import lombok.NonNull;
import mmm.coffee.mojo.api.Generator;
import mmm.coffee.mojo.api.TemplateWriter;
import mmm.coffee.mojo.catalog.CatalogEntry;
import mmm.coffee.mojo.catalog.TemplateCatalog;
import mmm.coffee.mojo.library.DependencyCatalog;
import mmm.coffee.mojo.mixin.DryRunOption;
import mmm.coffee.mojo.restapi.generator.Catalogs;
import mmm.coffee.mojo.restapi.generator.ConfigurationFactory;
import mmm.coffee.mojo.restapi.generator.TemplateHandler;
import mmm.coffee.mojo.restapi.generator.helpers.MojoUtils;
import mmm.coffee.mojo.restapi.generator.helpers.MustacheExpressionResolver;
import mmm.coffee.mojo.restapi.shared.MojoProperties;
import mmm.coffee.mojo.restapi.shared.SupportedFeatures;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;

import java.io.File;
import java.util.*;

/**
 * The code generator for project assets
 */
public abstract class AbstractProjectGenerator implements Generator {

    // This contains the collection of templates to be rendered
    private final TemplateCatalog catalog = new TemplateCatalog();

    // The lexicalScope contains key/values consumed by the templates when rendering source files
    private final Map<String,Object> lexicalScope = new HashMap<>();

    // The sourceSink is a wrapper on the output stream that writes the rendered template
    private TemplateWriter sourceSink;

    // The Configuration is specific to Apache Freemarker
    private Configuration configuration;

    // This contains the list of additional features to be applied when generating code.
    // The features are specified with the '--support' flag in the CLI.
    // Examples are: postgres, testcontainers, liquibase
    private final List<String> features = new ArrayList<>();

    @Override
    public void initialize() {
        // Initialize Apache Freemarker
        configuration = ConfigurationFactory.defaultConfiguration();
    }
    
    public Optional<TemplateCatalog> getCatalog() {
        return Optional.of(catalog);
    }
    
    /**
     * Configure the lexical scope of the ProjectGenerator. This method defines the
     * variables that will be shared with the templates when the templates are rendered.
     * Another way of putting it: define the variables intended for consumption by the templates.
     *
     * Internally, the lexicalScope is populated with all properties expected by templates.
     * A runtime error occurs if a template cannot resolve a property.
     * 
     * @param commandLineOptions values captured from the command-line, passed to us via a Map
     */
    @Override
    public void setUpLexicalScope(@NonNull Map<String, Object> commandLineOptions) {
        // Copy the library dependency versions into the lexicalScope.
        // These keys are used when dependency.gradle and build.gradle are generated
        var dependencyCatalog = new DependencyCatalog(Catalogs.THIRD_PARTY_LIBRARY_CATALOG);
        dependencyCatalog.loadTemplateKeys(lexicalScope);

        // The caller provides the basePackage, applicationName, groupId, basePath, etc.
        // The caller is usually the SubcommandCreateProject.
        lexicalScope.putAll(commandLineOptions);

        // Set the programming model, which determines whether Spring-MVC libraries are used, or Spring-Webflux
        SupportedFramework framework = (SupportedFramework) commandLineOptions.get(ProjectKeys.FRAMEWORK);
        if (framework == null) framework = SupportedFramework.WEBMVC;
        lexicalScope.put(ProjectKeys.FRAMEWORK, framework.name());

        // Translate the base package value into the equivalent filesystem folder hierarchy
        // For example, the package 'org.example' translates to the folder 'org/example'
        var basePackagePath = MojoUtils.convertPackageNameToPath((String)commandLineOptions.get(ProjectKeys.BASE_PACKAGE));
        lexicalScope.put(ProjectKeys.BASE_PACKAGE_PATH, basePackagePath);

        // Add the feature options into the lexical scope
        copyFeatures((SupportedFeatures[]) commandLineOptions.get("features"));
    }

    @Override
    public void setUpLexicalScope(@NonNull Map<String, Object> commandLineOptions, org.apache.commons.configuration2.Configuration ignored) {
        this.setUpLexicalScope(commandLineOptions);
    }

    @Override
    public Map<String,Object> getLexicalScope() { return lexicalScope; }

    @Override
    public void setOutputStrategy(@NonNull TemplateWriter writer) {
        this.sourceSink = writer;
    }

    @Override
    public void generate() {
        List<CatalogEntry> catalogEntries = catalog.filterByContext("project");

        // Generate all the assets that are needed regardless of any features selected by the end-user.
        // For example, a build.gradle file and an Application.java file are always created.
        catalogEntries.stream().filter(CatalogEntry::isFeatureIndependent).forEach(this::renderTemplate);

        // For each feature (i.e., added dependency), generate the assets specific to that feature
        features.forEach( f -> catalogEntries.stream().filter(e -> e.hasFeature(f)).forEach(this::renderTemplate));
    }

    /**
     * Saves the mojo.properties file to the root directory of the generated project.
     */
    @Override
    public void tearDown() {
        // If this was -not- a dry run, write the mojo.properties file
        if (Boolean.FALSE.equals(lexicalScope.getOrDefault(DryRunOption.DRY_RUN_KEY, Boolean.FALSE))) {
            MojoProperties.saveConfiguration (lexicalScope);
        }
    }

    /**
     * Returns a reference to our TemplateCatalog
     */
    protected TemplateCatalog catalog() { return catalog; }

    /**
     * Renders the given template
     *
     * @param entry the metadata about the template to render
     */
    private void renderTemplate(CatalogEntry entry) {
        TemplateHandler template = TemplateHandler.builder()
                                    .catalogEntry(entry)
                                    .properties(lexicalScope)
                                    .configuration(configuration)
                                    .build();
        var content = template.render();
        var outputFile = determineOutputFile(entry.getDestination());
        sourceSink.writeStringToFile(outputFile, content);
    }
    
    /**
     * Returns the File to which the content will be written.
     * The destination path is a mustache expression found in the webmvc-catalog.yaml.
     *
     * @param destinationAsMustacheExpression the destination folder, represented in a mustache expression
     * @return the handle of the file at the resolved location
     */
    private File determineOutputFile(String destinationAsMustacheExpression) {
        var fileName = MustacheExpressionResolver.toString(destinationAsMustacheExpression, lexicalScope);
        var fqFileName = MojoUtils.currentDirectory() + fileName;
        return new File(fqFileName);
    }

    /**
     * Copies the features passed in from the CLI into the data model
     * consumed by the templates.
     *
     * @param features the features (i.e., additional library dependencies) submitted on the command line
     *                 and passed into the generator
     */
    private void copyFeatures(SupportedFeatures[] features) {
        if (features != null) {
            Arrays.stream(features).forEach(f -> {
                lexicalScope.put(f.toString(), "true");
                this.features.add(f.toString());
            });
        }
    }
}
