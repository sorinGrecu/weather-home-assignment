package com.sorin.homework.weather.mock.helper;


import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class FileReader {

    public static final String VALID_RESPONSE = "/data/valid_response.json";
    public static final String ERROR_RESPONSE = "/data/error_response.json";
    public static final String ERROR_RESPONSE_NO_DATA = "/data/error_response_no_data.json";

    public static String readStringFromFile(String filepath) {
        InputStream stream = FileReader.class.getResourceAsStream(filepath);
        try {
            return IOUtils.toString(stream, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
