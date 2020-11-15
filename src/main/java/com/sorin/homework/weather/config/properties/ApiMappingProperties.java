package com.sorin.homework.weather.config.properties;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConstructorBinding;

/**
 * Class holding properties related to the mapping between the World Weather Online (WWO) API and our own DTOs
 */
@Getter
@NoArgsConstructor
@ConstructorBinding
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class ApiMappingProperties {
    private CurrentWeatherMapping currentWeather;
    private ForecastWeatherMapping forecastWeather;

    @Getter
    @ConstructorBinding
    @SuperBuilder(setterPrefix = "with")
    public static class CurrentWeatherMapping {
        private final String tempC;
        private final String tempF;
        private final String humidity;
        private final String description;
    }

    @Getter
    @ConstructorBinding
    @SuperBuilder(setterPrefix = "with")
    public static class ForecastWeatherMapping extends CurrentWeatherMapping {
        private final String date;
        private final String dateFormat;
    }

}
