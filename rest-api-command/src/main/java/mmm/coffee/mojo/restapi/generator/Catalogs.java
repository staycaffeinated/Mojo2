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
package mmm.coffee.mojo.restapi.generator;

/**
 * This class defines the resource paths of the various template catalogs.
 */
public class Catalogs {
    
    private Catalogs() {}

    /**
     * Common-stuff contains general artifacts, such as the README template
     */
    public static final String COMMON_CATALOG = "/restapi/catalogs/common-stuff.yml";

    /**
     * spring-gradle contains the templates for the **.gradle files
     */
    public static final String GRADLE_CATALOG = "/restapi/catalogs/spring-gradle.yml";

    /**
     * This file contains the 3rd-party library versions; this does not contain any templates
     */
    public static final String THIRD_PARTY_LIBRARY_CATALOG = "/restapi/catalogs/dependencies.yml";

    /**
     * These are the templates rendered for a Spring WebMVC project
     */
    public static final String WEBMVC_CATALOG = "/restapi/catalogs/spring-webmvc.yml";

    /**
     * These are the templates rendered for a Spring Webflux project
     */
    public static final String WEBFLUX_CATALOG = "/restapi/catalogs/spring-webflux.yml";
}