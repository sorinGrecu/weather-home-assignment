package com.sorin.homework.weather.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sorin.homework.weather.aspect.Stopwatch;
import com.sorin.homework.weather.client.WeatherDataSource;
import com.sorin.homework.weather.config.properties.WeatherApiProperties;
import com.sorin.homework.weather.converter.WeatherDataMapper;
import com.sorin.homework.weather.exception.ClientApiException;
import com.sorin.homework.weather.exception.ResourceNotFoundException;
import com.sorin.homework.weather.model.WeatherAggregateData;
import com.sorin.homework.weather.model.WeatherSnapshot;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log4j2
@Service
public class LocalCacheWeatherService extends WeatherService {

    private final LoadingCache<String, WeatherAggregateData> cache;

    public LocalCacheWeatherService(WeatherDataSource dataSource, WeatherDataMapper mapper, WeatherApiProperties properties) {
        super(dataSource, mapper, properties);
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<>() {
                    @Override
                    public WeatherAggregateData load(final String city) throws ClientApiException {
                        return loadWeatherForCity(city);
                    }
                });
    }

    @Override
    @Stopwatch(message = "Local cache service call duration for current weather")
    public WeatherSnapshot getCurrentWeatherForCity(String city) {
        return getFromCache(city).getCurrentWeather();
    }

    @Override
    @Stopwatch(message = "Local cache service call duration for forecasted weather")
    public List<WeatherSnapshot> getForecastedWeatherForCity(String city) {
        return getFromCache(city).getForecastedWeather().stream()
                .filter(this::isForecastInTimeFrame)
                .collect(Collectors.toList());
    }

    private WeatherAggregateData getFromCache(String city) {
        try {
            return this.cache.get(city);
        } catch (ExecutionException e) {
            log.error("Could not find city {} in the cache. Reason:{}", city, e.getMessage());
            throw new ResourceNotFoundException("Could not find data for this city", e);
        }
    }

}