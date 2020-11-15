package com.sorin.homework.weather.exception;

/**
 * Exception that should be thrown whenever there is a problem with the call to or payload returned
 * by our weather data source
 */
public class ClientApiException extends Exception {

    public ClientApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientApiException(String message) {
        super(message);
    }
}
