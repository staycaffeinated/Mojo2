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
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests
 */
class TemplateCatalogTests {

    TemplateCatalog templateCatalog;

    @BeforeEach
    void setUp() throws IOException {
        templateCatalog = new TemplateCatalog("/test-catalog.yml");
    }

    @Test
    void shouldReturnWellFormedObject() {
        templateCatalog = new TemplateCatalog();
        assertThat(templateCatalog).isNotNull();
    }

    @Test
    void shouldAppendCatalog() {
        // given
        TemplateCatalog mainCatalog = new TemplateCatalog();
        TemplateCatalog secondCatalog = new TemplateCatalog();
        TemplateCatalog thirdCatalog = new TemplateCatalog();

        // when
        mainCatalog.append(secondCatalog).append(thirdCatalog);

        // then
        assertThat(mainCatalog).isNotNull();
    }

    @Test
    void shouldReturnProjectEntries() {
        List<CatalogEntry> entries = templateCatalog.filterByContext("project");
        assertThat(entries).isNotEmpty();
    }

    @Test
    void whenCatalogNameIsNull_expectNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TemplateCatalog(null));
    }

    @Test
    void shouldReturnEmptyListIfNoEntriesMatchContext() {
        List<?> entries = templateCatalog.filterByContext("no-such-context");
        assertThat(entries).isNotNull();
        assertThat(entries).isEmpty();
    }

    @Test
    void shouldNotAllowAppendingNullCatalogs() {
        TemplateCatalog catalog = new TemplateCatalog();
        assertThrows(NullPointerException.class, () -> catalog.append(null));
    }

    @Test
    void whenFilterByNameIsNull_expectNullPointerException() {
        TemplateCatalog catalog = new TemplateCatalog();
        assertThrows(NullPointerException.class, () -> catalog.filterByNameLike(null));
    }

    @Test
    void whenFilterByContextIsNull_expectNullPointerException() {
        TemplateCatalog catalog = new TemplateCatalog();
        assertThrows(NullPointerException.class, () -> catalog.filterByContext(null));
    }
}
