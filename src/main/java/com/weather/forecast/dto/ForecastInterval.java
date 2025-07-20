package com.weather.forecast.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Getter
@Setter
public class ForecastInterval {

    @JsonProperty("dt")
    private long timestamp;

    private Main main;
    private Wind wind;
    private List<Weather> weather;

    private List<String> alerts;

    public LocalDateTime getDateTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneOffset.UTC);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.timestamp = dateTime.toEpochSecond(ZoneOffset.UTC);
    }
}
