package com.sorin.homework.weather.client;

import com.jayway.jsonpath.JsonPath;
import com.sorin.homework.weather.mock.helper.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static com.sorin.homework.weather.client.WorldWeatherClient.API_WEATHER_URL;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest
class WorldWeatherClientTest {

    @Autowired
    WorldWeatherClient worldWeatherClient;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void getWeatherJsonForCity_shouldReturnProperResponse() {
        // given
        mockServer.expect(ExpectedCount.once(),
                requestTo(matchesPattern(API_WEATHER_URL + ".*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(FileReader.readStringFromFile(FileReader.VALID_RESPONSE)));
        // when
        String response = worldWeatherClient.getWeatherJsonForCity("Bucharest");

        // then
        String tempC = JsonPath.read(response, "$.data.current_condition[0].temp_C");
        String tempF = JsonPath.read(response, "$.data.current_condition[0].temp_F");
        String humidity = JsonPath.read(response, "$.data.current_condition[0].humidity");
        String description = JsonPath.read(response, "$.data.current_condition[0].weatherDesc[0].value");

        assertEquals("9", tempC);
        assertEquals("48", tempF);
        assertEquals("81", humidity);
        assertEquals("Mist", description);

    }
}