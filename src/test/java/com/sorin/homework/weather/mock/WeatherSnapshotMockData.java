package com.sorin.homework.weather.mock;

import com.sorin.homework.weather.model.WeatherSnapshot;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

public class WeatherSnapshotMockData {

    public static final String CURRENT_CONDITION = "Feeling fit";

    public static WeatherSnapshot getMockedCurrentWeather() {
        return WeatherSnapshot.builder()
                .withCondition(CURRENT_CONDITION)
                .withDate(LocalDate.now())
                .withHumidity(75)
                .withTempF(34.5)
                .withTempC(9.9)
                .build();
    }

    public static List<WeatherSnapshot> getMockedForecastedWeather() {
        return Collections.singletonList(WeatherSnapshot.builder()
                .withCondition("Feeling extra fit")
                .withDate(LocalDate.now().plus(1, ChronoUnit.DAYS))
                .withHumidity(66)
                .withTempF(37.4)
                .withTempC(12.3)
                .build());
    }

}
