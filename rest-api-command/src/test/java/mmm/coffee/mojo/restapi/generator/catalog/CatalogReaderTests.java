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
package mmm.coffee.mojo.restapi.generator.catalog;

import mmm.coffee.mojo.catalog.CatalogReader;
import mmm.coffee.mojo.restapi.generator.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests of CatalogReader
 */
class CatalogReaderTests {

    CatalogReader reader;

    @BeforeEach
    public void setUp() {
        reader = new CatalogReader();
    }

    @Test
    void shouldReadTemplateCatalogSuccessfully() throws Exception {
        List<?> entries = reader.readCatalog(Constants.TEMPLATE_CATALOG);
        assertThat(entries).isNotNull();
        assertThat(entries).isNotEmpty();
    }

    @Test
    void shouldThrowNullPointerExceptionWhenArgIsNull() {
        assertThrows(NullPointerException.class, () -> reader.readCatalog(null));
    }
}
