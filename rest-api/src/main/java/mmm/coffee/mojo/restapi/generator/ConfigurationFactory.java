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
import freemarker.template.TemplateExceptionHandler;
import lombok.NonNull;

/**
 * Builds a Configuration instance suitable for this component's needs.
 *
 * References:
 *  https://freemarker.apache.org/docs/pgui_quickstart_createconfiguration.html
 *  https://www.vogella.com/tutorials/FreeMarker/article.html
 */
public class ConfigurationFactory {

    public static final String TEMPLATE_PATH = "/restapi/templates/";


    /**
     * Creates a Configuration instance with some default settings.
     * @return a Configuration
     */
    @NonNull static Configuration defaultConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setClassForTemplateLoading(ConfigurationFactory.class, TEMPLATE_PATH);
        configuration.setDefaultEncoding("UTF-8");
        // TODO: for production, use RETHROWS_HANDLER
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        configuration.setLogTemplateExceptions(false);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);
        
        return configuration;
    }
}
