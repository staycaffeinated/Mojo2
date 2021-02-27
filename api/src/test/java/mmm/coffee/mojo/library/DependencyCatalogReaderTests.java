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
package mmm.coffee.mojo.library;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests of DependencyCatalogReader class
 */
public class DependencyCatalogReaderTests {

    private final static String TEST_CATALOG = "/test-dependencies.yaml";

    DependencyCatalogReader readerUnderTest;

    @BeforeEach
    public void setUp() {
        readerUnderTest = new DependencyCatalogReader();
    }

    @Test
    void shouldDisallowNullCatalog() {
        assertThrows (NullPointerException.class,() ->  readerUnderTest.readLibraryCatalog(null));
    }

    /**
     * Reads the dependency catalog and confirms each entry has a name and version
     * @throws Exception for the usual reasons
     */
    @Test
    public void shouldBeWellFormedCatalog() throws Exception {
        List<Dependency> entries = readerUnderTest.readLibraryCatalog(TEST_CATALOG);

        assertThat(entries).isNotEmpty();

        entries.forEach( item ->  {
            assertThat (item.getName()).isNotEmpty();
            assertThat (item.getVersion()).isNotEmpty();
        });
    }
}
