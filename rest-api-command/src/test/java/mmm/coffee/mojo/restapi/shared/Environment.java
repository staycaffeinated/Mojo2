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
package mmm.coffee.mojo.restapi.shared;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

/**
 * This class provides methods for setting environment variables
 * Some parts are borrowed from StackOverflow as a way to set env vars for unit testing:
 *  https://stackoverflow.com/questions/318239/how-do-i-set-environment-variables-from-java
 */
public class Environment {

    /**
     * Adds an environment variable
     */
    @SuppressWarnings({"unchecked"})
    public static void addVariable(@NonNull String name, @NonNull String val) throws ReflectiveOperationException {
        Map<String, String> env = System.getenv();
        Field field = env.getClass().getDeclaredField("m");
        field.setAccessible(true);
        ((Map<String, String>) field.get(env)).put(name, val);
    }

    /**
     * Defines all environment variable values. The values in {@code newenv} define the complete
     * scope of environment variables that will exist after this method executes.
     *
     * @param newenv the complete list of environment variables to exist
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    protected static void setAllVariables(Map<String, String> newenv) throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            Class<?>[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class<?> cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newenv);
                }
            }
        }
    }
}
