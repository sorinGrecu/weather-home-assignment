package com.sorin.homework.weather.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherSnapshot {
    private double tempC;
    private double tempF;
    private String condition;
    private double humidity;
    private String date;

    public WeatherSnapshot(String tempC, String tempF, String condition, String humidity, String date) {
        this.humidity = Double.parseDouble(humidity);
        this.tempC = Double.parseDouble(tempC);
        this.tempF = Double.parseDouble(tempF);
        this.condition = condition;
        this.date = date;
    }

    public WeatherSnapshot(String tempC, String tempF, String condition, String humidity) {
        this.humidity = Double.parseDouble(humidity);
        this.tempC = Double.parseDouble(tempC);
        this.tempF = Double.parseDouble(tempF);
        this.condition = condition;
        this.date = "CURRENT_DATE";
    }
}
