package com.sorin.homework.weather.client;

import com.sorin.homework.weather.aspect.Stopwatch;
import com.sorin.homework.weather.config.properties.WeatherApiProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * World Weather Online implementation for our weather data source
 */
@Log4j2
@Service
public class WorldWeatherClient implements WeatherClient {

    public static final String NUM_OF_DAYS = "num_of_days";
    public static final String KEY = "key";
    public static final String TIME_INTERVAL = "tp";
    public static final String CITY_NAME = "q";
    public static final String FORMAT = "format";
    public static final String API_WEATHER_URL = "http://api.worldweatheronline.com/premium/v1/weather.ashx";

    private final WeatherApiProperties weatherApiProperties;
    private final RestTemplate restTemplate;

    public WorldWeatherClient(WeatherApiProperties weatherApiProperties, RestTemplate restTemplate) {
        this.weatherApiProperties = weatherApiProperties;
        this.restTemplate = restTemplate;
    }

    /**
     * Given the name of a city, this will call the World Weather Online (WWO) API and return the response,
     * currently set to JSON.
     * We increment the number of days forecasted by two because the WWO API includes the current day
     * in the returned forecast and we do not use it. The second extra day is to cover the case
     * in which a request is sent near the end of the day and we cache the WWO response.
     * If before the day-change we needed to return, for example, Monday, Tuesday,
     * Wednesday, after the day-change we will need to return Tuesday, Wednesday
     * and Thursday, so we request one extra day as buffer for a day-change
     * scenario.
     */
    @Stopwatch(message = "World Weather Online API call duration")
    public String getWeatherJsonForCity(String city) {
        log.info("Calling World Weather Online API for city: {}", city);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_WEATHER_URL)
                .queryParam(KEY, weatherApiProperties.getKey())
                .queryParam(NUM_OF_DAYS, weatherApiProperties.getNumberOfDays() + 2)
                .queryParam(FORMAT, MediaType.APPLICATION_JSON.getSubtype())
                .queryParam(TIME_INTERVAL, 24)
                .queryParam(CITY_NAME, city);

        return restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class)
                .getBody();
    }
}

