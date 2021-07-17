<#include "/common/Copyright.ftl">

package ${endpoint.packageName};

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ${endpoint.basePackage}.math.SecureRandomSeries;
import ${endpoint.basePackage}.validation.OnCreate;
import ${endpoint.basePackage}.validation.OnUpdate;
import ${endpoint.basePackage}.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ${endpoint.entityName}Service {

    private final ApplicationEventPublisher publisher;
	private final ConversionService conversionService;
    private final ${endpoint.entityName}Repository repository;

    /*
     * Constructor
     */
    @Autowired
    public ${endpoint.entityName}Service(${endpoint.entityName}Repository ${endpoint.entityVarName}Repository,
                                        ConversionService conversionService, ApplicationEventPublisher publisher) {
        this.repository = ${endpoint.entityVarName}Repository;
        this.conversionService = conversionService;
        this.publisher = publisher;
    }

    /*
     * findAll
     */
    public Flux<${endpoint.entityName}Resource> findAll${endpoint.entityName}s() {
        return Flux.from(repository.findAll().map(ejb -> conversionService.convert(ejb, ${endpoint.entityName}Resource.class)));
    }

    /**
     * findByResourceId
     */
    public Mono<${endpoint.entityName}Resource> find${endpoint.entityName}ByResourceId(Long id) throws ResourceNotFoundException {
        Mono<${endpoint.entityName}> monoItem = findByResourceId(id);
		return monoItem.flatMap(it -> Mono.just(conversionService.convert(it, ${endpoint.entityName}Resource.class)));
    }

    /*
     * findAllByText
     */
    public Flux<${endpoint.entityName}Resource> findAllByText(@NonNull String text) {
        return Flux.from(repository.findAllByText(text).map(ejb -> conversionService.convert(ejb, ${endpoint.entityName}Resource.class)));
    }

    /**
     * Create
     */
    public Mono<Long> create${endpoint.entityName}( @NonNull @Validated(OnCreate.class) ${endpoint.entityName}Resource resource ) {
	    ${endpoint.entityName} entity = conversionService.convert(resource, ${endpoint.entityName}.class);
		entity.setResourceId(SecureRandomSeries.nextLong());
		return repository.save(entity)
                         .doOnSuccess(item -> publishEvent(${endpoint.entityName}Event.CREATED, item))
				         .flatMap(item -> Mono.just(item.getResourceId()));
    }

    /**
     * Update
     */
    public void update${endpoint.entityName}( @NonNull @Validated(OnUpdate.class) ${endpoint.entityName}Resource resource ) {
	    Mono<${endpoint.entityName}> entityBean = findByResourceId(resource.getResourceId());
		entityBean.subscribe(value -> {
		    // As fields are added to the entity, this block has to be updated
        	value.setText(resource.getText());

			repository.save(value).doOnSuccess(item -> publishEvent(${endpoint.entityName}Event.UPDATED, item)).subscribe();
		});
    }

    /**
     * Delete
     */
    public void delete${endpoint.entityName}ByResourceId(@NonNull Long id) {
        repository.deleteByResourceId(id).subscribe();
    }

    /**
     * Find the EJB having the given resourceId
     */
    Mono<${endpoint.entityName}> findByResourceId(Long id) throws ResourceNotFoundException {
        return repository.findByResourceId(id).switchIfEmpty(Mono.defer(() -> Mono.error(
            new ResourceNotFoundException(String.format("Entity not found with the given resourceId: %s", id)))));
    }
    
    /**
     * Publish events
     */
    private void publishEvent(String event, ${endpoint.entityName} entity) {
	    log.debug("publishEvent: {}, resourceId: {}", event, entity.getResourceId());
		this.publisher.publishEvent(new ${endpoint.entityName}Event(event, conversionService.convert(entity, ${endpoint.entityName}Resource.class)));
	}
}