package com.weather.forecast.mapper;

import com.weather.forecast.dto.ForecastInterval;
import com.weather.forecast.dto.Main;
import com.weather.forecast.dto.Weather;
import com.weather.forecast.dto.Wind;
import com.weather.forecast.model.DailyForecast;
import com.weather.forecast.model.ForecastResponse;
import com.weather.forecast.rule.AlertRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ForecastMapper {

    private final List<AlertRule> alertRules;

    public ForecastMapper(List<AlertRule> alertRules) {
        this.alertRules = alertRules;
    }

    public ForecastResponse mapToForecastResponse(String city, List<ForecastInterval> intervals) {
        log.info("Mapping forecast for city: {} with {} intervals", city, intervals.size());

        Map<LocalDate, List<ForecastInterval>> groupedByDate = intervals.stream()
                .collect(Collectors.groupingBy(interval -> interval.getDateTime().toLocalDate()));

        LocalDate today = LocalDate.now(ZoneId.of("UTC"));

        List<DailyForecast> forecastList = groupedByDate.entrySet().stream()
                .filter(entry -> !entry.getKey().isBefore(today)) // include today and future dates
                .sorted(Map.Entry.comparingByKey())
                .limit(3) // next 3 days only
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<ForecastInterval> dailyIntervals = entry.getValue();

                    double maxTemp = dailyIntervals.stream().mapToDouble(i -> i.getMain().getTemp_max() - 273.15).max().orElse(Double.NaN);
                    double minTemp = dailyIntervals.stream().mapToDouble(i -> i.getMain().getTemp_min() - 273.15).min().orElse(Double.NaN);
                    double windSpeed = dailyIntervals.stream().mapToDouble(i -> i.getWind().getSpeed()).average().orElse(0);

                    String condition = dailyIntervals.stream()
                            .map(i -> i.getWeather().get(0).getMain())
                            .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
                            .entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse("Clear");

                    List<String> alerts = alertRules.stream()
                            .map(rule -> rule.evaluate(dailyIntervals, maxTemp, windSpeed, condition))
                            .flatMap(Optional::stream)
                            .collect(Collectors.toList());

                    double roundedMaxTemp = Math.round(maxTemp);
                    double roundedMinTemp = Math.round(minTemp);
                    double roundedWindSpeed = Math.round(windSpeed);

                    return new DailyForecast(date.toString(), roundedMaxTemp, roundedMinTemp, roundedWindSpeed, condition, alerts);
                })
                .collect(Collectors.toList());

        return new ForecastResponse(city, forecastList);
    }

    public List<ForecastInterval> generateMockIntervals() {
        LocalDate today = LocalDate.now();
        List<ForecastInterval> mockList = new ArrayList<>();

        for(int day = 0; day < 3; day++){
            for(int hour = 0; hour < 24; hour++){
                ForecastInterval mockInterval = new ForecastInterval();
                mockInterval.setDateTime(today.plusDays(day).atTime(hour, 0));

                Main main = new Main();
                main.setTemp_max(310.15);
                main.setTemp_min(293.15);
                mockInterval.setMain(main);

                Wind wind = new Wind();
                wind.setSpeed(12);
                mockInterval.setWind(wind);

                Weather weather = new Weather();
                weather.setMain(day == 1 ? "Thunderstorm" : (day == 2 ? "Rain" : "Clear"));
                mockInterval.setWeather(List.of(weather));

                mockList.add(mockInterval);
            }
        }
        return mockList;
    }
    private static double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

}
