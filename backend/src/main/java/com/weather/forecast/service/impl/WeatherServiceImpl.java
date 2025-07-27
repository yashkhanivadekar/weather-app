package com.weather.forecast.service.impl;

import com.weather.forecast.model.ForecastResponse;
import com.weather.forecast.provider.WeatherDataProvider;
import com.weather.forecast.service.WeatherService;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final WeatherDataProvider weatherDataProvider;

    public WeatherServiceImpl(WeatherDataProvider weatherDataProvider) {
        this.weatherDataProvider = weatherDataProvider;
    }

    @Override
    public ForecastResponse getForecastForCity(String city) {
        if(city == null || city.trim().isEmpty()){
            throw new IllegalArgumentException("City cannot be empty");
        }
        return weatherDataProvider.getForecast(city.trim());
    }
}
