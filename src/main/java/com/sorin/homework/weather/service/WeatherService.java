package com.sorin.homework.weather.service;

import com.sorin.homework.weather.client.WorldWeatherApiClient;
import com.sorin.homework.weather.converter.WeatherPayloadConverter;
import com.sorin.homework.weather.model.WeatherAggregateData;
import com.sorin.homework.weather.model.WeatherSnapshot;

import java.util.List;

public abstract class WeatherService {

    protected final WorldWeatherApiClient weatherApiClient;
    protected final WeatherPayloadConverter payloadConverter;

    protected WeatherService(WorldWeatherApiClient weatherApiClient, WeatherPayloadConverter payloadConverter) {
        this.payloadConverter = payloadConverter;
        this.weatherApiClient = weatherApiClient;
    }

    public abstract WeatherSnapshot getCurrentWeatherForCity(String city);

    public abstract List<WeatherSnapshot> getForecastedWeatherForCity(String city);


    protected WeatherAggregateData loadWeatherForCity(String city) {
        return payloadConverter.convertJsonToData(weatherApiClient.getWeatherJsonForCity(city));
    }
}
