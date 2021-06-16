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
import mmm.coffee.mojo.restapi.generator.AbstractProjectGenerator;
import mmm.coffee.mojo.restapi.generator.Catalogs;

/**
 * This is the code generator for a Spring framework project. This generator
 * lays down the essential files for a Spring application (the gradle files,
 * the main application class, a root controller, etc.).
 */
public class SpringProjectGenerator extends AbstractProjectGenerator {

    /**
     * Loads the templates commong to Spring WebMvc and Spring WebFlux
     */
    @Override
    public void loadTemplates() {
        catalog().append( loadCommonTemplates() )
               .append( loadSpringGradleTemplates() );
    }

    private TemplateCatalog loadCommonTemplates() {
        return new TemplateCatalog(Catalogs.COMMON_CATALOG);
    }
    private TemplateCatalog loadSpringGradleTemplates() {
        return new TemplateCatalog(Catalogs.GRADLE_CATALOG);
    }

}
