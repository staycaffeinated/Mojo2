<#include "/common/Copyright.ftl">
package ${project.basePackage}.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Catch all exception
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnprocessableEntityException extends ResponseStatusException {

    static private final long serialVersionUID = 1;

    /**
     * Default Constructor
     */
    public UnprocessableEntityException() {
        super(HttpStatus.BAD_REQUEST);
    }

    /**
     * Constructor with a reason to add to the exception
     * message as explanation.
     *
     * @param reason the associated reason (optional)
     */
    public UnprocessableEntityException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

    /**
     * Constructor with a reason to add to the exception
     * message as explanation, as well as a nested exception.
     *
     * @param reason the associated reason (optional)
     * @param cause  a nested exception (optional)
     */
    public UnprocessableEntityException(String reason, Throwable cause) {
        super(HttpStatus.BAD_REQUEST, reason, cause);
    }
}