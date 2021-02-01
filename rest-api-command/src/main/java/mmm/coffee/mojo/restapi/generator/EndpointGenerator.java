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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Generates the assets of a given endpoint, such as the controller,
 * service, and repository.
 */
public class EndpointGenerator implements Generator {

    public static final String ENDPOINT_CONTEXT = "endpoint";

    private List<CatalogEntry> catalogEntries;
    private final Map<String,Object> lexicalScope = new HashMap<>();
    private TemplateWriter sourceSink;
    private Configuration configuration;

    /**
     * Initialization phase
     */
    public void initialize()  {
        catalogEntries = new TemplateCatalog(TemplateCatalog.CATALOG_NAME).filterByContext(ENDPOINT_CONTEXT);
        configuration = ConfigurationFactory.defaultConfiguration();
    }

    /**
     * Configuration phase
     * @param commandLineOptions properties to be consumed by the template
     */
    @Override
    public void configure(@NonNull Map<String, Object> commandLineOptions) {
        Map<String,String> mojoProperties;
        if ( isDryRun(commandLineOptions) ) {
            mojoProperties = MojoUtils.loadMojoPropertiesFromDryRun();
        }
        else {
            mojoProperties = MojoUtils.loadMojoProperties();
        }
        lexicalScope.putAll(mojoProperties);
        lexicalScope.putAll(commandLineOptions);

        String basePackage = mojoProperties.get(ProjectKeys.BASE_PACKAGE);
        String resourceName = (String) commandLineOptions.get("resource");
        String entityName = SyntaxRules.entityNameSyntax(resourceName);
        String entityVarName = SyntaxRules.entityVarNameSyntax(resourceName);
        String basePath = SyntaxRules.basePathSyntax((String) commandLineOptions.get("route"));
        String packageName = MojoUtils.getPackageNameForResource(basePackage, resourceName);
        String packagePath = MojoUtils.convertPackageNameToPath(packageName);

        lexicalScope.put(EndpointKeys.ENTITY_NAME, entityName);
        lexicalScope.put(EndpointKeys.BASE_PATH, basePath);
        lexicalScope.put(EndpointKeys.ENTITY_LOWER_CASE_NAME, entityName.toLowerCase());
        lexicalScope.put(EndpointKeys.ENTITY_VAR_NAME, entityVarName);
        lexicalScope.put(EndpointKeys.PACKAGE_NAME, packageName);
        lexicalScope.put(EndpointKeys.PACKAGE_PATH, packagePath);
        lexicalScope.put(EndpointKeys.TABLE_NAME, entityName);
    }

    @Override
    public void outputStrategy(@NonNull TemplateWriter writer) {
        this.sourceSink = writer;
    }

    @Override
    public void generate() {
        catalogEntries.forEach(this::renderTemplate);
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
     * @param destinationAsMustacheExpression the expression to evaluate
     * @return the resolved expression
     */
    private File determineOutputFile(String destinationAsMustacheExpression) {
        String fileName = MustacheConversion.toString(destinationAsMustacheExpression, lexicalScope);
        return new File(fileName);
    }

    /**
     * Returns {@code true} if the --dry-run option was found
     * @param commandLineOptions the options passed in from the command line
     * @return {@code true} if --dry-run was found; {@code false} otherwise.
     */
    private boolean isDryRun(Map<String,Object> commandLineOptions) {
        boolean value = ((Boolean)commandLineOptions.getOrDefault(DryRunOption.DRY_RUN_KEY, Boolean.FALSE)).booleanValue();
        return value;
    }
}
