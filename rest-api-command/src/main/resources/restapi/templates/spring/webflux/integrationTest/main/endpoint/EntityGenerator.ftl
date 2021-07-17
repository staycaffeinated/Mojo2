<#include "/common/Copyright.ftl">

package ${endpoint.packageName};

class ${endpoint.entityName}Generator {
    static ${endpoint.entityName}Resource generate${endpoint.entityName}() {
        return ${endpoint.entityName}Resource.builder().text("sample text").build();
    }
}