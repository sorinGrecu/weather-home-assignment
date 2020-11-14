package com.sorin.homework.weather.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sorin.homework.weather.client.WorldWeatherApiClient;
import com.sorin.homework.weather.converter.WeatherPayloadConverter;
import com.sorin.homework.weather.model.WeatherAggregateData;
import com.sorin.homework.weather.model.WeatherSnapshot;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LocalCacheWeatherService extends WeatherService {

    private final LoadingCache<String, WeatherAggregateData> cache;

    public LocalCacheWeatherService(WorldWeatherApiClient weatherApiClient, WeatherPayloadConverter payloadConverter) {
        super(weatherApiClient, payloadConverter);
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<>() {
                    @Override
                    public WeatherAggregateData load(final String city) {
                        return loadWeatherForCity(city);
                    }
                });
    }

    @Override
    public WeatherSnapshot getCurrentWeatherForCity(String city) {
        //TODO: implement exception handling
        try {
            return this.cache.get(city).getCurrentWeather();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<WeatherSnapshot> getForecastedWeatherForCity(String city) {
        //TODO: implement exception handling
        try {
            return new ArrayList<>(this.cache.get(city).getForecastedWeather().values());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}