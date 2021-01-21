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

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests
 */
public class TemplateCatalogTests {

    TemplateCatalog templateCatalog;

    @BeforeEach
    void setUp() throws IOException {
        templateCatalog = new TemplateCatalog("/test-catalog.yml");
    }

    @Test
    void shouldReturnProjectEntries() {
        List<CatalogEntry> entries = templateCatalog.filterByContext("project");
        assertThat(entries).isNotEmpty();
    }

    @Test
    void shouldDisallowNullCatalogName() {
        assertThrows(NullPointerException.class, () -> new TemplateCatalog(null));
    }

    @Test
    void shouldReturnEmptyListIfNoEntriesMatchContext() {
        List<?> entries = templateCatalog.filterByContext("no-such-context");
        assertThat(entries).isNotNull();
        assertThat(entries).isEmpty();
    }
}
