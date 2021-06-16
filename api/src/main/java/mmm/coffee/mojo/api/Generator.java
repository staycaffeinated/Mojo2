/*
 * Copyright 2021 Jon Caulfield
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
package mmm.coffee.mojo.api;

import lombok.NonNull;
import mmm.coffee.mojo.catalog.TemplateCatalog;
import mmm.coffee.mojo.mixin.DryRunOption;
import org.apache.commons.configuration2.Configuration;

import java.util.Map;
import java.util.Optional;

/**
 * Defines the Generator stereotype
 */
@SuppressWarnings({ "java:S1116", "java:S5411" })
public interface Generator {

    /**
     * Initialize the state of the generator (whatever a default constructor would do, put that here)
     */
    default void initialize() {}

    /**
     * Loads the templates to be rendered
     */
    void loadTemplates();

    /**
     * Returns the master TemplateCatalog being used by the generator.
     * This method is exposed for testing. 
     */
    Optional<TemplateCatalog> getCatalog();

    /**
     * Configure the lexical scope of the generator. Whatever properties will be passed into the
     * templates during rendering, those properties are set here.
     *
     * @param commandLineOptions the command line options and anything the CLI might need to pass to the generator
     */
    void setUpLexicalScope(@NonNull Map<String, Object> commandLineOptions);

    /**
     * Configure the lexical scope of the generator. This is where properties that are consumed
     * by templates during rendering are defined.
     *
     * @param commandLineOptions the command line options and anything the CLI might need to pass to the generator
     * @param configuration properties acquired from a configuration file, such as the {@code mojo.properties} file
     */
    void setUpLexicalScope(@NonNull Map<String,Object> commandLineOptions, Configuration configuration);

    /**
     * Made visible for testing
     * @return a map containing the variables within the lexical scope of this generator.
     */
    Map<String,Object> getLexicalScope();

    /**
     * Configure the Writer that will write rendered templates. For example, by applying a NoOpTemplateWriter,
     * test cases can avoid writing test output to the file system.
     *
     * @param writer the TemplateWriter
     */
    void setOutputStrategy(TemplateWriter writer);

    /**
     * Render the templates
     */
    void generate();

    /**
     * Perform any post processing the Generator needs to do after the templates have been rendered.
     */
    default void tearDown() {}

    /**
     * Convenience method to enable clients of Generators to run the
     * generator without having to explicitly call the life cycle methods.
     * @param properties the properties consumed by the generator to resolve template values
     *                   and other variables
     */

    default int run(@NonNull Map<String,Object> properties) {
        TemplateWriter sourceSink;
        if ((Boolean)properties.getOrDefault(DryRunOption.DRY_RUN_KEY, Boolean.FALSE)) {
            sourceSink = new NoOpTemplateWriter();
        }
        else {
            sourceSink = new DefaultTemplateWriter();
        }
        initialize();
        loadTemplates();
        setUpLexicalScope(properties);
        setOutputStrategy(sourceSink);
        generate();
        tearDown();
        return 0;
    }

    default int run(@NonNull Map<String,Object> properties, Configuration configuration) {
        TemplateWriter sourceSink;
        if ((Boolean) properties.getOrDefault(DryRunOption.DRY_RUN_KEY, Boolean.FALSE)) {
            sourceSink = new NoOpTemplateWriter();
        }
        else {
            sourceSink = new DefaultTemplateWriter();
        }
        initialize();
        loadTemplates();
        setUpLexicalScope(properties, configuration);
        setOutputStrategy(sourceSink);
        generate();
        tearDown();
        return 0;
    }
}
