package com.weather.forecast.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DailyForecast {
    private String date;
    private double maxTemp;
    private double minTemp;
    private double windSpeed;
    private String condition;
    private List<String> alerts;

}
