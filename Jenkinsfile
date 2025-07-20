pipeline {
    agent any

    stages {
        stage('Build JAR') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Docker Build & Push') {
            steps {
                sh 'docker build -t yashkhanivadekar/weather-app:latest .'
                sh 'docker push yashkhanivadekar/weather-app:latest'
            }
        }

        stage('Docker Deploy') {
            steps {
                sh 'docker-compose down || true'
                sh 'docker-compose pull'
                sh 'docker-compose up -d'
            }
        }
    }
}