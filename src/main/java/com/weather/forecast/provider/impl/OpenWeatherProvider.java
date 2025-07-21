package com.weather.forecast.provider.impl;

import com.weather.forecast.dto.ForecastInterval;
import com.weather.forecast.dto.OpenWeatherApiResponse;
import com.weather.forecast.mapper.ForecastMapper;
import com.weather.forecast.model.ForecastResponse;
import com.weather.forecast.provider.WeatherDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.util.List;

@Component
@Slf4j
public class OpenWeatherProvider implements WeatherDataProvider {

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${WEATHER_API_KEY}")
    private String apiKey;

    @Value("${weather.api.count}")
    private int apiCount;

    @Value("${WEATHER_OFFLINE_MODE:false}")
    private boolean offlineMode;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ForecastMapper forecastMapper;

    public OpenWeatherProvider(RestTemplate restTemplate, ObjectMapper objectMapper, ForecastMapper forecastMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.forecastMapper = forecastMapper;
    }

    @Override
    public ForecastResponse getForecast(String city) {
        if (offlineMode) {
            log.warn("Offline mode is enabled, returning mock forecast");
            List<ForecastInterval> mock = forecastMapper.generateMockIntervals();
            return forecastMapper.mapToForecastResponse(city, mock);
        }

        try {
            URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                    .queryParam("q", city)
                    .queryParam("appid", apiKey)
                    .queryParam("cnt", apiCount)
                    .build()
                    .toUri();

            OpenWeatherApiResponse response = restTemplate.getForObject(uri, OpenWeatherApiResponse.class);
            List<ForecastInterval> intervals = response != null ? response.getList() : List.of();
            return forecastMapper.mapToForecastResponse(city, intervals);
        } catch (Exception e) {
            log.error("API call failed. Falling back to mock data for city: {}", city, e);
            return forecastMapper.mapToForecastResponse(city, forecastMapper.generateMockIntervals());
        }

    }
}
