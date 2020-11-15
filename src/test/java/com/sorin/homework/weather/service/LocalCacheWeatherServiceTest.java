package com.sorin.homework.weather.service;

import com.sorin.homework.weather.client.WeatherClient;
import com.sorin.homework.weather.config.properties.WeatherApiProperties;
import com.sorin.homework.weather.converter.WeatherDataMapper;
import com.sorin.homework.weather.exception.ClientApiException;
import com.sorin.homework.weather.exception.ResourceNotFoundException;
import com.sorin.homework.weather.mock.WeatherSnapshotMockData;
import com.sorin.homework.weather.mock.helper.FileReader;
import com.sorin.homework.weather.model.WeatherAggregateData;
import com.sorin.homework.weather.model.WeatherSnapshot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.sorin.homework.weather.mock.WeatherSnapshotMockData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class LocalCacheWeatherServiceTest {

    @InjectMocks
    LocalCacheWeatherService weatherService;

    @Mock
    private WeatherDataMapper weatherDataMapper;

    @Mock
    private WeatherApiProperties apiProperties;

    @Mock
    private WeatherClient client;

    @Test
    public void getCurrentWeatherForCity_shouldReturnSuccessfully() throws ClientApiException {
        // given
        String validResponse = FileReader.readStringFromFile(FileReader.VALID_RESPONSE);
        Mockito.when(client.getWeatherJsonForCity(anyString())).thenReturn(validResponse);

        WeatherSnapshot currentWeather = WeatherSnapshotMockData.getMockedCurrentWeather();
        List<WeatherSnapshot> forecastedWeather = WeatherSnapshotMockData.getMockedForecastedWeather();
        WeatherAggregateData weatherAggregateData = new WeatherAggregateData(currentWeather, forecastedWeather);
        Mockito.when(weatherDataMapper.convertJsonToData(validResponse)).thenReturn(weatherAggregateData);

        // when
        WeatherSnapshot weatherSnapshot = weatherService.getCurrentWeatherForCity("Bucharest");
        // then
        assertEquals(CURRENT_CONDITION, weatherSnapshot.getCondition());
        assertEquals(CURRENT_HUMIDITY, weatherSnapshot.getHumidity());
        assertEquals(CURRENT_TEMP_F, weatherSnapshot.getTempF());
        assertEquals(CURRENT_TEMP_C, weatherSnapshot.getTempC());
        assertEquals(LocalDate.now(), weatherSnapshot.getDate());
    }

    @Test
    public void getCurrentWeatherForCity_throwsResourceNotFound_whenNoDataFound() throws ClientApiException {
        // given
        String errorResponse = FileReader.readStringFromFile(FileReader.ERROR_RESPONSE);
        Mockito.when(client.getWeatherJsonForCity(anyString())).thenReturn(errorResponse);

        WeatherSnapshot currentWeather = WeatherSnapshotMockData.getMockedCurrentWeather();
        List<WeatherSnapshot> forecastedWeather = WeatherSnapshotMockData.getMockedForecastedWeather();
        Mockito.when(weatherDataMapper.convertJsonToData(errorResponse))
                .thenThrow(new ClientApiException("Test exception"));

        // when
        // then
        assertThrows(ResourceNotFoundException.class,
                () -> weatherService.getCurrentWeatherForCity("Bucharest"));
    }

    @Test
    public void getForecastWeatherForCity_shouldReturnSuccessfully() throws ClientApiException {
        // given
        WeatherSnapshot currentWeather = WeatherSnapshotMockData.getMockedCurrentWeather();
        List<WeatherSnapshot> forecastedWeather = WeatherSnapshotMockData.getMockedForecastedWeather();
        WeatherAggregateData weatherAggregateData = new WeatherAggregateData(currentWeather, forecastedWeather);
        Mockito.when(weatherDataMapper.convertJsonToData(anyString())).thenReturn(weatherAggregateData);

        String validResponse = FileReader.readStringFromFile(FileReader.VALID_RESPONSE);
        Mockito.when(client.getWeatherJsonForCity(anyString())).thenReturn(validResponse);
        Mockito.when(apiProperties.getNumberOfDays()).thenReturn(1);
        // when
        List<WeatherSnapshot> weatherSnapshot = weatherService.getForecastedWeatherForCity("Bucharest");
        // then
        assertEquals(LocalDate.now().plus(1, ChronoUnit.DAYS), weatherSnapshot.get(0).getDate());
        assertEquals(FORECAST_CONDITION, weatherSnapshot.get(0).getCondition());
        assertEquals(FORECAST_HUMIDITY, weatherSnapshot.get(0).getHumidity());
        assertEquals(FORECAST_TEMP_F, weatherSnapshot.get(0).getTempF());
        assertEquals(FORECAST_TEMP_C, weatherSnapshot.get(0).getTempC());
    }

    @Test
    public void getForecastWeatherForCity_throwsResourceNotFound_whenNoDataFound() throws ClientApiException {
        // given
        String errorResponse = FileReader.readStringFromFile(FileReader.ERROR_RESPONSE);
        Mockito.when(client.getWeatherJsonForCity(anyString())).thenReturn(errorResponse);

        WeatherSnapshot currentWeather = WeatherSnapshotMockData.getMockedCurrentWeather();
        List<WeatherSnapshot> forecastedWeather = WeatherSnapshotMockData.getMockedForecastedWeather();
        Mockito.when(weatherDataMapper.convertJsonToData(errorResponse))
                .thenThrow(new ClientApiException("Test exception"));

        // when
        // then
        assertThrows(ResourceNotFoundException.class,
                () -> weatherService.getForecastedWeatherForCity("Bucharest"));
    }

}