<#include "/common/Copyright.ftl">

package ${endpoint.packageName};

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ${endpoint.entityName}ResourceToBeanConverterTests {

    ${endpoint.entityName}ResourceToBeanConverter converterUnderTest = new ${endpoint.entityName}ResourceToBeanConverter();


	@Test
	void whenDataToConvertIsWellFormed_expectSuccessfulConversion() {
		final Long expectedPublicId = 12345L;
		final String expectedText = "hello world";

		${endpoint.entityName}Resource pojo = ${endpoint.entityName}Resource.builder().resourceId(expectedPublicId).text(expectedText).build();
		${endpoint.entityName} ejb = converterUnderTest.convert(pojo);

		assertThat(ejb).isNotNull();
		assertThat(ejb.getResourceId()).isEqualTo(expectedPublicId);
		assertThat(ejb.getText()).isEqualTo(expectedText);
	}

	@Test
	void whenDataListIsWellFormed_expectSuccessfulConversion() {
		// Given a list of 3 items
		final Long itemOne_expectedPublicId = 12345L;
		final String itemOne_expectedText = "hello goodbye";

		final Long itemTwo_expectedPublicId = 6000L;
		final String itemTwo_expectedText = "strawberry fields";

		final Long itemThree_expectedPublicId = 5000L;
		final String itemThree_expectedText = "sgt pepper";

		${endpoint.entityName}Resource itemOne = ${endpoint.entityName}Resource.builder().resourceId(itemOne_expectedPublicId)
				.text(itemOne_expectedText).build();
		${endpoint.entityName}Resource itemTwo = ${endpoint.entityName}Resource.builder().resourceId(itemTwo_expectedPublicId)
				.text(itemTwo_expectedText).build();
		${endpoint.entityName}Resource itemThree = ${endpoint.entityName}Resource.builder().resourceId(itemThree_expectedPublicId)
				.text(itemThree_expectedText).build();

		ArrayList<${endpoint.entityName}Resource> list = new ArrayList<>();
		list.add(itemOne);
		list.add(itemTwo);
		list.add(itemThree);

		// When
		List<${endpoint.entityName}> results = converterUnderTest.convert(list);

		// Then expect the fields of the converted items to match the original items
		assertThat(results.size()).isEqualTo(list.size());
		assertThat(fieldsMatch(itemOne, results.get(0))).isTrue();
		assertThat(fieldsMatch(itemTwo, results.get(1))).isTrue();
		assertThat(fieldsMatch(itemThree, results.get(2))).isTrue();
	}

	@Test
	void whenConvertingNullObject_expectNullPointerException() {
		assertThrows(NullPointerException.class,
            () -> converterUnderTest.convert((${endpoint.entityName}Resource) null));
	}

	@Test
	void whenConvertingNullList_expectNullPointerException() {
		assertThrows(NullPointerException.class,
			() -> converterUnderTest.convert((List<${endpoint.entityName}Resource>) null));
	}

    @Test
    void shouldPopulateAllFields() {
        ${endpoint.entityName}Resource resource = ${endpoint.entityName}Resource.builder().resourceId(100L).text("hello world").build();

        ${endpoint.entityName} bean = converterUnderTest.convert(resource);
        assertThat(bean.getResourceId()).isEqualTo(resource.getResourceId());
        assertThat(bean.getText()).isEqualTo(resource.getText());
    }

	// ------------------------------------------------------------------
	// Helper methods
	// ------------------------------------------------------------------
	private boolean fieldsMatch(${endpoint.entityName}Resource expected, ${endpoint.entityName} actual) {
		if (!Objects.equals(expected.getResourceId(), actual.getResourceId()))
			return false;
		if (!Objects.equals(expected.getText(), actual.getText()))
			return false;
		return true;
	}
}
