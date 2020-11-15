package com.sorin.homework.weather.converter;

import com.sorin.homework.weather.config.properties.ApiMappingProperties;
import com.sorin.homework.weather.config.properties.ApiMappingProperties.CurrentWeatherMapping;
import com.sorin.homework.weather.config.properties.ApiMappingProperties.ForecastWeatherMapping;
import com.sorin.homework.weather.config.properties.WeatherApiProperties;
import com.sorin.homework.weather.exception.ClientApiException;
import com.sorin.homework.weather.mock.helper.FileReader;
import com.sorin.homework.weather.model.WeatherAggregateData;
import com.sorin.homework.weather.model.WeatherSnapshot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static com.sorin.homework.weather.mock.WeatherPropertiesMockData.*;
import static com.sorin.homework.weather.util.ExceptionUtils.NO_EXCEPTION_CAUSE_AVAILABLE;
import static org.junit.jupiter.api.Assertions.*;

class WeatherDataMapperTest {

    private static final String VALID_RESPONSE = FileReader.readStringFromFile(FileReader.VALID_RESPONSE);
    private static final String ERROR_RESPONSE = FileReader.readStringFromFile(FileReader.ERROR_RESPONSE);
    private static final String ERROR_RESPONSE_NO_DATA = FileReader.readStringFromFile(FileReader.ERROR_RESPONSE_NO_DATA);
    private static WeatherDataMapper weatherDataMapper;

    @BeforeAll
    public static void setup() {
        CurrentWeatherMapping currentMapping = getMockedCurrentWeatherMapping();
        ForecastWeatherMapping forecastMapping = getMockedForecastWeatherMapping();
        ApiMappingProperties mappingProperties = getMockedMappingProperties(currentMapping, forecastMapping);

        WeatherApiProperties weatherApiProperties = getMockedApiProperties(mappingProperties);
        weatherDataMapper = new WeatherDataMapper(weatherApiProperties);
    }

    @Test
    void convertJsonToData_shouldReturnAccuratelyMappedAggregateWeatherData() throws ClientApiException {
        // when
        WeatherAggregateData weatherAggregateData = weatherDataMapper.convertJsonToData(VALID_RESPONSE);
        // then
        WeatherSnapshot currentWeather = weatherAggregateData.getCurrentWeather();
        assertEquals(LocalDate.now().toString(), currentWeather.getDate().toString());
        assertEquals("Mist", currentWeather.getCondition());
        assertEquals(0.81, currentWeather.getHumidity());
        assertEquals(9, currentWeather.getTempC());
        assertEquals(48, currentWeather.getTempF());

        List<WeatherSnapshot> forecastedWeather = weatherAggregateData.getForecastedWeather();
         /* the expected number of days is the one set in the properties + another one because we buffer an extra day
         to cover for an eventual day-change during the stale cache period */
        assertEquals(NUMBER_OF_DAYS + 1, forecastedWeather.size());
        assertEquals(
                "WeatherSnapshot(date=2020-11-14, tempC=12.0, tempF=54.0, humidity=0.74, condition=Partly cloudy)",
                forecastedWeather.get(0).toString());
        assertEquals(
                "WeatherSnapshot(date=2020-11-15, tempC=13.0, tempF=55.0, humidity=0.69, condition=Partly cloudy)",
                forecastedWeather.get(1).toString());
        assertEquals(
                "WeatherSnapshot(date=2020-11-16, tempC=13.0, tempF=55.0, humidity=0.68, condition=Partly cloudy)",
                forecastedWeather.get(2).toString());
    }

    @Test
    void convertJsonToData_shouldThrowExceptionOnErrorResponseFromApi_withNoMessage() {
        // when
        // then
        Exception exception = assertThrows(ClientApiException.class,
                () -> weatherDataMapper.convertJsonToData(ERROR_RESPONSE_NO_DATA));
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(NO_EXCEPTION_CAUSE_AVAILABLE));
    }

    @Test
    void convertJsonToData_shouldThrowExceptionOnErrorResponseFromApi_withDetailedMessage() {
        // when
        // then
        Exception exception = assertThrows(ClientApiException.class,
                () -> weatherDataMapper.convertJsonToData(ERROR_RESPONSE));
        String expectedMessage = "Unable to find any matching weather location to the query submitted!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void castHumidityToDouble_castsZeroHumidityCorrectly() {
        // given
        WeatherApiProperties zeroHumidityProperties = WeatherApiProperties.builder()
                .withHumidityAsDecimals(true)
                .build();
        // when
        Double humidity = weatherDataMapper.castHumidityToDouble("0", zeroHumidityProperties);
        // then
        assertEquals(0, humidity);
    }

    @Test
    void castHumidityToDouble_castsToDecimalsCorrectly() {
        // given
        WeatherApiProperties zeroHumidityProperties = WeatherApiProperties.builder()
                .withHumidityAsDecimals(true)
                .build();
        // when
        Double humidity = weatherDataMapper.castHumidityToDouble("23", zeroHumidityProperties);
        // then
        assertEquals(0.23, humidity);
    }

    @Test
    void castHumidityToDouble_castsToPercentageCorrectly() {
        // given
        WeatherApiProperties zeroHumidityProperties = WeatherApiProperties.builder()
                .withHumidityAsDecimals(false)
                .build();
        // when
        Double humidity = weatherDataMapper.castHumidityToDouble("23", zeroHumidityProperties);
        // then
        assertEquals(23, humidity);
    }

}