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

import lombok.NonNull;
import lombok.Synchronized;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.BasicConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 * Mojo configuration properties
 */
public class MojoConfiguration  {
    public static final String DEFAULT = "mojo.properties";

    private Configuration configuration;
    
    public MojoConfiguration() {
        try {
            this.configuration = readConfiguration(DEFAULT);
        }
        catch (ConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    /**
     * Returns the configuration found at the given filename or resource
     * @return configuration values found in the mojo.properties file
     */
    private Configuration readConfiguration(@NonNull String fileNameOrUrlOrClasspath) throws ConfigurationException {
        Parameters params = new Parameters();
        BasicConfigurationBuilder<PropertiesConfiguration> builder = new BasicConfigurationBuilder<>(PropertiesConfiguration.class)
                .configure(params.fileBased().setFileName(fileNameOrUrlOrClasspath));
        return builder.getConfiguration();
    }
}
