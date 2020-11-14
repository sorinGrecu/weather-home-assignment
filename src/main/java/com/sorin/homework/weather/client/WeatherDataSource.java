package com.sorin.homework.weather.client;

/**
 * Data source for our weather application
 */
public interface WeatherDataSource {

    /**
     * Given a city, it will return a String containing a JSON formatted payload containing the weather information
     */
    String getWeatherJsonForCity(String city);
}
