package com.sorin.homework.weather.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "weather.api")
public class WeatherApiProperties {
    private String key;
    private Integer numberOfDays;
}
