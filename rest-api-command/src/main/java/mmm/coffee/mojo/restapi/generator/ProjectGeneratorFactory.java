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


import lombok.NonNull;
import mmm.coffee.mojo.api.Generator;
import mmm.coffee.mojo.restapi.generator.spring.SpringWebFluxProjectGenerator;
import mmm.coffee.mojo.restapi.generator.spring.SpringWebMvcProjectGenerator;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;

/**
 * Creates a Generator for various frameworks. For example, if you
 * want to create a project that uses the Spring WebMVC framework, this
 * factory will return a SpringWebMvcProjectGenerator. If you want
 * to create a project that uses the Spring WebFlux framework, this
 * factory will return a SpringWebFluxProjectGenerator.
 */
public class ProjectGeneratorFactory {

    private ProjectGeneratorFactory() {}

    /**
     * Returns a ProjectGenerator instance appropriate for the given {@code framework}.
     *
     * @param framework identifies the desired framework, such as 'webmvc' or 'webflux'
     */
    @SuppressWarnings("java:S1301") // the switch will grow over time as micronaut and other frameworks are added
    public static Generator createProjectGenerator(@NonNull SupportedFramework framework) {
        switch (framework) {
            case WEBMVC:
                return new SpringWebMvcProjectGenerator();
            case WEBFLUX:
                return new SpringWebFluxProjectGenerator();
        }
        throw new IllegalArgumentException(String.format("Internal bug: missing ProjectGenerator for this framework: %s", framework.toString()));
    }
}