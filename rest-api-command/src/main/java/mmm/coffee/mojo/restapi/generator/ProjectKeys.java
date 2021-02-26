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
 * These are the properties used by project-level templates
 */
public class ProjectKeys {

    private ProjectKeys() {}
    
    public static final String APPLICATION_NAME = "applicationName";
    public static final String BASE_PACKAGE = "basePackage";
    public static final String BASE_PACKAGE_PATH = "basePackagePath";
    public static final String BASE_PATH = "basePath";
    public static final String JAVA_VERSION = "javaVersion";
    public static final String SCHEMA = "schema";
    public static final String GROUP_ID = "groupId";

    // Library Versions
    public static final String SPRING_BOOT_VERSION = "springBootVersion";
    public static final String SPRING_CLOUD_VERSION = "springCloudVersion";
    public static final String SPRING_DEPENDENCY_MGMT_VERSION = "springDependencyManagementVersion";
    public static final String PROBLEM_SPRING_VERSION = "problemSpringWebVersion";
    public static final String ASSERTJ_VERSION = "assertJVersion";
    public static final String JUNIT_SYSTEM_RULES_VERSION = "junitSystemRulesVersion";
    public static final String JUNIT_VERSION = "junitVersion";
    public static final String LIQUIBASE_VERSION = "liquibaseVersion";
    public static final String LOMBOK_VERSION = "lombokVersion";
    public static final String LOG4J_VERSION = "log4JVersion";
    public static final String MOJO_VERSION = "mojoVersion";
    public static final String TEST_CONTAINERS_VERSION = "testContainersVersion";
    

}
