pipeline {
    agent any

    triggers {
        pollSCM '* * * * *'
    }
    stages {
        stage('build') {
            steps {
                sh './websockets/gradlew build'
            }
        }
        stage('Test') {
            steps {
                sh './websockets/gradlew test'
            }
        }
    }
}