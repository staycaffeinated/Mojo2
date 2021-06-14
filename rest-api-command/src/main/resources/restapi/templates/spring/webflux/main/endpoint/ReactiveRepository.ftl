<#include "/common/Copyright.ftl">
package ${endpoint.packageName};

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ${endpoint.entityName}Repository extends JpaRepository<${endpoint.entityName}, Long> {

    Optional<${endpoint.entityName}> findByResourceId ( Long id );

    /* returns the number of entities deleted */
    Long deleteByResourceId( Long id );

    Page<${endpoint.entityName}> findByText(String text, Pageable pageable);
}

