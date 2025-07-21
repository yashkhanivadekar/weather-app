pipeline {
    agent any

    environment {
        HOME = "/var/jenkins_home"
        DOCKER_BUILDKIT = "1"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build JAR') {
            steps {
                dir('weather-app') {
                    sh 'chmod +x mvnw || true'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }

        stage('Docker Build') {
            steps {
                dir('weather-app') {
                    sh 'docker build -t weather-app .'
                }
            }
        }

        stage('Docker Deploy') {
            steps {
                sh 'docker rm -f weather-app || true'
                sh 'docker run --rm -d -p 9091:8080 --name weather-app weather-app'
            }
        }
    }

    post {
        failure {
            echo 'Build failed! Check logs.'
        }
    }
}