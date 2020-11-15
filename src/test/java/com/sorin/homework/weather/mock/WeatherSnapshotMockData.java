package com.sorin.homework.weather.mock;

import com.sorin.homework.weather.model.WeatherSnapshot;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class WeatherSnapshotMockData {

    public static final String CURRENT_CONDITION = "Feeling fit";
    public static final double CURRENT_TEMP_F = 34.5;
    public static final double CURRENT_TEMP_C = 9.9;
    public static final int CURRENT_HUMIDITY = 75;

    public static final String FORECAST_CONDITION = "Feeling extra fit";
    public static final double FORECAST_TEMP_F = 37.4;
    public static final double FORECAST_TEMP_C = 12.3;
    public static final int FORECAST_HUMIDITY = 66;

    public static WeatherSnapshot getMockedCurrentWeather() {
        return WeatherSnapshot.builder()
                .withCondition(CURRENT_CONDITION)
                .withHumidity(CURRENT_HUMIDITY)
                .withDate(LocalDate.now())
                .withTempF(CURRENT_TEMP_F)
                .withTempC(CURRENT_TEMP_C)
                .build();
    }

    public static List<WeatherSnapshot> getMockedForecastedWeather() {
        return Collections.singletonList(WeatherSnapshot.builder()
                .withDate(LocalDate.now().plus(1, ChronoUnit.DAYS))
                .withCondition(FORECAST_CONDITION)
                .withHumidity(FORECAST_HUMIDITY)
                .withTempF(FORECAST_TEMP_F)
                .withTempC(FORECAST_TEMP_C)
                .build());
    }

}
