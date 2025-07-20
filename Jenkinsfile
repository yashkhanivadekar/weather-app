pipeline {
    agent any

    environment {
        IMAGE_NAME = 'weather-app'
        CONTAINER_NAME = 'weather-container'
    }

    stages {
        stage('Clone') {
            steps {
                git credentialsId: 'jenkins-github-key', url: 'git@github.com:yashkhanivadekar/weather-app.git'
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
                sh 'docker build -t ${IMAGE_NAME} .'
            }
        }

        stage('Docker Deploy') {
            steps {
                sh 'docker stop ${CONTAINER_NAME} || true'
                sh 'docker rm ${CONTAINER_NAME} || true'
                sh 'docker run -d -p 8080:8080 --name ${CONTAINER_NAME} ${IMAGE_NAME}'
            }
        }
    }

    post {
        failure {
            echo "Build failed! Check logs."
        }
    }
}