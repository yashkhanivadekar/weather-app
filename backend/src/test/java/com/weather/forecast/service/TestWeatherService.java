package com.weather.forecast.service;

import com.weather.forecast.mapper.ForecastMapper;
import com.weather.forecast.model.ForecastResponse;
import com.weather.forecast.provider.WeatherDataProvider;

public class TestWeatherService implements WeatherService {
    private final WeatherDataProvider provider;

    public TestWeatherService(WeatherDataProvider provider, ForecastMapper mapper) {
        super();
        this.provider = provider;
    }

    @Override
    public ForecastResponse getForecastForCity(String city) {
        return provider.getForecast(city); // delegate directly
    }
}