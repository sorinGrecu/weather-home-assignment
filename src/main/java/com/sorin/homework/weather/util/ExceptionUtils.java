package com.sorin.homework.weather.util;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExceptionUtils {

    public static String readErrorMessageFromPayload(String jsonPayload) {
        try {
            return JsonPath.read(jsonPayload, "$.data.error[0].msg");
        } catch (Exception e) {
            log.warn("Could not read error message from json payload. Reason: {}", e.getMessage());
            return "No exception cause available";
        }
    }

}
