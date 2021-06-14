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

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import lombok.NonNull;

import java.util.Map;

/**
 * A collection of methods to convert mustache expressions into a resolved value.
 * For example, given the key/value "basePackage=org.example.projectx", and given
 * the mustache expression "{{basePackage}}",
 */
public class MustacheExpressionResolver {

    private MustacheExpressionResolver() {}

    /**
     * Resolves the given {@code mustacheExpression} into a String.
     * For example, given the key/value "basePackage=org.example.projectx", and given
     * the mustache expression "{{basePackage}}", this method returns {@code "org.example.projectx"}.
     */
    public static @NonNull String toString(@NonNull String mustacheExpression, Map<String,Object> values) {
        Template template = Mustache.compiler().compile(mustacheExpression);
        return template.execute(values);
    }
}
