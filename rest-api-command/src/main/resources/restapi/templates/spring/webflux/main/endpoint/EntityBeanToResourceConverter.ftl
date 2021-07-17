<#include "/common/Copyright.ftl">
package ${endpoint.packageName};

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ${endpoint.entityName}BeanToResourceConverter implements Converter<${endpoint.entityName}, ${endpoint.entityName}Resource> {

    /**
     * Convert the source object of type {@code ${endpoint.entityName}} to target type {@code ${endpoint.entityName}Resource}.
     *
     * @param source the source object to convert, which must be an instance of {@code ${endpoint.entityName}} (never {@code null})
     * @return the converted object, which must be an instance of {@code ${endpoint.entityName}Resource} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public ${endpoint.entityName}Resource convert(@NonNull ${endpoint.entityName} source) {
        return ${endpoint.entityName}Resource.builder()
                        .resourceId( source.getResourceId() )
                        .text( source.getText() )
                        .build();
    }

    /**
     * Convert a list of EJBs into RestfulResource objects
     */
    public List<${endpoint.entityName}Resource> convert (@NonNull List<${endpoint.entityName}> sourceList) {
        return sourceList.stream().map(this::convert).collect(Collectors.toList());
    }
}