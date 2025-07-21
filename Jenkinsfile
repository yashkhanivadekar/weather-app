pipeline {
  agent any

  environment {
    COMPOSE_PROJECT_NAME = "weatherapp"
  }

  stages {
    stage('Checkout Code') {
      steps {
        git url: 'git@github.com:yashkhanivadekar/weather-app.git', credentialsId: 'jenkins-github-key'
      }
    }

    stage('Build and Deploy Containers') {
      steps {
        sh 'docker-compose -f docker-compose.yml up --build -d weather-backend weather-ui'
      }
    }

    stage('Verify Services') {
      steps {
        sh 'docker ps'
        sh 'curl -s http://localhost:8080/api/forecast?city=Seattle || echo "API not responding"'
      }
    }
  }

  post {
    failure {
      echo 'Pipeline failed. Check logs and container states.'
    }
    success {
      echo 'Weather app deployed successfully!'
    }
  }
}