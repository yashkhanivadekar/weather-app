pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean package -DskipTests'
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t weather-app .'
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