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

import freemarker.template.Configuration;
import lombok.NonNull;
import mmm.coffee.mojo.api.Generator;
import mmm.coffee.mojo.api.TemplateWriter;
import mmm.coffee.mojo.catalog.CatalogEntry;
import mmm.coffee.mojo.catalog.TemplateCatalog;
import mmm.coffee.mojo.mixin.DryRunOption;
import mmm.coffee.mojo.restapi.generator.*;
import mmm.coffee.mojo.restapi.generator.helpers.MojoProperties;
import mmm.coffee.mojo.restapi.generator.helpers.MojoUtils;
import mmm.coffee.mojo.restapi.generator.helpers.MustacheExpressionResolver;
import mmm.coffee.mojo.restapi.generator.helpers.NamingRules;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Generates the assets of a single endpoint, such as the Controller,
 * Service, and Repository classes of that endpoint.
 */
public class SpringWebMvcEndpointGenerator extends AbstractEndpointGenerator {

    /**
     * Loads the catalog of templates used for Spring WebMVC endpoint artifacts.
     */
    public void initialize() {
        super.initialize();
        templateCatalog = new TemplateCatalog(Catalogs.WEBMVC_CATALOG);
    }
}
