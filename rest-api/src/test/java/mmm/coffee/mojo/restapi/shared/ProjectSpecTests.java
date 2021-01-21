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

import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests
 */
class ProjectSpecTests {

    /**
     * A ProjectSpec is created by lombok, so this
     * is only a smoke test of creating the ProjectSpec.
     */
    @Test
    void shouldPopulateFields() {
        ProjectSpec spec = ProjectSpec.builder()
                .applicationName("lesson01")
                .basePackage("org.example")
                .dbmsSchema("testdb")
                .groupId("org.example.lessons")
                .features(new String[] { "postgres" })
                .build();

        assertThat(spec).isNotNull();
        assertThat(spec.getApplicationName()).isNotEmpty();
        assertThat(spec.getBasePackage()).isNotEmpty();
        assertThat(spec.getDbmsSchema()).isNotEmpty();
        assertThat(spec.getGroupId()).isNotEmpty();
        assertThat(spec.getFeatures()).isNotEmpty();
    }
}
