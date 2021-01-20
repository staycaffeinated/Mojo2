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


import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * TODO: Fill me in
 */
public class EndpointPropertyFactoryTests {
    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog().muteForSuccessfulTests();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog().muteForSuccessfulTests();


    Map<String,String> projectProps;

    final String basePackage = "mmm.coffee.sommelier";
    final String basePackagePath = "mmm/coffee/sommelier";

    @BeforeEach
    public void setUpEachTimeime() {
        projectProps = new HashMap<>();
        projectProps.put("basePackage", basePackage);
        projectProps.put("basePackagePath", basePackagePath);
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenResourceArgIsNull() {
        assertThrows(NullPointerException.class, () -> EndpointPropertyFactory.createProperties(null, "/", projectProps));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenRouteArgIsNull() {
        assertThrows(NullPointerException.class, () -> EndpointPropertyFactory.createProperties("Entity", null, projectProps));
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenMapArgIsNull() {
        assertThrows(NullPointerException.class, () -> EndpointPropertyFactory.createProperties("Entity", "/entity", null));
    }

    /**
     * The EntityPropertyFactory has to assemble all the properties that will
     * be passed into the Template, where template expressions are resolved based
     * on the content of the properties map.
     */
    @Test
    public void shouldReturnRequiredProperties() {
        final String resource = "Coffee";
        final String entityVarName = "coffee";
        final String route = "/coffee";

        Map<String,String> props = EndpointPropertyFactory.createProperties(resource, route, projectProps);

        assertThat(props).isNotNull();
        assertThat(props.get("entityName")).isEqualTo(resource);
        assertThat(props.get("entityVarName")).isEqualTo(entityVarName);
        assertThat(props.get("basePath")).isEqualTo(route);
        assertThat(props.get("basePackage")).isEqualTo(basePackage);
        assertThat(props.get("basePackagePath")).isEqualTo(basePackagePath);
    }
}
