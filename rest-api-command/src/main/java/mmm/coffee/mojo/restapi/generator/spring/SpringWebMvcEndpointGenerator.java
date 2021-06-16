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
package mmm.coffee.mojo.restapi.generator.spring;

import mmm.coffee.mojo.catalog.TemplateCatalog;
import mmm.coffee.mojo.restapi.generator.AbstractEndpointGenerator;
import mmm.coffee.mojo.restapi.generator.Catalogs;


/**
 * Generates the assets of a single endpoint, such as the Controller,
 * Service, and Repository classes of that endpoint.
 */
public class SpringWebMvcEndpointGenerator extends AbstractEndpointGenerator {

    /**
     * Loads the catalog of templates used for Spring WebMVC endpoint artifacts.
     */
    @Override
    public void initialize() {
        super.initialize();
        templateCatalog = new TemplateCatalog(Catalogs.WEBMVC_CATALOG);
    }
}
