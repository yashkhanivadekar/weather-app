package com.weather.forecast.rule;

import com.weather.forecast.dto.ForecastInterval;
import com.weather.forecast.dto.Main;
import com.weather.forecast.dto.Weather;
import com.weather.forecast.dto.Wind;
import com.weather.forecast.rule.impl.HeatAlertRule;
import com.weather.forecast.rule.impl.RainAlertRule;
import com.weather.forecast.rule.impl.ThunderstormAlertRule;
import com.weather.forecast.rule.impl.WindAlertRule;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


public class AlertRuleTests {

    @Test
    void testRainAlertRule() {
        ForecastInterval interval = new ForecastInterval();
        Weather weather = new Weather();
        weather.setMain("Rain");
        interval.setWeather(List.of(weather));

        RainAlertRule rule = new RainAlertRule();
        Optional<String> alert = rule.evaluate(List.of(interval), 25.0, 5.0, "Rain");

        assertTrue(alert.isPresent());
        assertEquals("Carry Umbrella", alert.get());
    }

    @Test
    void testHeatAlertRule() {
        HeatAlertRule rule = new HeatAlertRule();
        Optional<String> alert = rule.evaluate(List.of(), 41.0, 5.0, "Clear");

        assertTrue(alert.isPresent());
        assertEquals("Use Sunscreen Lotion", alert.get());
    }

    @Test
    void testWindAlertRule() {
        WindAlertRule rule = new WindAlertRule();
        Optional<String> alert = rule.evaluate(List.of(), 30.0, 11.0, "Clear");

        assertTrue(alert.isPresent());
        assertEquals("It’s too windy, watch out!", alert.get());
    }

    @Test
    void testThunderstormAlertRule() {
        ThunderstormAlertRule rule = new ThunderstormAlertRule();
        Optional<String> alert = rule.evaluate(List.of(), 27.0, 15.0, "Thunderstorm");

        assertTrue(alert.isPresent());
        assertEquals("Don’t step out! A Storm is brewing!", alert.get());
    }
    @Test
    void testThunderstormAndWindAlertRule() {
        ThunderstormAlertRule thunderRule = new ThunderstormAlertRule();
        WindAlertRule windRule = new WindAlertRule();

        double windSpeed = 15.0;
        String condition = "Thunderstorm";

        Optional<String> thunderAlert = thunderRule.evaluate(List.of(), 27.0, windSpeed, condition);
        Optional<String> windAlert = windRule.evaluate(List.of(), 27.0, windSpeed, condition);

        assertTrue(thunderAlert.isPresent(), "Thunderstorm alert should be present");
        assertEquals("Don’t step out! A Storm is brewing!", thunderAlert.get());

        assertTrue(windAlert.isPresent(), "Wind alert should be present");
        assertEquals("It’s too windy, watch out!", windAlert.get());
    }

}