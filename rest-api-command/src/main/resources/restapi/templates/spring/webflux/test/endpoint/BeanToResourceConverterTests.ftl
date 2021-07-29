<#include "/common/Copyright.ftl">

package ${endpoint.packageName};

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ${endpoint.entityName}BeanToResourceConverterTests {

    ${endpoint.entityName}BeanToResourceConverter converter = new ${endpoint.entityName}BeanToResourceConverter();

	@Test
	void whenDataToConvertIsWellFormed_expectSuccessfulConversion() {
		final Long expectedPublicId = 12345L;
		final String expectedText = "hello world";

		${endpoint.entityName} ejb = ${endpoint.entityName}.builder().resourceId(expectedPublicId).id(0L).text(expectedText).build();
		${endpoint.entityName}Resource pojo = converterUnderTest.convert(ejb);

		assertThat(pojo).isNotNull();
		assertThat(pojo.getResourceId()).isEqualTo(expectedPublicId);
		assertThat(pojo.getText()).isEqualTo(expectedText);
	}

	@Test
	void whenDataListIsWellFormed_expectSuccessfulConversion() {
		// Given a list of 3 items
		final Long itemOne_expectedPublicId = 12345L;
		final Long itemOne_expectedDatabaseId = 2424L;
		final String itemOne_expectedText = "hello goodbye";

		final Long itemTwo_expectedPublicId = 6000L;
		final Long itemTwo_expectedDatabaseId = 42348L;
		final String itemTwo_expectedText = "strawberry fields";

		final Long itemThree_expectedPublicId = 5000L;
		final Long itemThree_expectedDatabaseId = 9341L;
		final String itemThree_expectedText = "sgt pepper";

		${endpoint.entityName} itemOne = ${endpoint.entityName}.builder().resourceId(itemOne_expectedPublicId).text(itemOne_expectedText)
				.id(itemOne_expectedDatabaseId).build();
		${endpoint.entityName} itemTwo = ${endpoint.entityName}.builder().resourceId(itemTwo_expectedPublicId).text(itemTwo_expectedText)
				.id(itemTwo_expectedDatabaseId).build();
		${endpoint.entityName} itemThree = ${endpoint.entityName}.builder().resourceId(itemThree_expectedPublicId).text(itemThree_expectedText)
				.id(itemThree_expectedDatabaseId).build();

		ArrayList<Driver> list = new ArrayList<>();
		list.add(itemOne);
		list.add(itemTwo);
		list.add(itemThree);

		// When
		List<${endpoint.entityName}Resource> results = converterUnderTest.convert(list);

		// Then expect the fields of the converted items to match the original items
		assertThat(results.size()).isEqualTo(list.size());
		assertThat(fieldsMatch(itemOne, results.get(0))).isTrue();
		assertThat(fieldsMatch(itemTwo, results.get(1))).isTrue();
		assertThat(fieldsMatch(itemThree, results.get(2))).isTrue();
	}

	@Test
	void whenBeanIsNull_expectNullPointerException() {
		Assertions.assertThrows(NullPointerException.class, () -> converterUnderTest.convert((${endpoint.entityName}) null));
	}

	@Test
	void whenListIsNull_expectNullPointerException() {
		Assertions.assertThrows(NullPointerException.class, () -> converterUnderTest.convert((List<${endpoint.entityName}>) null));
	}

    /**
     * Verify that properties of the EJB that must not shared outside
     * the security boundary of the service are not copied into
     * the RESTful resource.  For example, the database ID
     * assigned to an entity bean must not be exposed to
     * external applications, thus the database ID is never
     * copied into a RESTful resource.
     */
    @Test
    void shouldCopyOnlyExposedProperties() {
        ${endpoint.entityName} bean = new ${endpoint.entityName}();
        bean.setResourceId(12345L);
        bean.setText("hello, world");
        bean.setId(100L);

        ${endpoint.entityName}Resource pojo = converter.convert(bean);
        assertThat(pojo.getResourceId()).isEqualTo(bean.getResourceId());
        assertThat(pojo.getText()).isEqualTo(bean.getText());
    }

	// ------------------------------------------------------------------
	// Helper methods
	// ------------------------------------------------------------------
	private boolean fieldsMatch(${endpoint.entityName} expected, ${endpoint.entityName}Resource actual) {
		if (!Objects.equals(expected.getResourceId(), actual.getResourceId()))
			return false;
		if (!Objects.equals(expected.getText(), actual.getText()))
			return false;
		return true;
	}
}
