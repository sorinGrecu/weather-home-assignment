package com.sorin.homework.weather.config.properties;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Class holding properties related to the mapping between the World Weather Online (WWO) API and our own DTOs
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
public class ApiMappingProperties {
    private CurrentWeatherMapping currentWeather;
    private ForecastWeatherMapping forecastWeather;

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder(setterPrefix = "with")
    public static class CurrentWeatherMapping {
        private String tempC;
        private String tempF;
        private String humidity;
        private String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @SuperBuilder(setterPrefix = "with")
    public static class ForecastWeatherMapping extends CurrentWeatherMapping {
        private String date;
        private String dateFormat;
    }

}
