package com.weather.forecast.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OpenWeatherApiResponse {
    private List<ForecastInterval> list;
}
