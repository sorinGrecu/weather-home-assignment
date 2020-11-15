package com.sorin.homework.weather.util;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExceptionUtils {

    public static final String ERROR_JSON_PATH = "$.data.error[0].msg";
    public static final String NO_EXCEPTION_CAUSE_AVAILABLE = "No exception cause available";

    /**
     * Given a JSON String it will use the json path defined by ExceptionUtils.ERROR_JSON_PATH
     * to return the error message found at said path. If the path is invalid,
     * ExceptionUtils.NO_EXCEPTION_CAUSE_AVAILABLE is returned.
     */
    public static String readErrorMessageFromPayload(String jsonPayload) {
        try {
            return JsonPath.read(jsonPayload, ERROR_JSON_PATH);
        } catch (Exception e) {
            log.warn("Could not read error message from json payload. Reason: {}", e.getMessage());
            return NO_EXCEPTION_CAUSE_AVAILABLE;
        }
    }

}
