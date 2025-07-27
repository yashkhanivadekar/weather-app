package com.weather.forecast.model;

import java.util.List;

import com.weather.forecast.dto.ForecastInterval;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForecastResponse {
    private String city;
    private List<DailyForecast> forecast;
}
