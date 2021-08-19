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
package mmm.coffee.mojo.mixin;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit test of DryRunOption class
 */
class DryRunOptionTests {

    @Test
    void shouldReturnTrue() {
        // given
        DryRunOption optionUnderTest = new DryRunOption();
        optionUnderTest.dryRun = true;

        // then
        assertThat(optionUnderTest.isDryRun()).isTrue();
    }
    @Test
    void shouldReturnFalse() {
        // given
        DryRunOption optionUnderTest = new DryRunOption();
        optionUnderTest.dryRun = false;

        // then
        assertThat(optionUnderTest.isDryRun()).isFalse();
    }
}
