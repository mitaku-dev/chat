pipeline {
    agent any

    triggers {
        pollSCM '* * * * *'
    }
    stages {
        stage('build') {
            steps {
                sh './gradlew assemble'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }
    }
}