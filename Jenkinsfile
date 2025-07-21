pipeline {
    agent any

    environment {
        DOCKER_BUILDKIT = '1'
        HOME = "/var/jenkins_home"
    }

    options {
        skipDefaultCheckout()
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

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
                // Stop and remove any running container (optional safety)
                sh 'docker rm -f weather-app || true'
                sh 'docker run --rm -d -p 8081:8080 --name weather-app weather-app'
            }
        }
    }

    post {
        failure {
            echo 'Build failed! Check logs.'
        }
        success {
            echo 'Build and deployment successful!'
        }
        always {
            echo 'Pipeline finished.'
        }
    }
}