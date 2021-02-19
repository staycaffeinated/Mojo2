<#include "/common/Copyright.ftl">

package ${project.basePackage}.endpoint.root;

import ${project.basePackage}.common.AbstractIntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Integration tests of the exception handling of the root controller
 */
@ExtendWith(SpringExtension.class)
class RootExceptionHandlingIT extends AbstractIntegrationTest {

    @Nested
    class ExceptionTests {

        @Test
        void shouldNotReturnStackTrace() throws Exception {
            // when/then
            mockMvc.perform(post("/")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.stackTrace").doesNotExist())
                    .andDo((print()))
                    .andReturn();
        }
    }
}