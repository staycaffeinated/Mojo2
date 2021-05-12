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
package mmm.coffee.mojo.api.library;

import mmm.coffee.mojo.library.Dependency;
import mmm.coffee.mojo.library.DependencyCatalogReader;
import mmm.coffee.mojo.restapi.generator.Catalogs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/**
 * Tests of DependencyCatalogReader class
 */
class DependencyCatalogReaderIT {

    DependencyCatalogReader readerUnderTest;

    @BeforeEach
    public void setUp() {
        readerUnderTest = new DependencyCatalogReader();
    }

    /**
     * Verify the content of the production dependency catalog is well-formed.
     * This test helps catch errors in the dependencies.yaml file.
     *
     * @throws Exception for the usual reasons
     */
    @Test
    void shouldBeWellFormedCatalog() throws Exception {
        List<Dependency> entries = readerUnderTest.readLibraryCatalog(Catalogs.THIRD_PARTY_LIBRARY_CATALOG);

        assertThat(entries).isNotEmpty();

        entries.forEach( item ->  {
            assertThat (item.getName()).isNotEmpty();
            assertThat (item.getVersion()).isNotEmpty();
        });
    }
}
