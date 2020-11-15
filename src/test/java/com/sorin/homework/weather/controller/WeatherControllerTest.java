package com.sorin.homework.weather.controller;

import com.sorin.homework.weather.exception.ResourceNotFoundException;
import com.sorin.homework.weather.mock.WeatherSnapshotMockData;
import com.sorin.homework.weather.model.WeatherSnapshot;
import com.sorin.homework.weather.service.WeatherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.sorin.homework.weather.mock.WeatherSnapshotMockData.CURRENT_CONDITION;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WeatherController.class)
@ActiveProfiles("test")
class WeatherControllerTest {
    private static List<WeatherSnapshot> forecastedWeather;
    private static WeatherSnapshot currentWeather;
    @MockBean
    private WeatherService weatherService;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void setUp() {
        forecastedWeather = WeatherSnapshotMockData.getMockedForecastedWeather();
        currentWeather = WeatherSnapshotMockData.getMockedCurrentWeather();
    }

    @Test
    void getCurrentWeather_returnsCorrectResponse() throws Exception {
        given(weatherService.getCurrentWeatherForCity(anyString())).willReturn(currentWeather);
        this.mockMvc.perform(get("/current?city=Bucharest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.condition", is(CURRENT_CONDITION)));
    }

    @Test
    void getWeatherForecast_returnsCorrectResponse() throws Exception {
        given(weatherService.getForecastedWeatherForCity(anyString())).willReturn(forecastedWeather);
        this.mockMvc.perform(get("/forecast?city=Bucharest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(forecastedWeather.size())));
    }

    @Test
    void getForecastWeather_returns404() throws Exception {
        given(weatherService.getForecastedWeatherForCity(anyString())).willThrow(ResourceNotFoundException.class);
        this.mockMvc.perform(get("/forecast?city=Bucharest"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCurrentWeather_returns404() throws Exception {
        given(weatherService.getCurrentWeatherForCity(anyString())).willThrow(ResourceNotFoundException.class);
        this.mockMvc.perform(get("/current?city=Bucharest"))
                .andExpect(status().isNotFound());
    }
}