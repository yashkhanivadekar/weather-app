package com.weather.forecast.provider.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.weather.forecast.dto.ForecastInterval;
import com.weather.forecast.dto.OpenWeatherApiResponse;
import com.weather.forecast.exception.CityNotFoundException;
import com.weather.forecast.mapper.ForecastMapper;
import com.weather.forecast.model.ForecastResponse;
import com.weather.forecast.provider.WeatherDataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
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

    @Value("${weather.offline-mode:false}")
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

            ResponseEntity<OpenWeatherApiResponse> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    OpenWeatherApiResponse.class
            );

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                OpenWeatherApiResponse response = responseEntity.getBody();
                List<ForecastInterval> intervals = response != null ? response.getList() : List.of();
                return forecastMapper.mapToForecastResponse(city, intervals);
            } else if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new CityNotFoundException("City not found: " + city);
            } else {
                throw new RuntimeException("Unexpected API error");
            }

        } catch (HttpClientErrorException.NotFound e) {
            log.warn("City not found: {}", city);
            throw new CityNotFoundException("City not found: " + city);
        } catch (Exception e) {
            log.error("Unexpected error for city: {}, {}", city, e.getMessage());
            throw new RuntimeException("Internal server error");
        }
    }
}
