package com.sorin.homework.weather.service;

import com.sorin.homework.weather.client.WeatherDataSource;
import com.sorin.homework.weather.config.properties.WeatherApiProperties;
import com.sorin.homework.weather.converter.WeatherDataMapper;
import com.sorin.homework.weather.exception.ClientApiException;
import com.sorin.homework.weather.model.WeatherAggregateData;
import com.sorin.homework.weather.model.WeatherSnapshot;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public abstract class WeatherService {
    protected final WeatherApiProperties weatherApiProperties;
    protected final WeatherDataSource weatherDataSource;
    protected final WeatherDataMapper payloadMapper;

    protected WeatherService(WeatherDataSource dataSource, WeatherDataMapper mapper, WeatherApiProperties properties) {
        this.weatherApiProperties = properties;
        this.weatherDataSource = dataSource;
        this.payloadMapper = mapper;
    }

    public abstract WeatherSnapshot getCurrentWeatherForCity(String city);

    public abstract List<WeatherSnapshot> getForecastedWeatherForCity(String city);

    protected WeatherAggregateData loadWeatherForCity(String city) throws ClientApiException {
        return payloadMapper.convertJsonToData(weatherDataSource.getWeatherJsonForCity(city));
    }

    protected boolean isForecastInTimeFrame(WeatherSnapshot snapshot) {
        return snapshot.getDate().isAfter(LocalDate.now()) &&
                snapshot.getDate().isBefore(LocalDate.now()
                        .plus(weatherApiProperties.getNumberOfDays() + 1, ChronoUnit.DAYS));
    }
}
