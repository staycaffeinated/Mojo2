<#include "/common/Copyright.ftl">
package ${endpoint.packageName};

import ${endpoint.basePackage}.exception.ResourceNotFoundException;
import ${endpoint.basePackage}.exception.UnprocessableEntityException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import ${endpoint.basePackage}.math.SecureRandomSeries;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * Unit tests of the ${endpoint.entityName} service
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"unused"})
class ${endpoint.entityName}ServiceTests {
    @Mock
    private ${endpoint.entityName}Repository mockRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private ${endpoint.entityName}Service serviceUnderTest;

    @Spy
    private final ConversionService conversionService = FakeConversionService.build();


    @Test
    void testFindAll${endpoint.entityName}s() {
        Flux<${endpoint.entityName}> pojoList = convertToFlux(create${endpoint.entityName}List());
        given(mockRepository.findAll()).willReturn(pojoList);

        Flux<${endpoint.entityName}Resource> stream = serviceUnderTest.findAll${endpoint.entityName}s();

        StepVerifier.create(stream).expectSubscription().expectNextCount(3).verifyComplete();
    }


    @Test
    void testFind${endpoint.entityName}ByResourceId() {
        // Given
        ${endpoint.entityName} expectedEJB = create${endpoint.entityName}();
        Long expectedId = 1000L;
        expectedEJB.setResourceId(expectedId);
        Mono<${endpoint.entityName}> rs = Mono.just(expectedEJB);
        given(mockRepository.findByResourceId(any(Long.class))).willReturn(rs);

        // When
        Mono<${endpoint.entityName}Resource> publisher = serviceUnderTest.find${endpoint.entityName}ByResourceId(expectedId);

        // Then
        StepVerifier.create(publisher)
                .expectSubscription()
                .consumeNextWith(item -> assertThat(Objects.equals(item.getResourceId(), expectedId)))
                .verifyComplete();
    }


    @Test
    void testFindAllByText() {
        // Given
        final String expectedText = "Lorim ipsum";
        List<${endpoint.entityName}> expectedRows = create${endpoint.entityName}ListHavingSameTextValue(expectedText);
        given(mockRepository.findAllByText(expectedText)).willReturn(Flux.fromIterable(expectedRows));

        // When
        Flux<${endpoint.entityName}Resource> publisher = serviceUnderTest.findAllByText(expectedText);

        // Then
        StepVerifier.create(publisher)
                .expectSubscription()
                .consumeNextWith(item -> assertThat(Objects.equals(item.getText(), expectedText)))
                .consumeNextWith(item -> assertThat(Objects.equals(item.getText(), expectedText)))
                .consumeNextWith(item -> assertThat(Objects.equals(item.getText(), expectedText)))
                .verifyComplete();
    }


    @Test
    void testCreate${endpoint.entityName}() {
        // Given
        Long expectedResourceId = 123456789L;
        // what the client submits to the service
        ${endpoint.entityName}Resource expectedPOJO = ${endpoint.entityName}Resource.builder().text("Lorim ipsum dolor amount").build();
        // what the persisted version looks like
        ${endpoint.entityName} persistedObj = conversionService.convert(expectedPOJO, ${endpoint.entityName}.class);
        persistedObj.setResourceId(expectedResourceId);
        persistedObj.setId(1L);
        given(mockRepository.save(any(${endpoint.entityName}.class))).willReturn(Mono.just(persistedObj));

        // When
        Mono<Long> publisher = serviceUnderTest.create${endpoint.entityName}(expectedPOJO);

        // Then
        StepVerifier.create(publisher.log("testCreate : "))
                .expectSubscription().consumeNextWith(item -> assertThat(Objects.equals(item, expectedResourceId))).verifyComplete();

    }


    @Test
    void testUpdate${endpoint.entityName}() {
        // Given
        // what the client submits
        ${endpoint.entityName}Resource submittedPOJO = ${endpoint.entityName}Resource.builder().text("Updated value").resourceId(1000L).build();
        // what the new persisted value looks like
        ${endpoint.entityName} persistedObj = conversionService.convert(submittedPOJO, ${endpoint.entityName}.class);
        Mono<${endpoint.entityName}> dataStream = Mono.just(persistedObj);
        given(mockRepository.findByResourceId(any(Long.class))).willReturn(dataStream);
        given(mockRepository.save(persistedObj)).willReturn(dataStream);

        // When
        serviceUnderTest.update${endpoint.entityName}(submittedPOJO);

        // Then
        // verify publishEvent was invoked
        verify(publisher, times(1)).publishEvent(any());
    }


