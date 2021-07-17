<#include "/common/Copyright.ftl">

package ${endpoint.packageName};

import lombok.NonNull;
import ${endpoint.basePackage}.validation.OnCreate;
import ${endpoint.basePackage}.validation.OnUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ${endpoint.basePackage}.math.SecureRandomSeries;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ${endpoint.entityName}Service {

    private final ${endpoint.entityName}Repository ${endpoint.entityVarName}Repository;

    private final ConversionService conversionService;

    /*
     * Constructor
     */
    @Autowired
    public ${endpoint.entityName}Service(${endpoint.entityName}Repository ${endpoint.entityVarName}Repository,
                                        ConversionService conversionService)
    {
        this.${endpoint.entityVarName}Repository = ${endpoint.entityVarName}Repository;
        this.conversionService = conversionService;
    }

    /*
     * findAll
     */
    public Flux<${endpoint.entityName}Resource> findAll${endpoint.entityName}s() {
        List<${endpoint.entityName}> resultSet = ${endpoint.entityVarName}Repository.findAll();
        // List<?> vList = resultSet.stream().map(ejb -> conversionService.convert(ejb,${endpoint.entityName}Resource.class)).collect(Collectors.toList());
        return Flux.empty();
    }

    /**
     * findByResourceId
     */
    public Mono<${endpoint.entityName}Resource> find${endpoint.entityName}ByResourceId(Long id) {
        Optional<${endpoint.entityName}> optional = ${endpoint.entityVarName}Repository.findByResourceId ( id );
        return Mono.justOrEmpty(optional.map(ejb -> conversionService.convert(ejb, ${endpoint.entityName}Resource.class)));

    }

    /*
     * findByText
     */
    public Flux<${endpoint.entityName}Resource> findByText(@NonNull String text, @Min(value=0) int pageNumber, @Min(value=20) int pageSize) {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(text).ascending());
            Page<${endpoint.entityName}> resultSet = ${endpoint.entityVarName}Repository.findByText(text, pageable);
            resultSet.stream()
                            .map(ejb -> conversionService.convert(ejb, ${endpoint.entityName}Resource.class))
                            .collect(Collectors.toList());

            return Flux.empty();
    }

    /**
     * Persists a new resource
     */
    public Mono<${endpoint.entityName}Resource> create${endpoint.entityName}( @NonNull @Validated(OnCreate.class) ${endpoint.entityName}Resource resource ) {
        resource.setResourceId ( SecureRandomSeries.nextLong() );
        ${endpoint.entityName} entityBean = Objects.requireNonNull(conversionService.convert ( resource, ${endpoint.entityName}.class ));
        entityBean = ${endpoint.entityVarName}Repository.save ( entityBean );
        return Mono.of(conversionService.convert(entityBean, ${endpoint.entityName}Resource.class));
    }

    /**
     * Updates an existing resource
     */
    public Mono<${endpoint.entityName}Resource> update${endpoint.entityName}( @NonNull @Validated(OnUpdate.class) ${endpoint.entityName}Resource resource ) {
        Optional<${endpoint.entityName}> optional = ${endpoint.entityVarName}Repository.findByResourceId ( resource.getResourceId() );
        if ( optional.isPresent() ){
            ${endpoint.entityName} entityBean=optional.get();
            // Copy all mutable fields of the resource into the entity bean
            entityBean.setText(resource.getText());
            // persist the changes
            entityBean = ${endpoint.entityVarName}Repository.save(entityBean);
            //return Optional.of( Objects.requireNonNull(conversionService.convert(entityBean, ${endpoint.entityName}Resource.class)));
            return Mono.empty();
        }
        return Mono.empty();
    }

    /**
     * delete
     */
    public void delete${endpoint.entityName}ByResourceId(@NonNull Long id) {
        ${endpoint.entityVarName}Repository.deleteByResourceId(id);
    }
}
