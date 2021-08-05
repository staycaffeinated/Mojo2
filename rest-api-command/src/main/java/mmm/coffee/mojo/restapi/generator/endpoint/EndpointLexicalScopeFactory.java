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
package mmm.coffee.mojo.restapi.generator.endpoint;

import mmm.coffee.mojo.restapi.generator.project.ProjectKeys;
import mmm.coffee.mojo.restapi.generator.helpers.MojoUtils;
import mmm.coffee.mojo.restapi.generator.helpers.NamingRules;
import org.apache.commons.configuration2.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Builder for lexical scope of an endpoint
 */
public class EndpointLexicalScopeFactory {

    private Configuration mojoProps;
    private Map<String,Object> commandLineOptions;
    private final Map<String,Object> lexicalScope;


    public EndpointLexicalScopeFactory() {
        lexicalScope = new HashMap<>();
    }

    public void setCommandLineOptions(Map<String,Object> commandLineOptions) {
        this.commandLineOptions = commandLineOptions;
    }
    public void setMojoProps(Configuration configuration) {
        this.mojoProps = configuration;
    }

    public Map<String,Object> create() {
        lexicalScope.put(ProjectKeys.BASE_PATH, mojoProps.getString(ProjectKeys.BASE_PATH));
        lexicalScope.put(ProjectKeys.BASE_PACKAGE, mojoProps.getString(ProjectKeys.BASE_PACKAGE));
        lexicalScope.put(ProjectKeys.FRAMEWORK, mojoProps.getString(ProjectKeys.FRAMEWORK));

        lexicalScope.putAll(commandLineOptions);

        var basePackage = mojoProps.getString(ProjectKeys.BASE_PACKAGE);
        var resourceName = (String) commandLineOptions.get(EndpointKeys.CMDLINE_RESOURCE_ARG);
        var entityName = NamingRules.toEntityName(resourceName);
        var entityVarName = NamingRules.toEntityVariableName(resourceName);
        var basePath = NamingRules.toBasePathUrl((String) commandLineOptions.get( EndpointKeys.CMDLINE_ROUTE_ARG ));
        var packageName = NamingRules.toEndpointPackageName(basePackage, resourceName);
        var packagePath = MojoUtils.convertPackageNameToPath(packageName);
        var basePackagePath = MojoUtils.convertPackageNameToPath(basePackage);
        var entityPojoName = NamingRules.toPojoClassName(resourceName);
        var entityEjbName = NamingRules.toEjbClassName(resourceName);

        lexicalScope.put(ProjectKeys.BASE_PACKAGE_PATH, basePackagePath);
        lexicalScope.put(EndpointKeys.ENTITY_NAME, entityName);
        lexicalScope.put(EndpointKeys.BASE_PATH, basePath);
        lexicalScope.put(EndpointKeys.ENTITY_LOWER_CASE_NAME, entityName.toLowerCase());
        lexicalScope.put(EndpointKeys.ENTITY_VAR_NAME, entityVarName);
        lexicalScope.put(EndpointKeys.PACKAGE_NAME, packageName);
        lexicalScope.put(EndpointKeys.PACKAGE_PATH, packagePath);
        lexicalScope.put(EndpointKeys.TABLE_NAME, entityName);
        lexicalScope.put(EndpointKeys.ENTITY_POJO, entityPojoName);
        lexicalScope.put(EndpointKeys.ENTITY_EJB, entityEjbName);

        return lexicalScope;
    }
}
