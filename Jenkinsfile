pipeline {
    agent any

    options {
        timeout(time: 10, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    environment {
        WEATHER_API_KEY = credentials('WEATHER_API_KEY')
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']],
                          userRemoteConfigs: [[url: 'git@github.com:yashkhanivadekar/weather-app.git']]])
            }
        }

        stage('Clean Environment') {
            steps {
                sh 'docker-compose down --remove-orphans || true'
                sh 'docker system prune -af || true'
            }
        }

        stage('Inject Secrets') {
            steps {
                writeFile file: '.env', text: "WEATHER_API_KEY=${WEATHER_API_KEY}"
            }
        }

        stage('Build Services') {
            steps {
                sh 'docker-compose build'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose up -d'
            }
        }

        stage('Health Check') {
            steps {
                script {
                    sleep 10
                    sh 'curl -f http://localhost:8081/weather?city=Mumbai'
                }
            }
        }
    }

    post {
        always {
            script {
                // Use 'agent any' here directly. This ensures a node is allocated
                // and the workspace context is available for the 'sh' command.
                // It's more robust than relying on env.NODE_NAME here.
                node {
                    sh 'docker-compose down || true'
                }
            }
        }
        failure {
            echo 'Build failed!'
        }
        success {
            echo 'Build and deployment successful!'
        }
    }
}