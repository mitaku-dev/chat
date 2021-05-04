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
        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }
    }
}