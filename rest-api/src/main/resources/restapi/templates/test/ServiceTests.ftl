<#include "/common/Copyright.ftl">

package ${endpoint.packageName};


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Unit tests of {@link ${endpoint.entityName}Service }
 */
@ExtendWith(MockitoExtension.class)
public class ${endpoint.entityName}ServiceTests {

    @InjectMocks
    private ${endpoint.entityName}Service ${endpoint.entityVarName}Service;

    @Mock
    private ${endpoint.entityName}Repository ${endpoint.entityVarName}Repository;

    @Spy
    private final ${endpoint.entityName}ResourceToBeanConverter ${endpoint.entityVarName}ResourceToBeanConverter = new ${endpoint.entityName}ResourceToBeanConverter();

    @Spy
    private final ${endpoint.entityName}BeanToResourceConverter ${endpoint.entityVarName}BeanToResourceConverter = new ${endpoint.entityName}BeanToResourceConverter();

    @Spy
    private final ConversionService conversionService = FakeConversionService.build();

    private List<${endpoint.entityName}> ${endpoint.entityVarName}List;

    @BeforeEach
    public void setUpEachTime() {
        ${endpoint.entityName} item1 = new ${endpoint.entityName}(1L, 100L, "text 1");
        ${endpoint.entityName} item2 = new ${endpoint.entityName}(2L, 200L, "text 2");
        ${endpoint.entityName} item3 = new ${endpoint.entityName}(3L, 300L, "text 3");

        ${endpoint.entityVarName}List = new LinkedList<>();
        ${endpoint.entityVarName}List.add( item1 );
        ${endpoint.entityVarName}List.add( item2 );
        ${endpoint.entityVarName}List.add( item3 );
    }

    /**
     * Unit tests of the findAll method
     */
    @Nested
    public class FindAllTests {

        @Test
        public void shouldReturnAllRowsWhenRepositoryIsNotEmpty() {
            given(${endpoint.entityVarName}Repository.findAll() ).willReturn( ${endpoint.entityVarName}List );

            List<${endpoint.entityName}Resource> result = ${endpoint.entityVarName}Service.findAll${endpoint.entityName}s();

            then(result).isNotNull();       // must never return null
            then(result.size()).isEqualTo( ${endpoint.entityVarName}List.size()); // must return all rows
        }

        @Test
        public void shouldReturnEmptyListWhenRepositoryIsEmpty() {
            given( ${endpoint.entityVarName}Repository.findAll() ).willReturn( new ArrayList<>() );

            List<${endpoint.entityName}Resource> result = ${endpoint.entityVarName}Service.findAll${endpoint.entityName}s();

            then(result).isNotNull();       // must never get null back
            then(result.size()).isZero();   // must have no content for this edge case
        }
    }

    @Nested
    public class FindByTextTests {
        /*
         * Happy path - some rows match the given text
         */
        @Test
        public void shouldReturnRowsWhenRowsWithTextExists() {
            // given
            Pageable pageable = PageRequest.of(1,20);
            int start = (int) pageable.getOffset();
            int end = (int)((start +  pageable.getPageSize()) > ${endpoint.entityVarName}List.size() ? ${endpoint.entityVarName}List.size() : (start + pageable.getPageSize()));
            Page<${endpoint.entityName}> page = new PageImpl<>(${endpoint.entityVarName}List, pageable, ${endpoint.entityVarName}List.size());

            // we're not validating what text gets passed to the repo, only that a result set comes back
            given(${endpoint.entityVarName}Repository.findByText(any(), any(Pageable.class))).willReturn(page);

            // when/then
            List<${endpoint.entityName}Resource> result = ${endpoint.entityVarName}Service.findByText("text", 1, 20);

            then(result).isNotNull();       // must never return null

            // depending on which is smaller, the sample size or the page size, we expect that many rows back
            then(result.size()).isEqualTo(Math.min(${endpoint.entityVarName}List.size(), pageable.getPageSize()));
        }

        @Test
        public void shouldReturnEmptyListWhenNoDataFound() {
            given( ${endpoint.entityVarName}Repository.findByText(any(), any(Pageable.class))).willReturn( Page.empty() );

            List<${endpoint.entityName}Resource> result = ${endpoint.entityVarName}Service.findByText("foo", 1, 100);

            then(result).isNotNull();       // must never get null back
            then(result.size()).isZero();   // must have no content for this edge case
        }
    }

