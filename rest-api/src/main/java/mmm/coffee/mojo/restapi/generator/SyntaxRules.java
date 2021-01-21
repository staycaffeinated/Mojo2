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

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

/**
 * The syntax rules we apply to resource names, entity names, base paths, and such
 * to provide consistent naming conventions
 */
public class SyntaxRules {

    private SyntaxRules() {}

    /**
     * Returns the syntax convention for the given resource value
     * @param resource the name of some resource or entity
     * @return the resource name, with syntax conventions applied
     */
    public static @NonNull String entityNameSyntax(@NonNull String resource) {
        // Capitalize the first char; leave others as-is
        return StringUtils.capitalize(resource);

    }
    public static @NonNull String entityVarNameSyntax(@NonNull String resource) {
        return StringUtils.uncapitalize(resource);
    }

    /**
     * Base path must begin with a forward-slash ('/'), and the route must be
     * all lower-case characters.
     *
     * @param route the suggested base path to the resource, probably input by an end-user
     * @return the basePath, with formatting conventions applied.
     */
    public static @NonNull String basePathSyntax(@NonNull String route) {
        if (!route.startsWith("/")) {
            route = "/" + route;
        }
        return StringUtils.toRootLowerCase(route);
    }

    /**
     * The dabase schema for JPA projects. Use this if a schema wasn't explicitly
     * defined on the command line
     * @param projectName the project name
     * @return the default schema name
     */
    public static @NonNull String schemaSyntax(@NonNull String projectName) {
        return StringUtils.toRootLowerCase(projectName);
    }
}
