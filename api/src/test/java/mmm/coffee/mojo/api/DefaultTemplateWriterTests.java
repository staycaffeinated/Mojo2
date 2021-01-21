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
package mmm.coffee.mojo.api;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests of DefaultTemplateWriter
 */
class DefaultTemplateWriterTests {

    File scratchFile;
    DefaultTemplateWriter writer;

    @BeforeEach
    public void setUp() throws IOException {
        scratchFile = File.createTempFile("scratch", "java");
        scratchFile.createNewFile();
        writer = new DefaultTemplateWriter();
    }

    @Test
    void shouldWriteToFile() throws IOException {
        final String expectedContent = "hello, world";
        writer.writeStringToFile(scratchFile, expectedContent);
        String content = FileUtils.readFileToString(scratchFile, StandardCharsets.UTF_8);
        assertThat(content).isEqualTo(expectedContent);
    }

    @Test
    void shouldDisallowNullFile() {
        assertThrows(NullPointerException.class, () -> writer.writeStringToFile(null, "foo"));
    }

    /**
     * If the content given to write to the file is null,
     * the target file should be empty.
     */
    @Test
    void shouldDoNothingWithNullContent() {
        writer.writeStringToFile(scratchFile, null);
        assertThat(scratchFile.length()).isEqualTo(0);
    }
}
