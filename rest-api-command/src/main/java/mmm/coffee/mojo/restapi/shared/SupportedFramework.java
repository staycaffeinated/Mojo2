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
package mmm.coffee.mojo.restapi.shared;

import lombok.NonNull;

import java.util.Locale;
import java.util.function.Supplier;

/**
 * This indicates whether the generated project will
 * use the spring-mvc libraries or spring-webflux libraries.
 * Spring-MVC is used for blocking (synchronous) APIs as well as JDBC and JPA-based applications.
 * Spring-WebFlux uses the Spring reactive library for non-blocking (asynchronous) APIS
 */
public enum SupportedFramework {
    WEBMVC("webmvc"),
    WEBFLUX("webflux"),
    UNDEFINED ("undefined");    // this is for testing


    // This is the value an end-user enters on the command line.
    private final String modelName;

    SupportedFramework(String name) { this.modelName = name; }

    @Override
    public String toString() { return modelName; }
    
    public static SupportedFramework convert(@NonNull String s) {
        if (s.toLowerCase(Locale.ROOT).equals(WEBMVC.modelName)) return WEBMVC;
        if (s.toLowerCase(Locale.ROOT).equals(WEBFLUX.modelName)) return WEBFLUX;
        return UNDEFINED;
    }
}
