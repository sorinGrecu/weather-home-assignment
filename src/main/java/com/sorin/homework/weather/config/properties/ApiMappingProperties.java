package com.sorin.homework.weather.config.properties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Class holding properties related to the mapping between the World Weather Online (WWO) API and our own DTOs
 */
@Getter
@Setter
public class ApiMappingProperties {
    private CurrentWeatherMapping currentWeather;
    private ForecastWeatherMapping forecastWeather;

    @Getter
    @Setter
    public static class CurrentWeatherMapping {
        private String tempC;
        private String tempF;
        private String humidity;
        private String description;
    }

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    public static class ForecastWeatherMapping extends CurrentWeatherMapping {
        private String date;
        private String dateFormat;
    }

}
