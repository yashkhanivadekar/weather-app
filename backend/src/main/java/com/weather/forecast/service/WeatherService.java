package com.weather.forecast.service;

import com.weather.forecast.model.ForecastResponse;

public interface WeatherService {
    ForecastResponse getForecastForCity(String city);
}
