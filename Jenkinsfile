pipeline {
    agent any // Defines where the pipeline will run. 'any' means it can run on any available agent.

    options {
        // Sets a timeout for the entire pipeline. If it exceeds 10 minutes, the build will be aborted.
        timeout(time: 10, unit: 'MINUTES')
        // Configures build history retention, keeping only the last 5 builds.
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    // Defines environment variables for the pipeline.
    environment {
        // Retrieves the API key from Jenkins credentials store using the ID 'OPENWEATHER_API_KEY'.
        // This makes the key available as an environment variable within the pipeline.
        OPENWEATHER_API_KEY = credentials('OPENWEATHER_API_KEY')
    }

    stages {
        // Stage 1: Checkout Code from Git Repository
        stage('Checkout Code') {
            steps {
                // Checks out the source code from the specified Git repository.
                // It targets the 'main' branch.
                checkout([$class: 'GitSCM', branches: [[name: '*/main']],
                          userRemoteConfigs: [[url: 'git@github.com:yashkhanivadekar/weather-app.git']]])
            }
        }

        // Stage 2: Clean Docker Environment
        stage('Clean Environment') {
            steps {
                // Stops and removes Docker Compose services and their associated networks/volumes.
                // '|| true' ensures the step doesn't fail if containers/services don't exist.
                sh 'docker-compose down --remove-orphans || true'
                // Removes all unused Docker objects (containers, images, networks, volumes).
                // '-f' forces removal, '-a' removes all unused, not just dangling.
                sh 'docker system prune -af || true'
            }
        }

        // Stage 3: Inject Secrets into .env file
        stage('Inject Secrets') {
            steps {
                // Creates or overwrites a '.env' file in the workspace.
                // This file will contain the OpenWeather API key, allowing Docker Compose to use it.
                // The `OPENWEATHER_API_KEY` variable is populated from the Jenkins credential.
                writeFile file: '.env', text: "OPENWEATHER_API_KEY=${OPENWEATHER_API_KEY}"
            }
        }

        // Stage 4: Build Docker Services
        stage('Build Services') {
            steps {
                // Builds the Docker images defined in the docker-compose.yml file.
                sh 'docker-compose build'
            }
        }

        // Stage 5: Deploy Docker Services
        stage('Deploy') {
            steps {
                // Starts the Docker Compose services in detached mode (-d).
                sh 'docker-compose up -d'
            }
        }

        // Stage 6: Health Check
        stage('Health Check') {
            steps {
                script {
                    // Waits for 10 seconds to allow services to start up.
                    sleep 10
                    // Performs a health check by curling a specific endpoint of the deployed application.
                    // '-f' makes curl fail silently on HTTP errors (e.g., 4xx or 5xx),
                    // which will cause this step to fail if the health check isn't successful.
                    sh 'curl -f http://localhost:8081/weather?city=Mumbai'
                }
            }
        }
    }

    // Post-build actions, executed after all stages are completed.
    post {
        // This block always executes, regardless of the pipeline's success or failure.
        always {
            // Tears down the Docker Compose services.
            // '|| true' prevents the pipeline from failing if the services are already down.
            sh 'docker-compose down || true'
        }
        // This block executes only if the pipeline fails.
        failure {
            echo 'Build failed!'
        }
        // This block executes only if the pipeline succeeds.
        success {
            echo 'Build and deployment successful!'
        }
    }
}