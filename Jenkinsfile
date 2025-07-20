pipeline {
    agent any

    environment {
        DOCKER_BUILDKIT = "1"
    }

    stages {
        stage('Clone') {
            steps {
                checkout scm
                sh 'git remote -v'
            }
        }
    }
}