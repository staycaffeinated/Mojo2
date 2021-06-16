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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit test MojoConfiguration
 */
class MojoConfigurationTests {

    /**
     * Mojo allows project properties to be defined in env vars
     * (primarily for testing independent of a mojo.props file).
     * @throws Exception when things go wrong
     */
    @Test
    void shouldSupportEnvVars() throws Exception {
        updateEnv("mojo.basePath", "/dashboard");
        updateEnv("mojo.basePackage", "org.example.hello");

        // ensure the property made it to env var
        assertThat(System.getenv("mojo.basePath")).isNotNull();
    }

    /**
     * Verify the basics of adding a property and reading the property back work
     */
    @Test
    void shouldSupportCustomSettings() throws Exception {
        final String pathKey = "mojo.basePath";
        final String pathValue = "/dashboard";
        final String packageKey = "mojo.basePackage";
        final String packageValue = "org.example.hello";

        MojoConfiguration configuration = new MojoConfiguration();
        configuration.getConfiguration().addProperty(pathKey, pathValue);
        configuration.getConfiguration().addProperty(packageKey, packageValue);

        assertThat(configuration.getConfiguration().getString(pathKey)).isEqualTo(pathValue);
        assertThat(configuration.getConfiguration().getString(packageKey)).isEqualTo(packageValue);
    }

    @Test
    void shouldSupportOtherStuff() throws Exception {
        MojoConfiguration config = new MojoConfiguration();
        config.getConfiguration().addProperty("mojo.basePath", "/dashboard");
        config.getConfiguration().addProperty("mojo.basePackage", "org.example.hello");
    }
    
    /**
     * Adds {@code newenv} as environment variables; suitable for unit testing.
     * Borrowed from StackOverflow as a way to set env vars for unit testing:
     * See: https://stackoverflow.com/questions/318239/how-do-i-set-environment-variables-from-java
     */
    @SuppressWarnings("unchecked")
    protected static void setEnv(Map<String, String> newenv) throws Exception {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for(Class cl : classes) {
                if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
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

    @SuppressWarnings({ "unchecked" })
    public static void updateEnv(String name, String val) throws ReflectiveOperationException {
        Map<String, String> env = System.getenv();
        Field field = env.getClass().getDeclaredField("m");
        field.setAccessible(true);
        ((Map<String, String>) field.get(env)).put(name, val);
    }
}
