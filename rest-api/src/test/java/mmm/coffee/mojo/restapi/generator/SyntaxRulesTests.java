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

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests of SyntaxRules class
 */
public class SyntaxRulesTests {
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog().muteForSuccessfulTests();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog().muteForSuccessfulTests();


    @Nested
    public class EntityNameSyntaxTests {
        /**
         * Contract: the first character of the entityName must be capitalized.
         * All other characters are left as-is.
         *
         * @param sample the test string to evaluate
         * @param expected the expected outcome
         */
        @ParameterizedTest
        @CsvSource( {
                // test data, expected result
                "foo,       Foo",
                "Foo,       Foo",
                "fooBar,    FooBar"
        } )
        public void shouldReturnEntityNameStartingWithUpperCaseLetter(String sample, String expected) {
            assertThat(SyntaxRules.entityNameSyntax(sample)).isEqualTo(expected);
        }

        @Test
        public void shouldThrowNullPointerExceptionWhenArgIsNull() {
            assertThrows(NullPointerException.class, () -> SyntaxRules.entityNameSyntax(null));
        }
    }

    @Nested
    public class EntityVarNameSyntaxTests {
        /**
         * Contract: the first character of the {@code resource} is converted to lower-case
         * (i.e., uncapitalized). All other characters remain as-is.
         *
         * @param sample the test value to convert
         * @param expected the expected outcome
         */
        @ParameterizedTest
        @CsvSource({
                "FooBar,    fooBar",
                "FOOBAR,    fOOBAR",
                "WineGlass, wineGlass",
                "Wineglass, wineglass"
        })
        public void shouldReturnCamelCaseStartingWithLowerCaseLetter(String sample, String expected) {
            assertThat(SyntaxRules.entityVarNameSyntax(sample)).isEqualTo(expected);
        }

        @Test
        public void shouldThrowNullPointerExceptionWhenArgIsNull() {
            assertThrows(NullPointerException.class, () ->  SyntaxRules.entityVarNameSyntax(null));
        }
    }

    @Nested
    public class BasePathSyntaxTests {

        @ParameterizedTest
        @CsvSource({
                "FooBar,        /foobar",
                "FOOBAR,        /foobar",
                "WineGlass,     /wineglass",
                "Wineglass,     /wineglass",
                "/beer,         /beer",
                "/WineGlass,    /wineglass"
        })
        public void shouldReturnBasePathWithStartingSlashAndLowerCase(String sample, String expected) {
            assertThat(SyntaxRules.basePathSyntax(sample)).isEqualTo(expected);
        }

        @Test
        public void shouldThrowNullPointerExceptionWhenArgIsNull() {
            assertThrows(NullPointerException.class, () -> SyntaxRules.basePathSyntax(null));
        }
    }
}
