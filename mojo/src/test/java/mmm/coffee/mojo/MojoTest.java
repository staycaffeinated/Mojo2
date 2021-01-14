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
package mmm.coffee.mojo;

import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

import java.awt.event.MouseMotionAdapter;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
/**
 * TODO: Fill me in
 */
public class MojoTest {

    MojoApplication application = new MojoApplication();
    CommandLine cli = new CommandLine(application);

    @BeforeEach
    public void setUpEachTime() {
        cli.clearExecutionResults();
    }


    @Test
    public void testUsage() {
        cli.usage(System.out);
    }
    

    /**
     * The cli.execute() command expects a varargs {@code String...} value,
     * so its our responsibility to separate the command-line args
     * into the appropriate String array.
     */
    private String[] toArgV(String s) {
        return s.split("\\s");
    }
}
