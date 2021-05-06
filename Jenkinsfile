pipeline {
    agent any

    triggers {
        pollSCM '* * * * *'
    }
    stages {
        stage('build') {
            steps {
                sh './gradlew build -x test'
            }
        }
        stage('build docker') {
            agent {
                docker {
                    image 'gcr.io/kaniko-project/executor:latest'
                }
                steps {
                    sh './run_in_docker.sh . . images.mfhost.de/chat-be:latest'
                }
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            junit 'build/reports/**/*.xml'
        }
    }
}