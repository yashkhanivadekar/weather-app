package com.weather.forecast.controller;

import com.weather.forecast.mapper.ForecastMapper;
import com.weather.forecast.model.DailyForecast;
import com.weather.forecast.model.ForecastResponse;
import com.weather.forecast.provider.WeatherDataProvider;
import com.weather.forecast.rule.AlertRule;
import com.weather.forecast.service.WeatherService;
import com.weather.forecast.service.impl.WeatherServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(WeatherController.class)
@Import(WeatherForecastControllerTest.TestConfig.class)
public class WeatherForecastControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public WeatherDataProvider weatherProvider() {
            return city -> new ForecastResponse(
                    city,
                    List.of(new DailyForecast(
                            "2025-07-20", 35.0, 25.0, 12.0, "Rain",
                            List.of("Carry Umbrella", "It’s too windy, watch out!")
                    ))
            );
        }

        @Bean
        public ForecastMapper forecastMapper(List<AlertRule> alertRules) {
            return new ForecastMapper(alertRules);
        }

        @Bean
        public List<AlertRule> alertRules() {
            return List.of(
                    (intervals, maxTemp, windSpeed, condition) ->
                            maxTemp > 40 ? Optional.of("Use Sunscreen Lotion") : Optional.empty(),
                    (intervals, maxTemp, windSpeed, condition) ->
                            "Rain".equalsIgnoreCase(condition) ? Optional.of("Carry Umbrella") : Optional.empty(),
                    (intervals, maxTemp, windSpeed, condition) ->
                            windSpeed > 10 ? Optional.of("It’s too windy, watch out!") : Optional.empty(),
                    (intervals, maxTemp, windSpeed, condition) ->
                            "Thunderstorm".equalsIgnoreCase(condition) ? Optional.of("Don’t step out! A Storm is brewing!") : Optional.empty()
            );
        }

        @Bean
        public WeatherService weatherService(WeatherDataProvider provider) {
            return new WeatherServiceImpl(provider);
        }
    }

    @Test
    void testGetForecast() throws Exception {
        mockMvc.perform(get("/weather")
                        .param("city", "London")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forecast.city", is("London")))
                .andExpect(jsonPath("$.forecast.forecast[0].alerts", hasItem("Carry Umbrella")))
                .andExpect(jsonPath("$.forecast.forecast[0].alerts", hasItem("It’s too windy, watch out!")))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
}
