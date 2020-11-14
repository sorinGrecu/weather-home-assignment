package com.sorin.homework.weather.client;

import com.sorin.homework.weather.config.properties.WeatherApiProperties;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Function;

@Log4j2
@Service
public class WorldWeatherApiClient {

    public static final String NUM_OF_DAYS = "num_of_days";
    public static final String KEY = "key";
    public static final String TIME_INTERVAL = "tp";
    public static final String CITY_NAME = "q";
    private static final String API_BASE_URL = "http://api.worldweatheronline.com";
    private static final String API_WEATHER_PATH = "/premium/v1/weather.ashx";
    private final WeatherApiProperties weatherApiProperties;
    private final WebClient webClient;

    public WorldWeatherApiClient(WeatherApiProperties weatherApiProperties, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(API_BASE_URL).build();
        this.weatherApiProperties = weatherApiProperties;
    }

    public String getWeatherJsonForCity(String city) {
        log.info("Calling World Weather Online API");
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(API_WEATHER_PATH)
                        .queryParam(KEY, weatherApiProperties.getKey())
                        .queryParam(NUM_OF_DAYS, MediaType.APPLICATION_JSON.getSubtype())
                        .queryParam(TIME_INTERVAL, 24)
                        .queryParam(CITY_NAME, city)
                        .build())
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(String.class))
                .block();
    }
}

