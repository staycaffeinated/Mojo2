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
package mmm.coffee.mojo.restapi.generator.project.spring;

import mmm.coffee.mojo.catalog.TemplateCatalog;
import mmm.coffee.mojo.restapi.generator.Catalogs;

/**
 * A project generator for a Spring WebMvc project. This class
 * generates the essential project assets for a Spring WebMvc-based application.
 * The assets generated include the Gradle files, a README, the main application class,
 * and a base controller (to handle the starting path, '/' ).
 */
public class SpringWebMvcProjectGenerator extends SpringProjectGenerator {

    /**
     * Loads templates that are exclusive to WebMvc projects.
     * The end result is both common templates and WebMvc templates are loaded and cached in the catalog
     */
    @Override
    public void loadTemplates() {
        super.loadTemplates();
        catalog().append ( loadWebMvcTemplates() );
    }

    private TemplateCatalog loadWebMvcTemplates() {
        return new TemplateCatalog(Catalogs.WEBMVC_CATALOG);
    }
}
