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
package mmm.coffee.mojo.restapi.cli.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.google.common.truth.Truth.assertThat;

/**
 * Test PackageNameValidator
 */
public class PackageNameValidatorTests {

    /**
     * Check whether the PackageNameValidator detects legal and illegal
     * package names.  At the moment, this test "kicks the tires" and
     * does not try to be an exhaustive test of all package name rules.
     */
    @ParameterizedTest
    @CsvSource( value = {
            "org.example,widget",
            "org.example.warehouse",
            "com.example.bookstore"
        })
    public void shouldAcceptValidPackageNames(String packageName) {
        assertThat( PackageNameValidator.isValid(packageName) ).isTrue();
    }

    @ParameterizedTest
    @CsvSource( value = {
            "static.org.example,widget",
            "abstract.org.example.widget",
            "org.example.widget.synchronized"
    })
    public void shouldDetectInvalidPackageNames(String packageName) {
        assertThat( PackageNameValidator.isValid(packageName)).isFalse();
    }
}
