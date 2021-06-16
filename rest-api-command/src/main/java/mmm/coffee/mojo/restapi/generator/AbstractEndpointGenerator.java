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
import mmm.coffee.mojo.mixin.DryRunOption;
import mmm.coffee.mojo.restapi.generator.helpers.MojoUtils;
import mmm.coffee.mojo.restapi.generator.helpers.MustacheExpressionResolver;
import mmm.coffee.mojo.restapi.generator.helpers.NamingRules;

import java.io.File;
import java.util.*;


/**
 * Generates the assets of a given endpoint, such as the controller,
 * service, and repository.
 *
 * TODO: Make this class a SpringWebMvcEndpointGenerator and add SpringWebFluxEndpointGenerator,
 * since we'll have two sets of templates for each style of endpoint.
 */
public abstract class AbstractEndpointGenerator implements Generator {

    public static final String ENDPOINT_CONTEXT = "endpoint";

    protected TemplateCatalog templateCatalog;
    private List<CatalogEntry> catalogEntries = new ArrayList<>();
    private final Map<String,Object> lexicalScope = new HashMap<>();
    private TemplateWriter sourceSink;
    private Configuration configuration;

    public void initialize() {
        configuration = ConfigurationFactory.defaultConfiguration();
    }

    /**
     * Loads the templates
     */
    public void loadTemplates()  {
        // Select catalogEntries applicable to endpoint artifacts (such as Controllers and Repositories).
        addCatalogEntries(templateCatalog.filterByContext(ENDPOINT_CONTEXT));
    }

    public void setUpLexicalScope(@NonNull Map<String,Object> commandLineOptions) {
        throw new RuntimeException("This method not supported when creating endpoints");
    }

    public void setUpLexicalScope(@NonNull Map<String, Object> commandLineOptions,
                                  org.apache.commons.configuration2.Configuration mojoProps) {
        lexicalScope.put(ProjectKeys.BASE_PATH, mojoProps.getString(ProjectKeys.BASE_PATH));
        lexicalScope.put(ProjectKeys.BASE_PACKAGE, mojoProps.getString(ProjectKeys.BASE_PACKAGE));
        lexicalScope.put(ProjectKeys.FRAMEWORK, mojoProps.getString(ProjectKeys.FRAMEWORK));

        lexicalScope.putAll(commandLineOptions);

        String basePackage = mojoProps.getString(ProjectKeys.BASE_PACKAGE);
        String resourceName = (String) commandLineOptions.get("resource");
        String entityName = NamingRules.toEntityName(resourceName);
        String entityVarName = NamingRules.toEntityVariableName(resourceName);
        String basePath = NamingRules.toBasePathUrl((String) commandLineOptions.get("route"));
        String packageName = MojoUtils.getPackageNameForResource(basePackage, resourceName);
        String packagePath = MojoUtils.convertPackageNameToPath(packageName);
        String basePackagePath = MojoUtils.convertPackageNameToPath(basePackage);

        lexicalScope.put(ProjectKeys.BASE_PACKAGE_PATH, basePackagePath);
        lexicalScope.put(EndpointKeys.ENTITY_NAME, entityName);
        lexicalScope.put(EndpointKeys.BASE_PATH, basePath);
        lexicalScope.put(EndpointKeys.ENTITY_LOWER_CASE_NAME, entityName.toLowerCase());
        lexicalScope.put(EndpointKeys.ENTITY_VAR_NAME, entityVarName);
        lexicalScope.put(EndpointKeys.PACKAGE_NAME, packageName);
        lexicalScope.put(EndpointKeys.PACKAGE_PATH, packagePath);
        lexicalScope.put(EndpointKeys.TABLE_NAME, entityName);
    }
    
    @Override
    public Map<String,Object> getLexicalScope() { return lexicalScope; }

    @Override
    public Optional<TemplateCatalog> getCatalog() {
        return Optional.of(templateCatalog);
    }

    @Override
    public void setOutputStrategy(@NonNull TemplateWriter writer) {
        this.sourceSink = writer;
    }

    @Override
    public void generate() {
        catalogEntries.forEach(this::renderTemplate);
    }

    protected Configuration freeMarkerConfiguration() { return configuration; }

    protected void addCatalogEntries(@NonNull List<CatalogEntry> entries) {
        catalogEntries.addAll(entries);
    }

    protected void renderTemplate(CatalogEntry entry) {
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
     * The destination path is a mustache expression found in the webmvc-catalog.yaml.
     * 
     * @param destinationAsMustacheExpression the expression to evaluate
     * @return the resolved expression
     */
    private File determineOutputFile(String destinationAsMustacheExpression) {
        String fileName = MustacheExpressionResolver.toString(destinationAsMustacheExpression, lexicalScope);
        return new File(fileName);
    }

    /**
     * Returns {@code true} if the --dry-run option was found
     * @param commandLineOptions the options passed in from the command line
     * @return {@code true} if --dry-run was found; {@code false} otherwise.
     */
    private boolean isDryRun(Map<String,Object> commandLineOptions) {
        return (Boolean) commandLineOptions.getOrDefault(DryRunOption.DRY_RUN_KEY, Boolean.FALSE);
    }
}
