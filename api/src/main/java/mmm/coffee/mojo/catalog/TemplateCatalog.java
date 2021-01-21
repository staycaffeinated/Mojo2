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
package mmm.coffee.mojo.catalog;

import lombok.NonNull;
import mmm.coffee.mojo.exception.MojoException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstracts the contents of a template catalog. Provides
 * a helper method to filter the catalog entries.
 */
public class TemplateCatalog {

    public static final String CATALOG_NAME = "/restapi/catalog.yaml";

    private final List<CatalogEntry> entries;   // catalogReader.readCatalog guarantees a non-null list

    /**
     * Constructor
     */
    public TemplateCatalog(@NonNull String catalogName) {
        try {
            entries = new CatalogReader().readCatalog(catalogName);
        }
        catch (IOException e) {
            throw new MojoException(e.getMessage(), e);
        }
    }

    /**
     * Returns the catalog entries having the given {@code context}. May return an empty list.
     * @param context fetch entries having this context
     * @return the matching items
     */
    public @NonNull List<CatalogEntry> filterByContext(@NonNull String context) {
        return entries.stream().filter(it -> it.getContext().equals(context)).collect(Collectors.toList());
    }

    /**
     * Returns entries where the template name contains {@code templateName}
     * @param templateName the name of the template (can be a partial name)
     * @return the entries having templates named like {@code templateName}
     */
    public @NonNull List<CatalogEntry> filterByNameLike(@NonNull String templateName) {
        return entries.stream().filter(it -> it.getTemplate().contains(templateName)).collect(Collectors.toList());
    }
}
