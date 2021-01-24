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
import mmm.coffee.mojo.mixin.DryRunOption;

import java.util.Map;

/**
 * Defines the Generator stereotype
 */
public interface Generator {

    void initialize();

    void configure(Map<String,Object> properties);

    void outputStrategy(TemplateWriter sourceSink);

    void generate();

    /**
     * Convenience method to enable clients of Generators to run the
     * generator without having to explicitly call the life cycle methods.
     * @param properties the properties consumed by the generator to resolve template values
     *                   and other variables
     */
    default void run(@NonNull Map<String,Object> properties) {
        if ( ((Boolean)properties.getOrDefault(DryRunOption.DRY_RUN_KEY, Boolean.FALSE)).booleanValue()) {
            run(properties, new NoOpTemplateWriter());
        } else {
            run(properties, new DefaultTemplateWriter());
        }
    }

    default void run(@NonNull Map<String,Object> properties, @NonNull TemplateWriter sourceSink) {
        initialize();
        configure(properties);
        outputStrategy(sourceSink);
        generate();
    }
}
