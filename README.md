# ☁️ Weather Prediction Microservice

A production-ready full-stack weather forecast project that shows the next 3 days of high and low temperatures for a city and recommends actions like "Carry umbrella" or "Use sunscreen lotion" based on weather conditions.

---

## 🧱 Project Structure

```
weather-app/
├── backend/               # Spring Boot Microservice
│   ├── src/main/java/
│   ├── Dockerfile
├── ui/                   # React Frontend
│   ├── public/
│   ├── src/
│   ├── Dockerfile
├── jenkins/
│   ├── Dockerfile        # Jenkins (CI/CD only)
├── docker-compose.yml
├── Jenkinsfile
├── README.md
```

---

## 🚀 Features

- Shows 3-day forecast (High/Low temperatures)
- Smart alerts based on weather (rain → umbrella, UV index → sunscreen)
- Toggle mode for offline development (uses mock data)
- 12-factor compliant backend (API key via env)
- HATEOAS + REST
- Jenkins CI/CD pipeline
- Containerized with Docker

---

## 🧩 Architecture

### Services and Classes

#### Backend (Spring Boot)
- `WeatherServiceImpl`: Fetches weather from OpenWeather API
- `OpenWeatherProvider`: REST Client using `RestTemplate`
- `ForecastController`: Exposes `/api/forecast?city=XYZ`
- `AppConfig`: Provides Beans (RestTemplate, ObjectMapper)
- DTOs: `ForecastInterval`, `Main`, `Weather`, `Wind`

#### Frontend (React)
- `CitySearch`: input field for city
- `ForecastCard`: display day-wise forecast
- `App.tsx`: manages toggle and layout

---

## 🛠️ Build & Run

### Prerequisites:
- Docker + Docker Compose

### Build and Deploy
```bash
# Step 1: Clone and enter the repo
$ git clone git@github.com:yourname/weather-app.git
$ cd weather-app

# Step 2: Build & start Jenkins only
$ docker compose -f docker-compose.yml up -d jenkins

# Step 3: Go to Jenkins UI at http://localhost:9090
# Use initial password printed in logs

# Step 4: Create pipeline → use Jenkinsfile in repo root

# Step 5: Jenkins will:
# - Build backend & frontend Docker images
# - Compose services
```

---

## 🧪 Test (Local Only)
```bash
# Optionally test manually from host
$ curl localhost:8080/api/forecast?city=Chicago
```

---

## 🧯 Toggle: Online vs Offline Mode

We support offline development using mock data:

```yaml
# In application.yml
weather:
  mode: offline
```

If mode is offline:
- No API call to OpenWeather
- Returns static mock data

This helps when rate-limited or offline.

---

## 🧪 Sample API Call

```bash
curl "http://localhost:8081/weather?city=Paris"
```
Returns:
```json
{
  "city": "Paris",
  "forecasts": [
    {"day": "Monday", "high": 30, "low": 21, "condition": "Rain"},
    {"day": "Tuesday", "high": 33, "low": 24, "condition": "Clear"},
    {"day": "Wednesday", "high": 28, "low": 20, "condition": "Clouds"}
  ],
  "alerts": ["Carry umbrella"]
}
```

## 🔐 Secrets Handling

- API Key is injected as ENV: `WEATHER_API_KEY`
- `.env` not committed
- Used in Jenkins via Docker ENV

---

## 🧰 Jenkins Pipeline Summary

- Pulls repo via SSH (credentials configured)
- Builds both services
- Runs `docker compose` from Jenkins container
- Uses separate `Dockerfile` for Jenkins CI/CD

---

## 🐳 Docker Overview

| Component | Port | Dockerfile Used      |
|----------|------|-----------------------|
| Backend  | 8080 | `/backend/Dockerfile` |
| Frontend | 80   | `/ui/Dockerfile`      |
| Jenkins  | 9090 | `/jenkins/Dockerfile` |

---

## 📦 Production Handoff Checklist

- [x] `docker-compose.yml` (Backend + Frontend)
- [x] Jenkins setup with SSH key access
- [x] Environment injection for secrets
- [x] Swagger/OpenAPI for Backend
- [x] README with build + deploy steps
- [x] Toggle-based offline mode

---


