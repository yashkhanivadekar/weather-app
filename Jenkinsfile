pipeline {
    agent any

    environment {
        DOCKER_BUILDKIT = '1'
    }

    stages {
        stage('Build JAR') {
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

        stage('Docker Deploy') {
            steps {
                sh 'docker run -d -p 8080:8080 --name weather-app weather-app'
            }
        }
    }

    post {
        failure {
            echo 'Build failed! Check logs.'
        }
    }
}