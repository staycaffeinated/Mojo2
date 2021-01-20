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

import lombok.NonNull;
import mmm.coffee.mojo.exception.MojoException;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * This flavor of SourceSink writes the content to the given file.
 * The subdirectories of the file are created as needed.
 */
public class DefaultTemplateWriter implements TemplateWriter {

    /**
     * Writes the {@code content} to the {@code file}. The subdirectories
     * of {@code file} is created as needed.
     *
     * If {@code content} is {@code null}, this method returns without doing anything.
     *
     * @param file the file to which the content is written
     * @param content the content to to write
     */
    @Override
    public void writeStringToFile(@NonNull File file, String content) {
        if (Objects.isNull(content))
            return;

        try {
            FileUtils.forceMkdirParent(file);
            FileUtils.writeStringToFile(file, content, StandardCharsets.UTF_8); }
        catch (IOException ioe) {
            throw new MojoException(ioe.getMessage(), ioe);
        }
    }
}