    @Test
    void testDelete${endpoint.entityName}() {
        Long deletedId = 1000L;
        given(mockRepository.deleteByResourceId(deletedId)).willReturn(Mono.just(deletedId));

        serviceUnderTest.delete${endpoint.entityName}ByResourceId(deletedId);

        verify(publisher, times(1)).publishEvent(any());
    }


	@Test
	void whenDeleteNull${endpoint.entityName}_expectNullPointerException() {
		assertThrows(NullPointerException.class, () -> serviceUnderTest.delete${endpoint.entityName}ByResourceId((Long) null));
	}

	@Test
	void whenFindNonExistingEntity_expectResourceNotFoundException() {
		given(mockRepository.findByResourceId(any())).willReturn(Mono.empty());

		Mono<${endpoint.entityName}> publisher = serviceUnderTest.findByResourceId(100L);

		StepVerifier.create(publisher).expectSubscription().expectError(ResourceNotFoundException.class).verify();
	}

	@Test
	void whenUpdateOfNull${endpoint.entityName}_expectNullPointerException() {
		assertThrows(NullPointerException.class, () -> serviceUnderTest.update${endpoint.entityName}(null));
	}

	@Test
	void whenFindAllByNullText_expectNullPointerException() {
		assertThrows(NullPointerException.class, () -> serviceUnderTest.findAllByText(null));
	}

	@Test
	void whenCreateNull${endpoint.entityName}_expectNullPointerException() {
		assertThrows(NullPointerException.class, () -> serviceUnderTest.create${endpoint.entityName}(null));
	}

    /**
     * Per its API, a ConversionService::convert method _could_ return null.
     * The scope of this test case is to verify our own code's behavior should a null be returned.
     * In this case, an UnprocessableEntityException is thrown.
     */
	@Test
	void whenConversionToEjbFails_expectUnprocessableEntityException() {
		ConversionService mockConversionService = Mockito.mock(ConversionService.class);
		${endpoint.entityName}Service localService = new ${endpoint.entityName}Service(mockRepository, mockConversionService, publisher);
		given(mockConversionService.convert(any(${endpoint.entityName}Resource.class), eq(${endpoint.entityName}.class))).willReturn((${endpoint.entityName}) null);

		${endpoint.entityName}Resource sample = ${endpoint.entityName}Resource.builder().text("sample").build();
		assertThrows(UnprocessableEntityException.class, () -> localService.create${endpoint.entityName}(sample));
	}

    // -----------------------------------------------------------
    // Helper methods
    // -----------------------------------------------------------

    private Flux<${endpoint.entityName}> convertToFlux (List<${endpoint.entityName}> list) { return Flux.fromIterable(create${endpoint.entityName}List()); }

    private List<${endpoint.entityName}Resource> convertToPojo(List<${endpoint.entityName}> list) {
        return list.stream().map(item -> conversionService.convert(item, ${endpoint.entityName}Resource.class)).collect(Collectors.toList());
    }

    private List<${endpoint.entityName}> create${endpoint.entityName}List() {
        ${endpoint.entityName} w1 = ${endpoint.entityName}.builder().resourceId(1000L).text("Lorim ipsum dolor imit").build();
        ${endpoint.entityName} w2 = ${endpoint.entityName}.builder().resourceId(2000L).text("Duis aute irure dolor in reprehenderit").build();
        ${endpoint.entityName} w3 = ${endpoint.entityName}.builder().resourceId(3000L).text("Excepteur sint occaecat cupidatat non proident").build();

        ArrayList<${endpoint.entityName}> dataList = new ArrayList<>();
        dataList.add(w1);
        dataList.add(w2);
        dataList.add(w3);

        return dataList;
    }

    private List<${endpoint.entityName}> create${endpoint.entityName}ListHavingSameTextValue(final String value) {
        ${endpoint.entityName} w1 = ${endpoint.entityName}.builder().resourceId(1010L).text(value).build();
        ${endpoint.entityName} w2 = ${endpoint.entityName}.builder().resourceId(2020L).text(value).build();
        ${endpoint.entityName} w3 = ${endpoint.entityName}.builder().resourceId(3030L).text(value).build();

        ArrayList<${endpoint.entityName}> dataList = new ArrayList<>();
        dataList.add(w1);
        dataList.add(w2);
        dataList.add(w3);

        return dataList;
    }

    private ${endpoint.entityName} create${endpoint.entityName}() {
        return ${endpoint.entityName}.builder().resourceId(1000L).text("Lorim ipsum dolor imit").build();
    }
}