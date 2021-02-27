/*
 * Copyright 2021 Jon Caulfield
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
package mmm.coffee.mojo.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
/**
 * Unit test the Dependency class
 */
class DependencyTests {

    Dependency dependencyUnderTest;

    @BeforeEach
    public void setUp() {
        dependencyUnderTest = new Dependency();
        dependencyUnderTest.setName("springBoot");
        dependencyUnderTest.setVersion("2.4.0");
    }

    /**
     * A Dependency is a simple POJO, so we only need to verify the POJO is well-formed
     */
    @Test
    void shouldBeWellFormed() {
        assertThat(dependencyUnderTest.getName()).isNotEmpty();
        assertThat(dependencyUnderTest.getVersion()).isNotEmpty();
    }
}
