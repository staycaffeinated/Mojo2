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

/**
 * These are properties used by endpoint-level templates
 */
public class EndpointKeys {

    // prevent creating instances of this
    private EndpointKeys() {}

    public static final String ROOT_KEY = "endpoint";
    public static final String ENTITY_NAME = "entityName";
    public static final String ENTITY_VAR_NAME = "entityVarName";   // used when a variable name is wanted
    public static final String ENTITY_LOWER_CASE_NAME = "lowerCaseEntityName";
    public static final String ENTITY_POJO = "pojoName";            // classname of the domain (resource) object
    public static final String ENTITY_EJB = "ejbName";              // classname of the EJB object
    public static final String PACKAGE_NAME = "packageName";        // the endpoints package name
    public static final String PACKAGE_PATH = "packagePath";        // the endpoint package name as a file system path
    public static final String BASE_PATH = "basePath";              // the project's base path
    public static final String TABLE_NAME = "tableName";            // the database table name

    public static final String CMDLINE_RESOURCE_ARG = "resource";
    public static final String CMDLINE_ROUTE_ARG = "route";
}
