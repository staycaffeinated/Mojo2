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

import mmm.coffee.mojo.restapi.generator.ProjectKeys;
import mmm.coffee.mojo.restapi.shared.MojoProperties;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import org.apache.commons.configuration2.Configuration;
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
import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
/**
 * Unit tests of MojoUtils
 */
class MojoUtilsIT {

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog().muteForSuccessfulTests();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog().muteForSuccessfulTests();


    /**
     * Some tests write/read the mojo.properties file to ensure some code paths work.
     * We want to clean up any remnant mojo.properties files created by these tests.
     */
    @AfterEach
    void cleanUp() {
        File file = new File (MojoProperties.DEFAULT_FILE_NAME);
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
            String value = MojoUtils.getPackageNameForResource("org.example", "Account");
            assertThat(value).isNotEmpty();

            // per the naming convention, it should be this:
            assertThat(value).isEqualTo("org.example.endpoint.account");
        }

        @Test
        void shouldDisallowNullBasePackage() {
            assertThrows(NullPointerException.class, () -> MojoUtils.getPackageNameForResource(null, "widget"));
        }
        @Test
        void shouldDisallowNullResourceName() {
            assertThrows(NullPointerException.class, () -> MojoUtils.getPackageNameForResource("org.example", null));
        }
    }

}
