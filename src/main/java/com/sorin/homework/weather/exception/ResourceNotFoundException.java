package com.sorin.homework.weather.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Because of the annotation, the Spring REST controller will detect when this error is thrown inside of
 * the REST controller endpoints and map this to a HTTP response automatically
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not find data for the requested city")
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
