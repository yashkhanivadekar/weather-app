package com.weather.forecast.mapper;

import com.weather.forecast.dto.ForecastInterval;
import com.weather.forecast.dto.Main;
import com.weather.forecast.dto.Weather;
import com.weather.forecast.dto.Wind;
import com.weather.forecast.model.DailyForecast;
import com.weather.forecast.model.ForecastResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ForecastMapperTest {

    private ForecastMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ForecastMapper(List.of(
                (intervals, maxTemp, windSpeed, condition) ->
                        maxTemp > 40 ? java.util.Optional.of("Use Sunscreen Lotion") : java.util.Optional.empty(),
                (intervals, maxTemp, windSpeed, condition) ->
                        "Rain".equalsIgnoreCase(condition) ? java.util.Optional.of("Carry Umbrella") : java.util.Optional.empty()
        ));
    }

    @Test
    void testMapToForeCastResponse_withSampleIntervals() {
        ForecastInterval interval = new ForecastInterval();
        interval.setTimestamp(LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC));

        Main main = new Main();
        main.setTemp_max(316.15);
        main.setTemp_min(295.15);
        interval.setMain(main);

        Wind wind = new Wind();
        wind.setSpeed(5.0);
        interval.setWind(wind);

        Weather weather = new Weather();
        weather.setMain("Rain");
        interval.setWeather(List.of(weather));

        ForecastResponse response = mapper.mapToForecastResponse("TestCity", List.of(interval));

        assertEquals("TestCity", response.getCity());
        assertEquals(1, response.getForecast().size());

        List<String> alerts = response.getForecast().get(0).getAlerts();
        assertTrue(alerts.contains("Use Sunscreen Lotion"));  // From temp > 40
        assertTrue(alerts.contains("Carry Umbrella"));        // From condition == Rain
    }
}
