<#include "/common/Copyright.ftl">

package ${endpoint.packageName};

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts Drink entity beans into DrinkResource objects
 */
@Component
public class ${endpoint.entityName}ResourceToBeanConverter implements Converter<${endpoint.entityName}Resource, ${endpoint.entityName}> {
    /**
     * Convert the source object of type {@code ${endpoint.entityName}Resource} to target type {@code ${endpoint.entityName}}.
     *
     * @param resource the source object to convert, which must be an instance of {@code ${endpoint.entityName}Resource} (never {@code null})
     * @return the converted object, which must be an instance of {@code ${endpoint.entityName}} (potentially {@code null})
     * @throws IllegalArgumentException if the source cannot be converted to the desired target type
     */
    @Override
    public ${endpoint.entityName} convert (@NonNull ${endpoint.entityName}Resource resource) {
        var target = new ${endpoint.entityName}();
        target.setResourceId ( resource.getResourceId() );
        target.setText ( resource.getText() );
        return target;
    }

    /**
     * Convert a list of RestfulResource objects into EJBs
     */
    public List<${endpoint.entityName}> convert (@NonNull List<${endpoint.entityName}Resource> sourceList) {
        return sourceList.stream().map(this::convert).collect(Collectors.toList());
    }
}
