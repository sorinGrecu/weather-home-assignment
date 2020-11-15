package com.sorin.homework.weather.converter;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.sorin.homework.weather.config.properties.ApiMappingProperties;
import com.sorin.homework.weather.config.properties.WeatherApiProperties;
import com.sorin.homework.weather.exception.ClientApiException;
import com.sorin.homework.weather.model.WeatherAggregateData;
import com.sorin.homework.weather.model.WeatherSnapshot;
import com.sorin.homework.weather.util.DateUtils;
import com.sorin.homework.weather.util.ExceptionUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This component is responsible with converting the response received from the World Weather Online (WWO) API to our
 * own DTOs by using JsonPath for data normalization. This approach is preferred instead of parsing the whole JSON
 * response, as the payload received from WWO contains a lot of data and that would represent a huge overhead.
 * The mappings between the WWO API response and our own DTOs are injected into this class using
 * {@link ApiMappingProperties}
 */
@Log4j2
@Component
public class WeatherDataMapper {

    private final WeatherApiProperties weatherApiProperties;

    public WeatherDataMapper(WeatherApiProperties weatherApiProperties) {
        this.weatherApiProperties = weatherApiProperties;
    }

    /**
     * @param jsonPayload the JSON payload representing the data from which we should extract the weather data
     *                    that we will map to our DTOs
     * @return {@link WeatherAggregateData} holding weather data, current weather and a list of forecasted
     * weather, both represented through {@link WeatherSnapshot}
     * @throws ClientApiException if mapping of the JSON fails
     */
    public WeatherAggregateData convertJsonToData(String jsonPayload) throws ClientApiException {
        log.info("Converting JSON payload to model");
        try {
            WeatherSnapshot currentWeather = jsonToCurrentWeather(jsonPayload);
            List<WeatherSnapshot> forecastedWeather = jsonToForecastedWeather(jsonPayload);
            return new WeatherAggregateData(currentWeather, forecastedWeather);
        } catch (PathNotFoundException e) {
            String cause = ExceptionUtils.readErrorMessageFromPayload(jsonPayload);
            throw new ClientApiException(cause, e);
        }
    }

    /**
     * Converts a given JSON String into {@link WeatherSnapshot}, representing the current weather, based on the
     * mapping found in the properties
     */
    private WeatherSnapshot jsonToCurrentWeather(String jsonPayload) {
        ApiMappingProperties.CurrentWeatherMapping mapping = weatherApiProperties.getMapping().getCurrentWeather();
        String tempC = JsonPath.read(jsonPayload, mapping.getTempC());
        String tempF = JsonPath.read(jsonPayload, mapping.getTempF());
        String humidity = JsonPath.read(jsonPayload, mapping.getHumidity());
        String weatherDescription = JsonPath.read(jsonPayload, mapping.getDescription());

        return WeatherSnapshot.builder()
                .withHumidity(castHumidityToDouble(humidity, weatherApiProperties))
                .withTempC(Double.parseDouble(tempC))
                .withTempF(Double.parseDouble(tempF))
                .withCondition(weatherDescription)
                .withDate(LocalDate.now())
                .build();
    }

    /**
     * Converts a given JSON String into a List of {@link WeatherSnapshot}, representing the forecasted weather
     * for the next days based on the mapping found in the properties
     */
    private List<WeatherSnapshot> jsonToForecastedWeather(String jsonPayload) {
        ApiMappingProperties.ForecastWeatherMapping mapping = weatherApiProperties.getMapping().getForecastWeather();

        List<String> tempC = JsonPath.read(jsonPayload, mapping.getTempC());
        List<String> tempF = JsonPath.read(jsonPayload, mapping.getTempF());
        List<String> humidity = JsonPath.read(jsonPayload, mapping.getHumidity());
        List<String> weatherDescription = JsonPath.read(jsonPayload, mapping.getDescription());
        List<String> weatherDate = JsonPath.read(jsonPayload, mapping.getDate());

        log.info("Fetched {} forecasted days from the JSON response", tempC.size());

        return IntStream
                .range(0, tempC.size())
                .mapToObj(i ->
                        WeatherSnapshot.builder()
                                .withDate(DateUtils.stringToDate(weatherDate.get(i), mapping.getDateFormat()))
                                .withHumidity(castHumidityToDouble(humidity.get(i), weatherApiProperties))
                                .withTempC(Double.parseDouble(tempC.get(i)))
                                .withTempF(Double.parseDouble(tempF.get(i)))
                                .withCondition(weatherDescription.get(i))
                                .build())
                .collect(Collectors.toList());
    }

    /**
     * Casts the humidity String returned from the JSON to {@link Double} to comply with the API specifications.
     * Based on the properties it will either return a decimal value between [0,1] or a percentage
     * value between [0,100]
     */
    protected Double castHumidityToDouble(String humidity, WeatherApiProperties weatherApiProperties) {
        double humidityValue = Double.parseDouble(humidity);
        return humidityValue == 0 ? 0D : weatherApiProperties.getHumidityAsDecimals() ? humidityValue / 100 : humidityValue;
    }

}
