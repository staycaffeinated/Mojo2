
## [Unreleased]

## [0.3.1] - 2021-10-14
Refactored build.gradle
- Collected imports into common file
- Use Java Toolchain to define Java version
- Set default Java to 17

## [0.3.0] - 2021-10-14
Updated library dependencies (spring-boot, zolando problem, spotless plugin version)
- spring boot gradle plugin to 2.5.5
- zalando problem library to 0.27.0. Refactored code as needed.
- spotless gradle plugin to 5.17.0
- added lombok gradle plugin

### Added changelog plugin
- Something I should have done sooner


## [0.2.1] - 2020-08-07
- Improvements in generated code drive by SonarQube reports
- Changed endpoint domain class names to match the resource name
  For example, instead of TreasureResource.java, its now Treasure.java
- Changed the entity bean class name to {resourceName}EntityBean.java
- Generate a lombok.config file

