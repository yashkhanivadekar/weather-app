pipeline {
    agent any

    options {
        timeout(time: 10, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    environment {
        OPENWEATHER_API_KEY = credentials('OPENWEATHER_API_KEY')
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']],
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
                writeFile file: '.env', text: "OPENWEATHER_API_KEY=${OPENWEATHER_API_KEY}"
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
            // Wrap the sh command in a node block to ensure a workspace context
            script { // Script block is needed to use 'node' within 'post'
                node(label: "${env.NODE_NAME}") { // Use the same node where the build ran
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