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

import freemarker.template.Configuration;
import lombok.NonNull;
import mmm.coffee.mojo.api.Generator;
import mmm.coffee.mojo.api.TemplateWriter;
import mmm.coffee.mojo.catalog.CatalogEntry;
import mmm.coffee.mojo.catalog.TemplateCatalog;
import mmm.coffee.mojo.exception.MojoException;
import mmm.coffee.mojo.restapi.generator.ConfigurationFactory;
import mmm.coffee.mojo.restapi.generator.TemplateHandler;
import mmm.coffee.mojo.restapi.generator.helpers.MustacheExpressionResolver;

import java.io.File;
import java.util.*;


/**
 * Generates the assets of a given endpoint, such as the controller,
 * service, and repository.
 */
public abstract class AbstractEndpointGenerator implements Generator {

    public static final String ENDPOINT_CONTEXT = "endpoint";

    protected TemplateCatalog templateCatalog;
    private List<CatalogEntry> catalogEntries = new ArrayList<>();
    private final Map<String,Object> lexicalScope = new HashMap<>();
    private TemplateWriter sourceSink;
    private Configuration configuration;

    @Override
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
        throw new MojoException("This method not supported when creating endpoints. Endpoints additionally need the meta-data from mojo.properties.");
    }

    public void setUpLexicalScope(@NonNull Map<String,Object> commandLineOptions,
                                  org.apache.commons.configuration2.Configuration mojoProps) {

        var factory = new EndpointLexicalScopeFactory();
        factory.setCommandLineOptions(commandLineOptions);
        factory.setMojoProps(mojoProps);
        var map = factory.createLexicalScope();
        map.forEach(lexicalScope::put);
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
        var content = template.render();
        var outputFile = determineOutputFile(entry.getDestination());
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
        var fileName = MustacheExpressionResolver.toString(destinationAsMustacheExpression, lexicalScope);
        return new File(fileName);
    }
}
