pipeline {
    agent any

    environment {
        WEATHER_API_KEY = credentials('weather-api-key') // Use Jenkins credentials store
    }

    stages {
        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build --build-arg WEATHER_API_KEY=$WEATHER_API_KEY -t weather-app .'
            }
        }
        stage('Docker Run') {
            steps {
                sh 'docker stop weather-container || true'
                sh 'docker rm weather-container || true'
                sh 'docker run -d -p 8080:8080 --name weather-container weather-app'
            }
        }
    }
}