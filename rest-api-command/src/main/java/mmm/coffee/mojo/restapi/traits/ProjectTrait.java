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
package mmm.coffee.mojo.restapi.traits;

/**
 * Common project-level properties needed by the code generator
 */
public interface ProjectTrait extends FrameworkTrait, BaseRouteTrait, BasePackageTrait {
    /**
     * Returns the group-id of this artifact. This value maps to the library's
     * repository coordinates
     */
    String getGroupId();

    /**
     * The artifact name, or application name. This also becomes part of the artifact's
     * repository coordinates.
     */
    String getApplicationName();
}
