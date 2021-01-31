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
import mmm.coffee.mojo.exception.MojoException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A collection of helper methods
 */
public class MojoUtils {

    private MojoUtils() {}

    /**
     * Saves project-level properties to a hidden file to enable
     * these properties to be recalled when endpoints are generated.
     * For example, endpoint classes need to know the basePackage
     * of the project.
     */
    static void saveContext(@NonNull Map<String,Object> properties) {
        final String mojoFileName = getMojoFileName();
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(mojoFileName))) {
            properties.entrySet().forEach( entry -> pw.printf("%s=%s%n", entry.getKey(), entry.getValue() ));
            pw.flush();
        }
        catch (Exception e) {
            throw new MojoException(String.format("ERROR: Tried to save '.mojo' file but encounted this: %s%n", e.getMessage()), e);
        }
    }

    @NonNull static Map<String,String> loadContext() {
        Map<String,String> resultMap = new HashMap<>();
        try (InputStream is = new FileInputStream(getMojoFileName())) {
            Properties properties = new Properties();
            properties.load(is);
            properties.forEach((key, value) -> resultMap.put((String) key, (String) value));
        }
        catch (IOException e) {
            throw new MojoException(String.format("ERROR: %s%n", e.getMessage()), e);
        }
        return resultMap;
    }


    /**
     * Returns the current working directory
     * @return the current working directory, ending with a front-slash, such as "some/path/";
     */
    @NonNull static String currentDirectory() {
        return System.getProperty("user.dir") + "/"; // JVM guarantees this has a value
    }

    @NonNull static String getMojoFileName() {
        return currentDirectory() + ".mojo";
    }

    /**
     * Conjures the package name to which the code for the given RESTful resource (aka Entity) will be written.
     * The convention followed is: {basePackage}.endpoint.{lowercaseVersionOfResourceName}.
     * For example, if the basePackage is ```org.example.greeting_service``` and the resource is ```Greeting```,
     * the package name returned will be ```
     * ```org.example.greeting_service.endpoint.greeting```.
     * (where ```org.example.greeting_service``` is the assumed base package, and ```Greeting``` is the resource/entity.
     *
     * @param resourceOrEntityName the name of the RESTful resource/entity, for example ```Student``` or ```Account```
     * @return the package name into which the assets of this resource will be placed
     */
    @NonNull static String getPackageNameForResource(@NonNull String resourceOrEntityName) {
        String basePackage = loadContext().get("basePackage");
        String packageName = basePackage + ".endpoint." + StringUtils.toRootLowerCase(resourceOrEntityName);
        return StringUtils.toRootLowerCase(packageName);
    }

    @NonNull static String convertPackageNameToPath(@NonNull String packageName) { return packageName.replace(".", "/");
    }
}
