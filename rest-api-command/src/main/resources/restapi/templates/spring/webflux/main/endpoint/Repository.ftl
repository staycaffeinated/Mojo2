<#include "/common/Copyright.ftl">
package ${endpoint.packageName};

import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ${endpoint.entityName}Repository extends ReactiveSortingRepository<${endpoint.entityName}, Long> {

    // Find by the resource ID known by external applications
    Mono<${endpoint.entityName}> findByResourceId ( Long id );

    // Find by the database ID
    Mono<${endpoint.entityName}> findById ( Long id );


    /* returns the number of entities deleted */
    Mono<Long> deleteByResourceId( Long id );
    

    Flux<${endpoint.entityName}> findAllByText(String text);
}

