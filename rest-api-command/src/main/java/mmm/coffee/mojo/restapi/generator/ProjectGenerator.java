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
import lombok.NonNull;
import mmm.coffee.mojo.api.Generator;
import mmm.coffee.mojo.api.TemplateWriter;
import mmm.coffee.mojo.catalog.CatalogEntry;
import mmm.coffee.mojo.catalog.TemplateCatalog;
import mmm.coffee.mojo.library.Dependency;
import mmm.coffee.mojo.library.DependencyCatalog;
import mmm.coffee.mojo.restapi.shared.SupportedFeatures;

import java.io.File;
import java.util.*;

/**
 * The code generator for project assets
 */
public class ProjectGenerator implements Generator {

    public static final String PROJECT_CONTEXT = "project";
    
    private List<CatalogEntry> catalogEntries;
    private final Map<String,Object> lexicalScope = new HashMap<>();
    private TemplateWriter sourceSink;
    private Configuration configuration;
    private final List<String> features = new ArrayList<>();
    private List<Dependency> libraries;

    @Override
    public void initialize() {
        catalogEntries = new TemplateCatalog(Constants.TEMPLATE_CATALOG).filterByContext(PROJECT_CONTEXT);
        configuration = ConfigurationFactory.defaultConfiguration();
    }

    /**
     * Configure the lexical scope of the ProjectGenerator. This method defines the
     * variables that will be shared with the templates when the templates are rendered.
     *
     * Internally, the lexicalScope is populated with all properties expected by templates.
     * A runtime error occurs if a template cannot resolve a property.
     * 
     * @param commandLineOptions values captured from the command-line, passed to us via a Map
     */
    @Override
    public void configure(@NonNull Map<String, Object> commandLineOptions) {
        // Copy the library dependency versions into the lexicalScope.
        // These keys are used when dependency.gradle and build.gradle are generated
        DependencyCatalog catalog = new DependencyCatalog(Constants.DEPENDENCY_CATALOG);
        catalog.loadTemplateKeys(lexicalScope);

        // The caller provides the basePackage, applicationName, groupId, basePath, etc.
        // The caller is usually the SubcommandCreateProject.
        lexicalScope.putAll(commandLineOptions);

        String basePackagePath = MojoUtils.convertPackageNameToPath((String)commandLineOptions.get(ProjectKeys.BASE_PACKAGE));
        lexicalScope.put(ProjectKeys.BASE_PACKAGE_PATH, basePackagePath);

        copyFeatures((SupportedFeatures[]) commandLineOptions.get("features"));
    }

    // Visible for testing
    Map<String,Object> getConfiguration() { return lexicalScope; }

    @Override
    public void outputStrategy(@NonNull TemplateWriter writer) {
        this.sourceSink = writer;
    }

    @Override
    public void generate() {
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
        if ( !(Boolean) lexicalScope.getOrDefault("dryRun", Boolean.FALSE)) {
            MojoProperties props = MojoProperties.toMojoProperties(lexicalScope);
            MojoProperties.saveMojoProperties(props);
        }

    }

    private void renderTemplate(CatalogEntry entry) {
        TemplateHandler template = TemplateHandler.builder()
                                    .catalogEntry(entry)
                                    .properties(lexicalScope)
                                    .configuration(configuration)
                                    .build();
        String content = template.render();
        File outputFile = determineOutputFile(entry.getDestination());
        sourceSink.writeStringToFile(outputFile, content);
    }
    
    /**
     * Returns the File to which the content will be written.
     * The destination path is a mustache expression found in the catalog.yaml.
     *
     * @param destinationAsMustacheExpression the destination folder, represented in a mustache expression
     * @return the handle of the file at the resolved location
     */
    private File determineOutputFile(String destinationAsMustacheExpression) {
        String fileName = MustacheConversion.toString(destinationAsMustacheExpression, lexicalScope);
        String fqFileName = MojoUtils.currentDirectory() + fileName;
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
