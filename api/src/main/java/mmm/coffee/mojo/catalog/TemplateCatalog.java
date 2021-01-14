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

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstracts the contents of a template catalog. Provides
 * a helper method to filter the catalog entries.
 */
public class TemplateCatalog {

    public static final String CATALOG_NAME = "/restapi/catalog.yaml";

    private List<CatalogEntry> entries;

    /**
     * Constructor
     */
    public TemplateCatalog() {
        try {
            entries = new CatalogReader().readCatalog(CATALOG_NAME);
        }
        catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Returns the catalog entries having the given {@code context}. May return an empty list.
     * @param context
     * @return
     */
    public List<CatalogEntry> filterByContext(@NonNull String context) {
        if (entries == null) {
            return new LinkedList<>();
        }
        return entries.stream().filter(it -> it.getContext().equals(context)).collect(Collectors.toList());
    }

    /**
     * Returns the catalog entries having the given {@code context}. May return an empty list.
     * @param name look for templates with names like this
     * @return any templates with the template name like {@code name}
     */
    public List<CatalogEntry> filterByNameLike(@NonNull String name) {
        if (entries == null) {
            return new LinkedList<>();
        }
        return entries.stream().filter(it -> it.getTemplate().contains(name)).collect(Collectors.toList());
    }

}
