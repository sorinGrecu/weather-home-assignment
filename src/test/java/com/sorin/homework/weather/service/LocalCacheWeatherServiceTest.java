package com.sorin.homework.weather.service;

import com.sorin.homework.weather.mock.helper.FileReader;
import com.sorin.homework.weather.model.WeatherSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static com.sorin.homework.weather.client.WorldWeatherDataSource.API_WEATHER_URL;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
@TestPropertySource(locations = "/application.yaml")
class LocalCacheWeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void getCurrentWeatherForCity() {
        // given
        mockServer.expect(ExpectedCount.once(),
                requestTo(matchesPattern(API_WEATHER_URL + ".*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(FileReader.readStringFromFile(FileReader.VALID_RESPONSE)));
        // when
        WeatherSnapshot currentWeather = weatherService.getCurrentWeatherForCity("Bucharest");
        // then
        mockServer.verify();
        assertEquals("kek", currentWeather.getCondition());
    }


    @Test
    void getForecastedWeatherForCity() {
    }
}