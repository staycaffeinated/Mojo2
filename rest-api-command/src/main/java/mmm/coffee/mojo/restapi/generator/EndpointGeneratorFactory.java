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
import mmm.coffee.mojo.restapi.generator.spring.SpringWebFluxEndpointGenerator;
import mmm.coffee.mojo.restapi.generator.spring.SpringWebMvcEndpointGenerator;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;

/**
 * Returns a code generator that produces code appropriate for the underlying framework.
 * For example, if the current project is based on Spring WebMVc, then a SpringWebMvcEndpointGenerator
 * is returned.
 */
public class EndpointGeneratorFactory {

    private EndpointGeneratorFactory() {}

    public static Generator createGenerator(@NonNull SupportedFramework framework) {
        switch(framework) {
            case WEBMVC:
                return new SpringWebMvcEndpointGenerator();
            case WEBFLUX:
                return new SpringWebFluxEndpointGenerator();
            default:
                throw new UnsupportedOperationException(String.format("Internal bug: the EndpointGeneratorFactory encountered an unsupported framework: '%s'", framework.toString()));
        }
    }
}
