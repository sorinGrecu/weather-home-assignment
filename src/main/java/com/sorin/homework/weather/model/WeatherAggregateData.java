package com.sorin.homework.weather.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * Object designed to hold data in the cache, modeled after the format returned by the currently used weather
 * information data source
 */
@Getter
@AllArgsConstructor
public class WeatherAggregateData {
    private final WeatherSnapshot currentWeather;
    private final List<WeatherSnapshot> forecastedWeather;
}
