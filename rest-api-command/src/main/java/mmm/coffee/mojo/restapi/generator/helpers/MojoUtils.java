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
package mmm.coffee.mojo.restapi.generator.helpers;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

/**
 * A collection of helper methods
 */
public class MojoUtils {

    private MojoUtils() {}

    
    /**
     * Returns the current working directory
     * @return the current working directory, ending with a front-slash, such as "some/path/";
     */
    @NonNull public static String currentDirectory() {
        return System.getProperty("user.dir") + "/"; // the JVM guarantees this property has a value
    }
    
    /**
     * Conjures the package name to which the code for the given RESTful resource (aka Entity) will be written.
     * The convention followed is: {basePackage}.endpoint.{lowercaseVersionOfResourceName}.
     * For example, if the basePackage is ```org.example.greeting_service``` and the resource is ```Greeting```,
     * the package name returned will be ```
     * ```org.example.greeting_service.endpoint.greeting```.
     * (where ```org.example.greeting_service``` is the assumed base package, and ```Greeting``` is the resource/entity.
     *
     * @param basePackage the base package of the project
     * @param resourceOrEntityName the name of the RESTful resource/entity, for example ```Student``` or ```Account```
     * @return the package name into which the assets of this resource will be placed
     */
    @NonNull
    public static String getPackageNameForResource(@NonNull String basePackage, @NonNull String resourceOrEntityName) {
        String packageName = basePackage + ".endpoint." + StringUtils.toRootLowerCase(resourceOrEntityName);
        return StringUtils.toRootLowerCase(packageName);
    }

    @NonNull
    public static String convertPackageNameToPath(@NonNull String packageName) { return packageName.replace(".", "/");
    }
}
