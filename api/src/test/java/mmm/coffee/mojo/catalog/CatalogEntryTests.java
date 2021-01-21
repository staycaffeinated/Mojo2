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
package mmm.coffee.mojo.catalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests
 */
class CatalogEntryTests {

    CatalogEntry entry;

    @BeforeEach
    void setUp() {
        entry = new CatalogEntry();
        entry.setFeature("postgres");   // indicates this template applies to postgresql integration
        entry.setContext("project");    // indicates this template is consumed when a project is created (create-project command)
        entry.setDestination("src/main/java/Main.java");    // indicates the destination of the rendered template
        entry.setTemplate("/main/Main.ftl");    // indicates the template file
    }

    /**
     * A CatalogEntry is a POJO, so our validation
     * merely verifies the fields are populated.
     */
    @Test
    void shouldBeWellFormed() {
        assertThat(entry.getContext()).isNotNull();
        assertThat(entry.getTemplate()).isNotNull();
        assertThat(entry.getDestination()).isNotNull();
        assertThat(entry.getFeature()).isNotNull();

        assertThat(entry.isFeatureIndependent()).isFalse(); // this template only applies to Postgres integration
        assertThat(entry.hasFeature("postgres")).isTrue();
    }

    @Test
    void shouldHandleEmptyAndNullFeature() {
        assertThat(entry.hasFeature("")).isFalse();
        assertThat(entry.hasFeature(null)).isFalse();
    }
}
