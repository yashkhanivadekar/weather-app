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
        git branch: 'main', url: 'git@github.com:yashkhanivadekar/weather-app.git'
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
      sh 'docker-compose down || true'
      archiveArtifacts artifacts: '**/build/**', allowEmptyArchive: true
    }
    failure {
      echo 'Build failed!'
    }
  }
}