    @Nested
    public class Find${endpoint.entityName}ByResourceIdTests {
        /*
         * Happy path - finds the entity in the database
         */
        @Test
        public void shouldReturnOneWhenRepositoryContainsMatch() {
            // given
            Long expectedId = 100L;
            Optional<${endpoint.entityName}> expected = Optional.of(new ${endpoint.entityName}(1L, expectedId, "sample"));
            given(${endpoint.entityVarName}Repository.findByResourceId(any())).willReturn(expected);;

            // when/then
            Optional<${endpoint.entityName}Resource> actual = ${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(expectedId);

            assertThat(actual).isNotNull();
            assertThat(actual.isPresent()).isTrue();
            assertThat(actual.get().getResourceId()).isEqualTo(expectedId);
        }

        /*
         * Test path when no such entity exists in the database
         */
        @Test
        public void shouldReturnEmptyWhenRepositoryDoesNotContainMatch() {
            given(${endpoint.entityVarName}Repository.findByResourceId(any())).willReturn(Optional.empty());

            // when/then
            Optional<${endpoint.entityName}Resource> actual = ${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(100L);

            assertThat(actual).isNotNull();
            assertThat(actual.isPresent()).isFalse();
        }
    }

    @Nested
    public class Create${endpoint.entityName}Tests {

        /*
         * happy path should create a new entity
         */
        @Test
        public void shouldCreateOneWhen${endpoint.entityName}ResourceIsWellFormed() {
            // given
            final String sampleText = "sample text";
            final ${endpoint.entityName} expectedEJB = new ${endpoint.entityName}(1L, 100L, sampleText);
            given(${endpoint.entityVarName}Repository.save(any())).willReturn(expectedEJB);

            // when/then
            ${endpoint.entityName}Resource sampleData = ${endpoint.entityName}Resource.builder().text(sampleText).build();
            ${endpoint.entityName}Resource actual = ${endpoint.entityVarName}Service.create${endpoint.entityName}(sampleData);

            assertThat(actual).isNotNull();
            assertThat(actual.getResourceId()).isNotNull();
            assertThat(actual.getText()).isEqualTo(sampleText);
        }

        @Test
        public void shouldThrowNullPointerExceptionWhen${endpoint.entityName}IsNull() {
            Exception exception = assertThrows (NullPointerException.class,
                    () ->  ${endpoint.entityVarName}Service.create${endpoint.entityName}(null));
        }
    }

    @Nested
    public class Update${endpoint.entityName}Tests {
        /*
         * Happy path - updates an existing entity
         */
        @Test
        public void shouldUpdateWhenEntityIsFound() {
            // given
            Long resourceId = 100L;
            ${endpoint.entityName}Resource changedVersion = ${endpoint.entityName}Resource.builder().resourceId(resourceId).text("new text").build();
            ${endpoint.entityName} originalEJB = new ${endpoint.entityName} (1L, resourceId, "old text");
            ${endpoint.entityName} updatedEJB = ${endpoint.entityVarName}ResourceToBeanConverter.convert (changedVersion);
            given(${endpoint.entityVarName}Repository.findByResourceId(any())).willReturn(Optional.of(originalEJB));
            given(${endpoint.entityVarName}Repository.save(any())).willReturn(updatedEJB);

            // when/then
            Optional<${endpoint.entityName}Resource> optional = ${endpoint.entityVarName}Service.update${endpoint.entityName}(changedVersion);
            then(optional).isNotNull();
            then(optional.isPresent()).isTrue();
            if (optional.isPresent()) {
               then (optional.get().getResourceId()).isEqualTo(resourceId);
               then (optional.get().getText()).isEqualTo("new text");
            }
        }

        /*
         * When no database record is found, the update should return an empty result
         */
        @Test
        public void shouldReturnEmptyOptionWhenEntityIsNotFound() {
            // given no such entity exists in the database...
            given(${endpoint.entityVarName}Repository.findByResourceId(any())).willReturn(Optional.empty());

            // then/when
            ${endpoint.entityName}Resource changedVersion = ${endpoint.entityName}Resource.builder().resourceId(100L).text("new text").build();
            Optional<${endpoint.entityName}Resource> result = ${endpoint.entityVarName}Service.update${endpoint.entityName}(changedVersion);
            then(result.isEmpty()).isTrue();
        }
    }

    @Nested
    public class Delete${endpoint.entityName}Tests {
        /*
         * Happy path - deletes an entity
         */
        @Test
        public void shouldDeleteWhenEntityExists()  {
            // given one matching one is found
            given(${endpoint.entityVarName}Repository.deleteByResourceId(any())).willReturn(1L);

            ${endpoint.entityVarName}Service.delete${endpoint.entityName}ByResourceId(1010L);

            // Verify the deleteByResourceId method was invoked
            verify(${endpoint.entityVarName}Repository, times(1)).deleteByResourceId(any());
        }

        /*
         * When deleting a non-existing EJB, silently return
         */
        @Test
        public void shouldSilentlyReturnWhenEntityDoesNotExist() {
            // given no matching record is found
            given(${endpoint.entityVarName}Repository.deleteByResourceId(any())).willReturn(0L);

            ${endpoint.entityVarName}Service.delete${endpoint.entityName}ByResourceId(1010L);

            // Verify the deleteByResourceId method was invoked
            verify(${endpoint.entityVarName}Repository, times(1)).deleteByResourceId(any());
        }
    }
}