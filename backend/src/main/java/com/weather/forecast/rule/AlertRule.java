package com.weather.forecast.rule;

import com.weather.forecast.dto.ForecastInterval;

import java.util.List;
import java.util.Optional;

public interface AlertRule {
    Optional<String> evaluate(List<ForecastInterval> intervals, double maxTemp, double windSpeed, String condition);
}
