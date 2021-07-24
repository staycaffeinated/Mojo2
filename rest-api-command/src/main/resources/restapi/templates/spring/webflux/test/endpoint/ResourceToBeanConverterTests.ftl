<#include "/common/Copyright.ftl">

package ${endpoint.packageName};

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ${endpoint.entityName}ResourceToBeanConverterTests {

    ${endpoint.entityName}ResourceToBeanConverter converter = new ${endpoint.entityName}ResourceToBeanConverter();

    @Test
    void shouldReturnNullWhenResourceIsNull() {
        assertThrows (NullPointerException.class, () ->  {
            converter.convert((${endpoint.entityName}Resource) null);
        });
    }

    @Test
    void shouldReturnNullWhenListIsNull() {
        assertThrows (NullPointerException.class, () -> {
            converter.convert((List<${endpoint.entityName}Resource>)null);
        });
    }

    @Test
    void shouldPopulateAllFields() {
        ${endpoint.entityName}Resource resource = ${endpoint.entityName}Resource.builder().resourceId(100L).text("hello world").build();

        ${endpoint.entityName} bean = converter.convert(resource);
        assertThat(bean.getResourceId()).isEqualTo(resource.getResourceId());
        assertThat(bean.getText()).isEqualTo(resource.getText());
    }
}
