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

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * This encapsulates the content of the mojo.properties file
 */
public class MojoProperties extends Properties {

    /**
     * Reads the mojo.properties file into a Map
     * @return a Map containing the contents of the mojo.properties file
     */
    @NonNull
    static MojoProperties loadMojoProperties() {
        MojoProperties mojoProperties = new MojoProperties();
        try (InputStream is = new FileInputStream(getMojoPropertiesFileName())) {
            mojoProperties.load(is);
        }
        catch (IOException e) {
            throw new MojoException(String.format("ERROR: %s%n", e.getMessage()), e);
        }
        return mojoProperties;
    }

    /**
     * Saves project-level properties to a hidden file to enable
     * these properties to be recalled when endpoints are generated.
     * For example, endpoint classes need to know the basePackage
     * of the project.
     */
    static void saveMojoProperties(@NonNull MojoProperties properties) {
        final String mojoFileName = getMojoPropertiesFileName();
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(mojoFileName))) {
            properties.store(pw, "These are properties from the Mojo code generator");
            pw.flush();
        }
        catch (Exception e) {
            throw new MojoException(String.format("ERROR: Tried to save '%s' file but encountered this: %s%n", mojoFileName, e.getMessage()), e);
        }
    }

    /**
     * This method is exposed to support test cases that do not want to write to the file system.
     * A fake set of properties that are typically found in the mojo.properties file are returned.
     */
    @NonNull static MojoProperties loadMojoPropertiesForDryRun() {
        MojoProperties mojoProperties = new MojoProperties();
        mojoProperties.put(ProjectKeys.BASE_PATH, "/my-service");
        mojoProperties.put(ProjectKeys.APPLICATION_NAME, "my-service");
        mojoProperties.put(ProjectKeys.SCHEMA, "exampleDB");
        mojoProperties.put(ProjectKeys.BASE_PACKAGE, "org.example.myservice");
        mojoProperties.put(ProjectKeys.BASE_PACKAGE_PATH, "org/example/myservice");
        mojoProperties.put(ProjectKeys.GROUP_ID, "org.example");
        mojoProperties.put(ProjectKeys.JAVA_VERSION, "11");
        mojoProperties.put(ProjectKeys.SPRING_BOOT_VERSION, "2.3.4.RELEASE");
        mojoProperties.put(ProjectKeys.SPRING_DEPENDENCY_MGMT_VERSION, "1.0.10.RELEASE");
        mojoProperties.put(ProjectKeys.SPRING_CLOUD_VERSION, "2.2.5.RELEASE");
        mojoProperties.put(ProjectKeys.PROBLEM_SPRING_VERSION, "0.26.2");
        return mojoProperties;
    }

    /**
     * Returns the value of {@code key} as a String
     * @param key the key to find
     * @return the value of {@code key}, as a String
     */
    public String getString(@NonNull String key) {
        return (String)this.get(key);
    }

    public static MojoProperties toMojoProperties (Map<String,Object> map) {
        MojoProperties properties = new MojoProperties();
        map.forEach((key, value) -> {
            if (value instanceof String) {
                properties.put(key, value);
            }
        });
        return properties;
    }


    /**
     * The mojo.properties file contains the project-scope properties. There are some properties
     * defined at create-project time that also need to be known at create-endpoint time, such
     * as the base package name.  The mojo.properties thus encapsulates context information that
     * needs to get passed down from the ProjectGenerator to the EndpointGenerator.
     * @return the fully-qualified filename of the mojo.properties file.
     */
    @NonNull static String getMojoPropertiesFileName() {
        return MojoUtils.currentDirectory() + "mojo.properties";
    }
}
