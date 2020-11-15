package com.sorin.homework.weather.service;

import com.sorin.homework.weather.client.WeatherClient;
import com.sorin.homework.weather.config.properties.WeatherApiProperties;
import com.sorin.homework.weather.converter.WeatherDataMapper;
import com.sorin.homework.weather.exception.ClientApiException;
import com.sorin.homework.weather.model.WeatherAggregateData;
import com.sorin.homework.weather.model.WeatherSnapshot;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service interface that returns the current weather and a list of forecasted weather, starting with tomorrow
 */
public abstract class WeatherService {
    protected final WeatherApiProperties weatherApiProperties;
    protected final WeatherClient weatherClient;
    protected final WeatherDataMapper payloadMapper;

    protected WeatherService(WeatherClient client, WeatherDataMapper mapper, WeatherApiProperties properties) {
        this.weatherApiProperties = properties;
        this.weatherClient = client;
        this.payloadMapper = mapper;
    }

    /**
     * Returns the current weather for the inputted city
     */
    public abstract WeatherSnapshot getCurrentWeatherForCity(String city);

    /**
     * Returns a List holding forecasted days for the city inputted, starting with tomorrow
     */
    public abstract List<WeatherSnapshot> getForecastedWeatherForCity(String city);

    /**
     * Calls the {@link WeatherClient}, fetches the weather data and converts is using the {@link WeatherDataMapper}
     */
    protected WeatherAggregateData loadWeatherForCity(String city) throws ClientApiException {
        return payloadMapper.convertJsonToData(weatherClient.getWeatherJsonForCity(city));
    }

    /**
     * Returns true if the {@link WeatherSnapshot} represents a correctly dated forecast day. This is implemented in
     * order to check if an eventual (and very probable) stale cache is going to return a forecast that includes
     * the current day. This can happen if the cache is refreshed at, for example, 23:50. After midnight the
     * cache should not return the first day from the before mentioned stale cache, but should skip it,
     * as it now represents the current day.
     */
    protected boolean isForecastInTimeFrame(WeatherSnapshot snapshot) {
        return snapshot.getDate().isAfter(LocalDate.now()) &&
                snapshot.getDate().isBefore(LocalDate.now()
                        .plus(weatherApiProperties.getNumberOfDays() + 1, ChronoUnit.DAYS));
    }
}
