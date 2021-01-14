<#include "/common/Copyright.ftl">
package ${endpoint.packageName};

import ${endpoint.basePackage}.validation.OnCreate;
import ${endpoint.basePackage}.validation.OnUpdate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.net.URI;

@RestController
@RequestMapping("${endpoint.basePath}")
@Slf4j
public class ${endpoint.entityName}Controller {

    private final ${endpoint.entityName}Service ${endpoint.entityVarName}Service;

    /*
     * Constructor
     */
     @Autowired
     public ${endpoint.entityName}Controller(${endpoint.entityName}Service ${endpoint.entityVarName}Service) {
        this.${endpoint.entityVarName}Service = ${endpoint.entityVarName}Service;
     }

    /*
     * Get all
     */
    @GetMapping (value="", produces = MediaType.APPLICATION_JSON_VALUE )
    public List<${endpoint.entityName}Resource> getAll${endpoint.entityName}s() {
        return ${endpoint.entityVarName}Service.findAll${endpoint.entityName}s();
    }

    /*
     * Get one by resourceId
     *
     */
    @GetMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<${endpoint.entityName}Resource> get${endpoint.entityName}ById(@PathVariable Long id) {
        return ${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
     * Create one
     */
    @PostMapping (value="", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create${endpoint.entityName}(@RequestBody @Validated(OnCreate.class) ${endpoint.entityName}Resource resource ) {
        ${endpoint.entityName}Resource savedResource = ${endpoint.entityVarName}Service.create${endpoint.entityName} ( resource );
        URI uri = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(savedResource.getResourceId())
                        .toUri();
        return ResponseEntity.created(uri).body(savedResource);
    }
    
    /*
     * Update one
     */
    @PutMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<${endpoint.entityName}Resource> update${endpoint.entityName}(@PathVariable Long id, @RequestBody @Validated(OnUpdate.class) ${endpoint.entityName}Resource ${endpoint.entityVarName}) {
        Optional<${endpoint.entityName}Resource> optional = ${endpoint.entityVarName}Service.update${endpoint.entityName}( ${endpoint.entityVarName} );
        return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
     * Delete one
     */
    @DeleteMapping(value="/{id}")
    public ResponseEntity<${endpoint.entityName}Resource> delete${endpoint.entityName}(@PathVariable Long id) {
        return ${endpoint.entityVarName}Service.find${endpoint.entityName}ByResourceId(id)
                .map(${endpoint.entityVarName} -> {
                    ${endpoint.entityVarName}Service.delete${endpoint.entityName}ByResourceId(id);
                    return ResponseEntity.ok(${endpoint.entityVarName});
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
     * Find by text
     */
    @GetMapping(value="/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<${endpoint.entityName}Resource>> searchByText (@RequestParam(name="text", required = true) String text,
                                         @RequestParam(name="page", required = false, defaultValue = "1") int pageNumber,
                                         @RequestParam(name="size", required = false, defaultValue = "20") Integer pageSize)
    {
        return ResponseEntity.ok(${endpoint.entityVarName}Service.findByText(text, pageNumber, pageSize));
    }
}