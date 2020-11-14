package com.sorin.homework.weather.config.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Class holding properties related to the mapping between the World Weather Online (WWO) API and our own DTOs
 */
@Data
public class ApiMappingProperties {
    private CurrentWeatherMapping currentWeather;
    private ForecastWeatherMapping forecastWeather;

    @Data
    public static class CurrentWeatherMapping {
        private String tempC;
        private String tempF;
        private String humidity;
        private String description;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class ForecastWeatherMapping extends CurrentWeatherMapping {
        private String date;
        private String dateFormat;
    }

}
