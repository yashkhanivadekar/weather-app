package com.weather.forecast.controller;

import com.weather.forecast.model.ForecastResponse;
import com.weather.forecast.service.WeatherService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.Link;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Weather Forecast", description = "Get 3-day weather forecast with alerts")
@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    @Operation(summary = "Get 3-day forecast", description = "Returns high/low temperatures and alerts for the next 3 days")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forecast successfully retrieved",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ForecastModel.class),
                            examples = @ExampleObject(value = """
                {
                  "forecast": {
                    "city": "Mumbai",
                    "forecast": [
                      {
                        "date": "2025-07-20",
                        "maxTemp": 28.0,
                        "minTemp": 26.0,
                        "windSpeed": 7.1,
                        "condition": "Rain",
                        "alerts": ["Carry Umbrella", "It’s too windy, watch out!"]
                      }
                    ]
                  },
                  "_links": {
                    "self": {
                      "href": "/weather?city=Mumbai"
                    }
                  }
                }
            """)
                    )
            ),
            @ApiResponse(responseCode = "200", description = "Forecast successfully retrieved"),
            @ApiResponse(responseCode = "400", description = "Missing or invalid city parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ForecastModel getForecast(
            @Parameter(description = "City name to fetch forecast for", required = true)
            @RequestParam String city){
        ForecastResponse response = weatherService.getForecastForCity(city);

        ForecastModel model = new ForecastModel(response);
        model.add(Link.of("/weather?city=" + city).withSelfRel());
        return model;
    }
    @Getter
    public static class ForecastModel extends RepresentationModel<ForecastModel> {
        private final ForecastResponse forecast;

        public ForecastModel(ForecastResponse forecast) {
            this.forecast = forecast;
        }
    }
}
