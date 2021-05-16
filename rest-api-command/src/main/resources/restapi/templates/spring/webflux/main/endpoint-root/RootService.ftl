<#-- @ftlroot "../../../.." -->
<#include "/common/Copyright.ftl">
package ${project.basePackage}.endpoint.root;

import org.springframework.stereotype.Service;

/**
 * Empty implementation of a Service
 */
// This service a placeholder, so we suppress the Sonarqube complaint
@SuppressWarnings("java:S3400")
@Service
public class RootService {

    int doNothing() { return 0; }
}
