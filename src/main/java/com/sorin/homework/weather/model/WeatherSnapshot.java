package com.sorin.homework.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO holding data about weather conditions specific to the moment the request was made.
 * The Date field is used internally to differentiate between models, offering the
 * possibility to show it for logging purposes or as a future feature.
 */
@Data
@Builder(setterPrefix = "with")
public class WeatherSnapshot {
    @JsonIgnore
    private LocalDate date;
    private double tempC;
    private double tempF;
    private double humidity;
    private String condition;
}
