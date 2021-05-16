<#include "/common/Copyright.ftl">
package ${project.basePackage}.advice;

import ${project.basePackage}.exception.UnprocessableEntityException;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
* Handles turning exceptions into RFC-7807 problem/json responses,
* so instead of an exception and its stack trace leaking back
* to the client, an RFC-7807 problem description is returned instead.
*/
@SuppressWarnings("unused")
@ControllerAdvice
public class GlobalExceptionHandler {

    // TODO: implement this code

}

