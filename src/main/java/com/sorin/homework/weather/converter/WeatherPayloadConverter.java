package com.sorin.homework.weather.converter;

import com.jayway.jsonpath.JsonPath;
import com.sorin.homework.weather.config.properties.ApiMappingProperties;
import com.sorin.homework.weather.model.WeatherAggregateData;
import com.sorin.homework.weather.model.WeatherSnapshot;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
@Component
public class WeatherPayloadConverter {

    private final ApiMappingProperties mappingProperties;

    public WeatherPayloadConverter(ApiMappingProperties mappingProperties) {
        this.mappingProperties = mappingProperties;
    }

    public WeatherAggregateData convertJsonToData(String jsonPayload) {
        log.info("Converting JSON payload to model");
        return new WeatherAggregateData(jsonToCurrentWeather(jsonPayload), jsonToForecastedWeather(jsonPayload));
    }

    private WeatherSnapshot jsonToCurrentWeather(String jsonPayload) {
        String tempC = JsonPath.read(jsonPayload, mappingProperties.getCurrentWeather().getTempC());
        String tempF = JsonPath.read(jsonPayload, mappingProperties.getCurrentWeather().getTempF());
        String humidity = JsonPath.read(jsonPayload, mappingProperties.getCurrentWeather().getHumidity());
        String weatherDescription = JsonPath.read(jsonPayload, mappingProperties.getCurrentWeather().getDescription());

        return new WeatherSnapshot(tempC, tempF, weatherDescription, humidity);
    }

    //TODO: refactor this to o more generic method
    private Map<String, WeatherSnapshot> jsonToForecastedWeather(String jsonPayload) {
        List<String> tempC = JsonPath.read(jsonPayload, mappingProperties.getForecastWeather().getTempC());
        List<String> tempF = JsonPath.read(jsonPayload, mappingProperties.getForecastWeather().getTempF());
        List<String> humidity = JsonPath.read(jsonPayload, mappingProperties.getForecastWeather().getHumidity());
        List<String> weatherDescription = JsonPath.read(jsonPayload, mappingProperties.getForecastWeather().getDescription());
        List<String> weatherDate = JsonPath.read(jsonPayload, mappingProperties.getForecastWeather().getDescription());

        return IntStream
                .range(0, tempC.size())
                .mapToObj(i -> new WeatherSnapshot(tempC.get(i), tempF.get(i), humidity.get(i), weatherDescription.get(i), weatherDate.get(i)))
                .collect(Collectors.toMap(WeatherSnapshot::getDate, Function.identity()));
    }

}
