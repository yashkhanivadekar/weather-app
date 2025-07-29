import React, { useState } from 'react';

const ForecastCard = () => {
    const [city, setCity] = useState("");
    const [forecast, setForecast] = useState(null);
    const [error, setError] = useState(null);

    const fetchForecast = async () => {
        if (!city.trim()) {
            setForecast(null);
            setError("City name cannot be empty");
            return;
        }
        try {
            const response = await fetch(`/weather?city=${city}`);
            if (!response.ok) throw new Error("Failed to fetch forecast");
            const data = await response.json();
            setForecast(data.forecast); // backend format: { city, forecast: [...] }
            setError(null);
        } catch (err) {
            setForecast(null);
            setError(err.message);
        }
    };

    return (
        <div style={{ padding: "2rem", fontFamily: "Arial" }}>
            <h1>🌤️ 3-Day Weather Forecast</h1>

            <input
                type="text"
                value={city}
                placeholder="Enter city name"
                onChange={(e) => setCity(e.target.value)}
                style={{ padding: "0.5rem", marginRight: "1rem" }}
            />
            <button onClick={fetchForecast} style={{ padding: "0.5rem" }}>
                Get Forecast
            </button>

            {error && <p style={{ color: "red", marginTop: "1rem" }}>{error}</p>}

            {forecast && (
                <div style={{ marginTop: "2rem" }}>
                    <h2>Forecast for {forecast.city}</h2>
                    {forecast.forecast?.length > 0 ? (
                        forecast.forecast.map((day, index) => (
                            <div
                                key={index}
                                style={{
                                    border: "1px solid #ccc",
                                    padding: "1rem",
                                    marginBottom: "1rem",
                                    borderRadius: "8px",
                                    backgroundColor: "#f9f9f9",
                                }}
                            >
                                <strong>Date:</strong> {day.date} <br />
                                <strong>High:</strong> {day.maxTemp}°C <br />
                                <strong>Low:</strong> {day.minTemp}°C <br />
                                <strong>Wind Speed:</strong> {day.windSpeed} mph/h <br />
                                <strong>Condition:</strong> {day.condition} <br />
                                <strong>Alerts:</strong>{" "}
                                {day.alerts?.length > 0 ? day.alerts.join(", ") : "None"}
                            </div>
                        ))
                    ) : (
                        <p>No forecast data available.</p>
                    )}
                </div>
            )}
        </div>
    );
};


export default ForecastCard;