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
package mmm.coffee.mojo.restapi.generator.helpers;

import lombok.NonNull;

/**
 * A collection of helper methods
 */
public class MojoUtils {

    private MojoUtils() {}

    
    /**
     * Returns the current working directory
     * @return the current working directory, ending with a front-slash, such as "some/path/";
     */
    @NonNull public static String currentDirectory() {
        return System.getProperty("user.dir") + "/"; // the JVM guarantees this property has a value
    }

    @NonNull
    public static String convertPackageNameToPath(@NonNull String packageName) { return packageName.replace(".", "/");
    }
}
