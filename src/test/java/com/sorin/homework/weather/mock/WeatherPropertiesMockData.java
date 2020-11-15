package com.sorin.homework.weather.mock;

import com.sorin.homework.weather.config.properties.ApiMappingProperties;
import com.sorin.homework.weather.config.properties.ApiMappingProperties.CurrentWeatherMapping;
import com.sorin.homework.weather.config.properties.ApiMappingProperties.ForecastWeatherMapping;
import com.sorin.homework.weather.config.properties.WeatherApiProperties;

public class WeatherPropertiesMockData {

    public static final int NUMBER_OF_DAYS = 3;
    public static final String WWO_KEY = "homeAssignmentWWOKey";

    public static final String CURRENT_WEATHER_DESCRIPTION_MAPPING = "$.data.current_condition[0].weatherDesc[0].value";
    public static final String CURRENT_WEATHER_HUMIDITY_MAPPING = "$.data.current_condition[0].humidity";
    public static final String CURRENT_WEATHER_TEMP_C_MAPPING = "$.data.current_condition[0].temp_C";
    public static final String CURRENT_WEATHER_TEMP_F_MAPPING = "$.data.current_condition[0].temp_F";

    public static final String FORECAST_WEATHER_DESCRIPTION_MAPPING = "$.data.weather[1:].hourly[0].weatherDesc[0].value";
    public static final String FORECAST_WEATHER_HUMIDITY_MAPPING = "$.data.weather[1:].hourly[0].humidity";
    public static final String FORECAST_WEATHER_TEMP_C_MAPPING = "$.data.weather[1:].hourly[0].tempC";
    public static final String FORECAST_WEATHER_TEMP_F_MAPPING = "$.data.weather[1:].hourly[0].tempF";
    public static final String FORECAST_WEATHER_DATE_MAPPING = "$.data.weather[1:].date";
    public static final String FORECAST_WEATHER_DATE_FORMAT_MAPPING = "yyyy-MM-dd";

    public static WeatherApiProperties getMockedApiProperties(ApiMappingProperties apiMappingProperties) {
        return WeatherApiProperties.builder()
                .withKey(WWO_KEY)
                .withMapping(apiMappingProperties)
                .withNumberOfDays(NUMBER_OF_DAYS)
                .withHumidityAsDecimals(true)
                .build();
    }

    public static ApiMappingProperties getMockedMappingProperties(CurrentWeatherMapping currentMapping, ForecastWeatherMapping forecastMapping) {
        return ApiMappingProperties.builder()
                .withCurrentWeather(currentMapping)
                .withForecastWeather(forecastMapping)
                .build();
    }

    public static CurrentWeatherMapping getMockedCurrentWeatherMapping() {
        return CurrentWeatherMapping.builder()
                .withDescription(CURRENT_WEATHER_DESCRIPTION_MAPPING)
                .withHumidity(CURRENT_WEATHER_HUMIDITY_MAPPING)
                .withTempC(CURRENT_WEATHER_TEMP_C_MAPPING)
                .withTempF(CURRENT_WEATHER_TEMP_F_MAPPING)
                .build();
    }

    public static ForecastWeatherMapping getMockedForecastWeatherMapping() {
        return ForecastWeatherMapping.builder()
                .withDescription(FORECAST_WEATHER_DESCRIPTION_MAPPING)
                .withDateFormat(FORECAST_WEATHER_DATE_FORMAT_MAPPING)
                .withHumidity(FORECAST_WEATHER_HUMIDITY_MAPPING)
                .withTempC(FORECAST_WEATHER_TEMP_C_MAPPING)
                .withTempF(FORECAST_WEATHER_TEMP_F_MAPPING)
                .withDate(FORECAST_WEATHER_DATE_MAPPING)
                .build();
    }

}
