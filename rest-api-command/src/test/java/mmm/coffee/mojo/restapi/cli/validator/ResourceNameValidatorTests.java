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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests
 */
public class ResourceNameValidatorTests {
    
    @ParameterizedTest
    @ValueSource( strings = { "Employee", "employee", "Test_Suite", "xyzzy", "_foobar", "$foo" })
    public void shouldRecognizeValidIdentifiers(String identifier) {
        assertThat( ResourceNameValidator.isValid(identifier)).isTrue();
    }

    @ParameterizedTest
    @ValueSource( strings = { "abstract", "null", "const", "float", "'Hello'", "/Hello" })
    public void shouldRecognizeInvalidIdentifiers(String identifier) {
        assertThat( ResourceNameValidator.isValid(identifier)).isFalse();
    }
}
