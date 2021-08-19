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

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests
 */
class SupportedFrameworkTests {

    @Test
    void shouldReturnWebMvc() {
        var result = SupportedFramework.convert("webmvc");
        assertThat(result).isEqualTo(SupportedFramework.WEBMVC);
    }

    @Test
    void shouldReturnWebFlux() {
        var result = SupportedFramework.convert("webflux");
        assertThat(result).isEqualTo(SupportedFramework.WEBFLUX);
    }

    @Test
    void shouldReturnUndefined() {
        var result = SupportedFramework.convert("webflux_");
        assertThat(result).isEqualTo(SupportedFramework.UNDEFINED);
    }

    @Test
    void shouldHandleMixedCase() {
        var result = SupportedFramework.convert("WebMVC");
        assertThat(result).isEqualTo(SupportedFramework.WEBMVC);

        result = SupportedFramework.convert("WEBFLUX");
        assertThat(result).isEqualTo(SupportedFramework.WEBFLUX);
    }
}
