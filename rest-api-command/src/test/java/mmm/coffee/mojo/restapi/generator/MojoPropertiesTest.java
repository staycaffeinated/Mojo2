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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests
 */
public class MojoPropertiesTest {

    @Nested
    class Test_getMojoPropertiesFileName {
        @Test
        void shouldNotReturnNull() {
            assertThat(MojoProperties.getMojoPropertiesFileName()).isNotNull();
        }

        @Test
        void shouldReturnMojoPropertiesAheFilename() {
            assertThat(MojoProperties.getMojoPropertiesFileName()).endsWith("mojo.properties");
        }
    }

    @Nested
    class Test_loadPropertiesForDryRun {
        /**
         * This tests for keys that must be present for generate-endpoint to work.
         */
        @Test
        void shouldContainRequiredKeys() {
            MojoProperties properties = MojoProperties.loadMojoPropertiesForDryRun();
            assertThat(properties).isNotEmpty();
            assertThat( properties.get(ProjectKeys.BASE_PACKAGE)).isNotNull();
            assertThat( properties.get(ProjectKeys.BASE_PATH)).isNotNull();
            assertThat( properties.get(ProjectKeys.BASE_PACKAGE_PATH)).isNotNull();
        }
    }
}
