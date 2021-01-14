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

import java.io.File;

/**
 * A SourceSink that does not write any output. This is useful for testing
 * when a SourceSink is needed but you do not want content (e.g., generated code)
 * written to the file system.
 */
public class NoOpTemplateWriter implements TemplateWriter {

    @Override
    public void writeStringToFile(@NonNull File file, String content) {
        // For debugging, we'll write to stdout
        // System.out.println("NoOpSourceSink::writeStringToFile");
        // System.out.printf("Filename: path: %s", file.getPath(), " name: %s", file.getName());
        // System.out.printf("%n%s%n", content);
    }
}
