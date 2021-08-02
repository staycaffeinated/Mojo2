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
package mmm.coffee.mojo.restapi.generator.project;

import mmm.coffee.mojo.api.Generator;
import mmm.coffee.mojo.api.NoOpTemplateWriter;
import mmm.coffee.mojo.api.TemplateWriter;
import mmm.coffee.mojo.mixin.DryRunOption;
import mmm.coffee.mojo.restapi.generator.project.ProjectGeneratorFactory;
import mmm.coffee.mojo.restapi.generator.project.ProjectKeys;
import mmm.coffee.mojo.restapi.shared.SupportedFeatures;
import mmm.coffee.mojo.restapi.shared.SupportedFramework;
import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertThat;

/**
 * Unit tests of ProjectGenerator.
 */
class ProjectGeneratorTests {

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog().muteForSuccessfulTests();

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog().muteForSuccessfulTests();

    private Generator projectGenerator;

    @BeforeEach
    public void setUp() {
        projectGenerator = ProjectGeneratorFactory.createProjectGenerator(SupportedFramework.WEBMVC);
    }

    /**
     * A happy path test case where the minimum parameters that would be given on the
     * command line are provided to the ProjectGenerator, and we verify the
     * ProjectGenerator can indeed produce the project assets with those minimum parameters.
     */
    @Test
    void shouldGenerateProjectBasedOnProperties() {

        Map<String,Object> projectSpec = new HashMap<>();
        projectSpec.put(ProjectKeys.BASE_PACKAGE, "com.example.app");
        projectSpec.put(ProjectKeys.APPLICATION_NAME, "mini-service");
        projectSpec.put(ProjectKeys.BASE_PATH, "/examples");
        projectSpec.put(DryRunOption.DRY_RUN_KEY, Boolean.TRUE);

        TemplateWriter writer = new NoOpTemplateWriter();
        // The run method has a void return-type, so there's nothing to check
        // without scanning the file system for generated assets
        projectGenerator.run(projectSpec, null);
        assertThat(writer).isNotNull(); }

    /**
     * Verify the use case of the user not using the '--support' flag, which
     * adds supported dependencies to the generated code.
     *
     * Expected behavior: the configuration map does not contain the 'features' key.
     */
    @Test
    void shouldContainNoFeaturesWhenNoneSelected()  {
        Map<String,Object> given = new HashMap<>();
        given.put(ProjectKeys.SCHEMA, "schema");
        given.put(ProjectKeys.APPLICATION_NAME, "taxi-service");
        given.put(ProjectKeys.BASE_PACKAGE, "org.example");

        projectGenerator.setUpLexicalScope(given);
        Map<String,Object> actual = projectGenerator.getLexicalScope();

        for (SupportedFeatures it : SupportedFeatures.values())
            assertThat(actual).doesNotContainKey(it.toString());
        assertThat(actual).containsEntry(ProjectKeys.SCHEMA, "schema");
        assertThat(actual).containsEntry(ProjectKeys.APPLICATION_NAME, "taxi-service");
    }
}
