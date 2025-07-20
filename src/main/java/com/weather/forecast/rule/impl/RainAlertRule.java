package com.weather.forecast.rule.impl;

import com.weather.forecast.dto.ForecastInterval;
import com.weather.forecast.rule.AlertRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RainAlertRule implements AlertRule {
    public Optional<String> evaluate(List<ForecastInterval> intervals, double maxTemp, double windSpeed, String condition) {
        return "Rain".equalsIgnoreCase(condition) ? Optional.of("Carry Umbrella") : Optional.empty();
    }
}
