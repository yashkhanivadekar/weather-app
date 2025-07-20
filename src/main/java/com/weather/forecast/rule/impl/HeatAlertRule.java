package com.weather.forecast.rule.impl;

import com.weather.forecast.dto.ForecastInterval;
import com.weather.forecast.rule.AlertRule;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HeatAlertRule implements AlertRule {
    @Override
    public Optional<String> evaluate(List<ForecastInterval> intervals, double maxTemp, double windSpeed, String condition) {
        return maxTemp > 40 ? Optional.of("Use Sunscreen Lotion") : Optional.empty();
    }
}
