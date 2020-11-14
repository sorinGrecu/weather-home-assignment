package com.sorin.homework.weather.controller;

import com.sorin.homework.weather.model.WeatherSnapshot;
import com.sorin.homework.weather.service.WeatherService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public WeatherSnapshot getCurrentWeather(@RequestParam String city) {
        return weatherService.getCurrentWeatherForCity(city);
    }

    @RequestMapping(value = "/forecast", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WeatherSnapshot> getWeatherForecast(@RequestParam String city) {
        return weatherService.getForecastedWeatherForCity(city);
    }
}
