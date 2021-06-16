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


import mmm.coffee.mojo.restapi.generator.ProjectKeys;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * Integration tests of the MojoProperties class.
 * These tests read and write to the file system.
 */
class MojoPropertiesIT {

    @AfterEach
    public void tearDown() {
        // Remove any mojo.properties file left behind by these tests
        File f = new File(MojoProperties.DEFAULT_FILE_NAME);
        FileUtils.deleteQuietly(f);
    }

    @Test
    void shouldSaveAndLoadMojoProperties() {
        // set up sample values that will be saved to mojo.properties
        final String basePackageKey = ProjectKeys.BASE_PACKAGE;
        final String basePackageValue = "org.example.testing.widget";

        final String basePathKey = ProjectKeys.BASE_PATH;
        final String basePathValue = "/widgets/api/v1";

        final String frameworkKey = ProjectKeys.FRAMEWORK;
        final String frameworkValue = SupportedFramework.WEBMVC.toString();

        Map<String,Object> map = new HashMap<>();
        map.put(basePackageKey, basePackageValue);
        map.put(basePathKey, basePathValue);
        map.put(frameworkKey, frameworkValue);

        MojoProperties.saveConfiguration(map);

        Configuration configuration = new MojoProperties().getConfiguration();
        assertThat(configuration).isNotNull();
        assertThat(configuration.getString(basePackageKey)).isEqualTo(basePackageValue);
        assertThat(configuration.getString(basePathKey)).isEqualTo(basePathValue);
        assertThat(configuration.getString(frameworkKey)).isEqualTo(frameworkValue);
    }

    /**
     * When a mojo.properties file does not exist, the Configuration is
     * populated using environment variables. The EnvironmentConfiguration
     * created by commons-configuration is an immutable config. Under the
     * covers, the MojoProperties class returns a mutable configuration.
     * This test ensures that a mutable configuration is returned
     */
    @Test
    void shouldProvideMutableConfigWhenReadingFromEnvironment() throws Exception {
        // Test data
        final String basePackageKey = ProjectKeys.BASE_PACKAGE;
        final String basePackageValue = "org.example.testing.widget";

        final String basePathKey = ProjectKeys.BASE_PATH;
        final String basePathValue = "/widgets/api/v1";

        final String frameworkKey = ProjectKeys.FRAMEWORK;
        final String frameworkValue = SupportedFramework.WEBMVC.toString();

        updateEnv(basePackageKey, basePackageValue);
        updateEnv(basePathKey, basePathValue);
        updateEnv(frameworkKey, frameworkValue);

        Configuration config = new MojoProperties().getConfiguration();

        // If the configuration is immutable, the following will throw an exception
        config.setProperty("foo", "bar");

        // Confirm the setProperty set the value
        assertThat(config.getString("foo")).isEqualTo("bar");
    }

    @SuppressWarnings({ "unchecked" })
    public static void updateEnv(String name, String val) throws ReflectiveOperationException {
        Map<String, String> env = System.getenv();
        Field field = env.getClass().getDeclaredField("m");
        field.setAccessible(true);
        ((Map<String, String>) field.get(env)).put(name, val);
    }
}
