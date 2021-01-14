<#include "/common/Copyright.ftl">
package ${project.basePackage}.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

/**
* Handles turning exceptions into RFC-7807 problem/json responses,
* so instead of an exception and its stack trace leaking back
* to the client, an RFC-7807 problem description is returned instead.
*/
@ControllerAdvice
public class ExceptionHandler implements ProblemHandling {}
