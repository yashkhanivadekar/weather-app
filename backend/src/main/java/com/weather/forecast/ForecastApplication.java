package com.weather.forecast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ForecastApplication {
	public static void main(String[] args) {
		System.out.println("🚀 Starting Forecast app");
		SpringApplication.run(ForecastApplication.class, args);
	}
}