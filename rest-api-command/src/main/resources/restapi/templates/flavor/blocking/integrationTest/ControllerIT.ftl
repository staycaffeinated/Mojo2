<#include "/common/Copyright.ftl">

package ${endpoint.packageName};

import ${endpoint.basePackage}.common.AbstractIntegrationTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ${endpoint.entityName}ControllerIT extends AbstractIntegrationTest {

    @Autowired
    private ${endpoint.entityName}Repository ${endpoint.entityVarName}Repository;

    private List<${endpoint.entityName}> ${endpoint.entityVarName}List = null;

    @BeforeEach
    void setUp() {
        ${endpoint.entityVarName}List = new ArrayList<>();
        ${endpoint.entityVarName}List.add(new ${endpoint.entityName}(1L, 123L, "First ${endpoint.entityName}"));
        ${endpoint.entityVarName}List.add(new ${endpoint.entityName}(2L, 456L, "Second ${endpoint.entityName}"));
        ${endpoint.entityVarName}List.add(new ${endpoint.entityName}(3L, 789L, "Third ${endpoint.entityName}"));
        ${endpoint.entityVarName}List = ${endpoint.entityVarName}Repository.saveAll(${endpoint.entityVarName}List);
    }

    @AfterEach
    public void tearDownEachTime() {
        ${endpoint.entityVarName}Repository.deleteAll();
    }

    /*
     * FindById
     */
    @Nested
    public class ValidateFindById {
        @Test
        void shouldFind${endpoint.entityName}ById() throws Exception {
            ${endpoint.entityName} ${endpoint.entityVarName} = ${endpoint.entityVarName}List.get(0);
            Long ${endpoint.entityVarName}Id = ${endpoint.entityVarName}.getResourceId();

            mockMvc.perform(get(${endpoint.entityName}Routes.EXACTLY_ONE, ${endpoint.entityVarName}Id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.text", is(${endpoint.entityVarName}.getText())));

        }
    }

    /*
     * Create method
     */
    @Nested
    public class ValidateCreate${endpoint.entityName} {
        @Test
        void shouldCreateNew${endpoint.entityName}() throws Exception {
            ${endpoint.entityName}Resource resource = ${endpoint.entityName}Resource.builder().text("I am a new resource").build();

            mockMvc.perform(post(${endpoint.entityName}Routes.CREATE_ONE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(resource)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.text", is(resource.getText())))
                    ;

        }

        /**
         * Verify the controller's data validation catches malformed inputs and returns a problem/json response
         */
        @Test
        void shouldReturn400WhenCreateNew${endpoint.entityName}WithoutText() throws Exception {
            ${endpoint.entityName}Resource resource = ${endpoint.entityName}Resource.builder().build();

            mockMvc.perform(post(${endpoint.entityName}Routes.CREATE_ONE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(resource)))
                    .andExpect(status().is(400))
                    .andExpect(header().string("Content-Type", startsWith("application/json")))
                    .andExpect(jsonPath("$.status", is(400)))   // hibernate throws PropertyValueException
                    .andReturn()
                    ;
        }
    }

    /*
     * Update method
     */
    @Nested
    public class ValidateUpdate${endpoint.entityName} {

        @Test
        void shouldUpdate${endpoint.entityName}() throws Exception {
            ${endpoint.entityName} ${endpoint.entityVarName} = ${endpoint.entityVarName}List.get(0);
            ${endpoint.entityName}Resource resource = new ${endpoint.entityName}BeanToResourceConverter().convert(${endpoint.entityVarName});

            mockMvc.perform(put(${endpoint.entityName}Routes.EXACTLY_ONE, ${endpoint.entityVarName}.getResourceId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(resource)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.text", is(${endpoint.entityVarName}.getText())));

        }
    }

    /*
     * Delete method
     */
    @Nested
    public class ValidateDelete${endpoint.entityName} {
        @Test
        void shouldDelete${endpoint.entityName}() throws Exception {
            ${endpoint.entityName} ${endpoint.entityVarName} = ${endpoint.entityVarName}List.get(0);

            mockMvc.perform(
                    delete(${endpoint.entityName}Routes.EXACTLY_ONE, ${endpoint.entityVarName}.getResourceId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.text", is(${endpoint.entityVarName}.getText())));
        }
    }
}