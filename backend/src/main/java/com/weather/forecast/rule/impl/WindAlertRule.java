package com.weather.forecast.rule.impl;

import com.weather.forecast.dto.ForecastInterval;
import com.weather.forecast.rule.AlertRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WindAlertRule implements AlertRule {
    @Override
    public Optional<String> evaluate(List<ForecastInterval> intervals, double maxTemp, double windSpeed, String condition) {
        double windSpeedMph = windSpeed * 2.23694;
        if (windSpeedMph > 10) {
            return Optional.of("It’s too windy, watch out!");
        }
        return Optional.empty();
    }
}
