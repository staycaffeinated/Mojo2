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
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Reads a template catalog, returning a list of CatalogEntry
 */
public class CatalogReader {

    // These are the keys expected to be defined in the catalog.yaml file
    private static final String CATALOG_KEY = "catalog";
    private static final String CATALOG_ENTRY_KEY = "entry";
    private static final String CONTEXT_KEY = "context";
    private static final String TEMPLATE_KEY = "template";
    private static final String DESTINATION_KEY = "destination";
    private static final String FEATURE_KEY = "feature";

    /**
     * Loads the content of the catalog.yaml file, returning the content as a list of CatalogEntry.
     *
     * @param catalog the resource path of the catalog.yaml file
     * @return the entries of the catalog, as a list of CatalogEntry
     */
    @SuppressWarnings("unchecked")
    public @NonNull List<CatalogEntry> readCatalog(@NonNull String catalog) throws IOException {
        try (InputStream is = this.getClass().getResourceAsStream(catalog)) {
            // Fail fast if the catalog.yaml file wasn't found
            Objects.requireNonNull(is, String.format("The catalog file, '%s', was not found. Verify the resource exists at the given path.", catalog));

            // Load the yaml content as CatalogEntry items
            var yaml = new Yaml();
            Map<String, Object> obj = yaml.load(is);
            List<Map<String, Object>> entries = (List<Map<String, Object>>) obj.get(CATALOG_KEY);
            return entries.stream().map(CatalogReader::readCatalogEntry).collect(Collectors.toList());
        }
    }

    @SuppressWarnings("unchecked")
    /**
     * Reads the values loaded by YAML into a CatalogEntry object
     * @param map these are the values from the yaml file
     */
    private static CatalogEntry readCatalogEntry (Map<String,Object> map) {
        var catalogEntry = new CatalogEntry();
        Map<String,Object> values = (Map<String,Object>)map.get(CATALOG_ENTRY_KEY);
        catalogEntry.setContext((String)values.get(CONTEXT_KEY));
        catalogEntry.setDestination((String)values.get(DESTINATION_KEY));
        catalogEntry.setTemplate((String)values.get(TEMPLATE_KEY));
        catalogEntry.setFeature( (String)values.get(FEATURE_KEY) );
        return catalogEntry;
    }
}
