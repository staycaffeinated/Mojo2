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

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Unit tests of MojoUtils
 */
class MojoUtilsTests {

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog().muteForSuccessfulTests();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog().muteForSuccessfulTests();


    /**
     * Some tests write/read the mojo.properties file to ensure that code path works.
     * We want to clean up any remnant mojo.properties files created by these tests.
     */
    @AfterEach
    void cleanUp() {
        File file = new File (MojoUtils.getMojoPropertiesFileName());
        if (file.exists())
            FileUtils.deleteQuietly(file);
    }


    @Nested
    class Test_convertPackageNameToPath {
        @Test
        void shouldThrowNullPointerExceptionWhenArgIsNull() {
            assertThrows(NullPointerException.class, () -> {
                MojoUtils.convertPackageNameToPath(null);
            });
        }

        @ParameterizedTest
        @CsvSource(value = {
                "org.example.widget,    org/example/widget",
                "org.example,           org/example",
                "org,                   org"
        })
        void shouldConvertPackageNameToEquivalentPath(String testValue, String expectedValue) {
            assertThat(MojoUtils.convertPackageNameToPath(testValue)).isEqualTo(expectedValue);
        }

        @Test
        void shouldConvertEmptyStringToEmptyString() {
            assertThat(MojoUtils.convertPackageNameToPath("")).isEqualTo("");
        }
    }

    @Nested
    class Test_getMojoFileName {
        @Test
        void shouldNotReturnNull() {
            assertThat(MojoUtils.getMojoPropertiesFileName()).isNotNull();
        }
    }

    @Nested
    class Test_getCurrentDirectory {
        @Test
        void shouldNotReturnNull() {
            assertThat(MojoUtils.currentDirectory()).isNotNull();
        }
    }

    @Nested
    class Test_getPackageNameForResource {
        @Test
        void shouldTranslatePackageNameToItsEquivalentFilePath() {
            // happy path
            assertThat(MojoUtils.convertPackageNameToPath("mmm.coffee.example")).isEqualTo("mmm/coffee/example");

            // with only one package
            assertThat(MojoUtils.convertPackageNameToPath("org")).isEqualTo("org");

            // with empty package name
            assertThat(MojoUtils.convertPackageNameToPath("")).isEqualTo("");
        }

        @Test
        void shouldDisallowNullArgument() {
            assertThrows(NullPointerException.class, () -> MojoUtils.convertPackageNameToPath(null));
        }
    }

    @Nested
    class Test_loadContext {
        @Test
        void shouldSaveAndLoadMojoFile() {
            final String key = "springBoot.version";
            final String value = "2.2.4.RELEASE";

            Map<String, Object> properties = new HashMap<>();
            properties.put(key, value);
            MojoUtils.saveMojoProperties(properties);
            Map<String, String> results = MojoUtils.loadMojoProperties();
            assertThat(results).isNotEmpty();
            assertThat(results.get(key)).isEqualTo(value);
        }
    }
}
