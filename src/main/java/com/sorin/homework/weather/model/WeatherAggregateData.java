package com.sorin.homework.weather.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class WeatherAggregateData {
    private final WeatherSnapshot currentWeather;
    private final Map<String, WeatherSnapshot> forecastedWeather;
}
