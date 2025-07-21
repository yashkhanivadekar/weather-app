package com.weather.forecast.provider;

import com.weather.forecast.model.ForecastResponse;

public interface WeatherDataProvider {
    ForecastResponse getForecast(String city);
}
