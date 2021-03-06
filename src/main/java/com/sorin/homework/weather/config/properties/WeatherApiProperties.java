package com.sorin.homework.weather.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * Configuration class holding properties that will be used through Spring Boot to set values for the key used
 * for the weather data source, mappings between the data received from the data source and our DTOs and
 * the number of days that should be shown by our forecast endpoint.
 */
@Getter
@Setter
@Validated
@NoArgsConstructor
@SuperBuilder(setterPrefix = "with")
@ConfigurationProperties(prefix = "weather.api")
public class WeatherApiProperties {
    @NotBlank(message = "Please define an API key in the properties")
    private String key;
    @Min(1)
    private Integer numberOfDays = 3;
    private ApiMappingProperties mapping;
    private Boolean humidityAsDecimals = true;
}
