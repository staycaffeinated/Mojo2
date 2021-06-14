<#include "/common/Copyright.ftl">
package ${project.basePackage}.advice;

import ${project.basePackage}.exception.UnprocessableEntityException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.webflux.advice.ProblemHandling;
import reactor.core.publisher.Mono;

/**
* Handles turning exceptions into RFC-7807 problem/json responses,
* so instead of an exception and its stack trace leaking back
* to the client, an RFC-7807 problem description is returned instead.
*/
@SuppressWarnings("unused")
@ControllerAdvice
public class GlobalExceptionHandler implements ProblemHandling {

    @ExceptionHandler(UnprocessableEntityException.class)
    public Mono<Problem> handleUnprocessableEntityException(UnprocessableEntityException exception) {
        return problemDescription("The request cannot be processed", exception);
    }

    /**
     * Build a Problem/JSON description with HttpStatus: 422 (unprocessable entity)
     */
    private Mono<Problem> problemDescription(String title, Throwable throwable) {
        return problemDescription(title, throwable, Status.UNPROCESSABLE_ENTITY);
    }

    private Mono<Problem> problemDescription(String title, Throwable throwable, Status status) {
        Problem problem = Problem.builder().withStatus(status).withDetail(throwable.getMessage())
            .withTitle(title).build();

  	  return Mono.just(problem);
    }
}

