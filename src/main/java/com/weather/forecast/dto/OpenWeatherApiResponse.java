package com.weather.forecast.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpenWeatherApiResponse {
    private List<ForecastInterval> list;
}
