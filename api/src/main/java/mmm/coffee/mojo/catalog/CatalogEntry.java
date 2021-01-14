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

/**
 * This object models the entries of the template Catalog
 */
import lombok.Data;

@Data
public class CatalogEntry {
    private String template;
    private String destination;
    private String context;     // this tells us what to send the template engine to resolve template properties
    private String feature;     // this tells us if the template is to support a specific feature

    public boolean isFeatureIndependent() { return isEmpty(feature); }

    public boolean hasFeature(String feature) {
        if (isEmpty(feature)) return false;
        if (feature.equalsIgnoreCase(this.feature)) return true;
        return false;
    }

    private boolean isEmpty(String s) { return s == null || s.trim().length() == 0; }
}
