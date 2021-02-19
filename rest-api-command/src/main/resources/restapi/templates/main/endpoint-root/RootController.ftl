<#-- @ftlroot "../.." -->
<#include "/common/Copyright.ftl">
package ${project.basePackage}.endpoint.root;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Slf4j
public class RootController {

    /*
     * Constructor
     */
    public RootController() {
        // empty
    }

    /*
     * The root path
     */
    @GetMapping (value= "", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<String> getHome() {
        return ResponseEntity.ok().build();
    }
}