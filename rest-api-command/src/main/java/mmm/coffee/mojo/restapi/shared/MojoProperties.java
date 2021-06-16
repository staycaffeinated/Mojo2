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

import mmm.coffee.mojo.exception.MojoException;
import mmm.coffee.mojo.restapi.generator.ProjectKeys;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ConfigurationUtils;
import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Mojo configuration properties
 */
public class MojoProperties {
    public static final String DEFAULT_FILE_NAME = "mojo.properties";

    private final Configuration configuration;
    
    public MojoProperties() {
        this.configuration = readConfigurationFile();
    }

    public static void saveConfiguration(Map<String, Object> lexicalScope) {
        try {
            var config = new PropertiesConfiguration();

            // Only copy properties needed for endpoint generation
            config.setHeader("This property file was created by the Mojo code generator.\nThese values are consumed when creating endpoints");
            config.setProperty(ProjectKeys.BASE_PATH, lexicalScope.get(ProjectKeys.BASE_PATH));
            config.setProperty(ProjectKeys.BASE_PACKAGE, lexicalScope.get(ProjectKeys.BASE_PACKAGE));
            config.setProperty(ProjectKeys.FRAMEWORK, lexicalScope.get(ProjectKeys.FRAMEWORK));

            writeConfiguration(config);
        }
        catch (Exception ex) {
            throw new MojoException(ex.getMessage(), ex);
        }
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    /**
     * Returns the configuration found at the given filename or resource
     * @return configuration values found in the mojo.properties file
     */
    private Configuration readConfigurationFile() {
        try {
            var params = new Parameters();
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder = new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                    .configure(params.fileBased().setFileName(DEFAULT_FILE_NAME));
            return builder.getConfiguration();
        }
        catch (ConfigurationException ex) {
            // When the mojo.properties file does not exist, fall back to using
            // environment variables. The motivation is to enable tests to
            // consume mojo properties without having to conjure a temporary mojo.properties file.
            var config = new PropertiesConfiguration();
            var immutableEnv = new EnvironmentConfiguration();
            ConfigurationUtils.copy(immutableEnv, config);
            return config;
        }
    }

    /**
     * Write the the configuration to the mojo.properties file
     */
    private static void writeConfiguration(PropertiesConfiguration configuration) throws IOException, ConfigurationException {
        var file = new File(DEFAULT_FILE_NAME);
        try (var fw = new FileWriter(file)) {
            configuration.write(fw);
            fw.flush();
        }
    }
}
