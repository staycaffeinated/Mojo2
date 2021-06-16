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
import freemarker.template.TemplateException;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mmm.coffee.mojo.catalog.CatalogEntry;
import mmm.coffee.mojo.exception.MojoException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Encapsulates the workflow of rendering a template
 */
@Builder
@RequiredArgsConstructor
public class TemplateHandler {
    // The CatalogEntry tells us which ST template to load
    // and where to write the output of the rendered template
    private final CatalogEntry catalogEntry;

    // The properties to be resolved when the template is rendered
    private final Map<String,Object> properties;
    
    private final Configuration configuration;

    /**
     * Renders the template, resolving any variables within the template
     * @return the resolved content of the template
     */
    @NonNull public String render() {
        try {
            // Read the template file, or get it from cache
            var template = configuration.getTemplate(catalogEntry.getTemplate(), "UTF-8");

            // Set up the data model to be passed to the template
            Map<String,Object> map = new HashMap<>();
            map.put(catalogEntry.getContext(), properties);

            // Parse the template and write it to cache
            var writer = new StringWriter();
            template.process(map, writer);
            return writer.toString();
        } catch (TemplateException | IOException e) {
            throw new MojoException(e);
        }
    }
}
