package com.weather.forecast.rule.impl;

import com.weather.forecast.dto.ForecastInterval;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ThunderstormAlertRule {
    public Optional<String> evaluate(List<ForecastInterval> intervals, double maxTemp, double windSpeed, String condition) {
        if ("Thunderstorm".equalsIgnoreCase(condition)) {
            return Optional.of("Don’t step out! A Storm is brewing!");
        }
        return Optional.empty();
    }
}
