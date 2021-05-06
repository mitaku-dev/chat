pipeline {
    agent any

    triggers {
        pollSCM '* * * * *'
    }


    def remote = [:]
    remote.name = "node-1"
    remote.host = "10.000.000.153"
    remote.allowAnyHosts = true



    stages {
        stage('build') {
            steps {
                sh './gradlew build -x test'
                sh 'docker build --build-arg JAR_FILE=build/libs/*.jar -t images.mfhost.de/chat-be .'
                sh 'docker push images.mfhost.de/chat-be'
            }
        }
            node {
                   withCredentials([sshUserPrivateKey(credentialsId: 'sshAuth', keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'userName')]) {
                       remote.user = userName
                       remote.identityFile = identity
                       stage("deploy") {
                           sshCommand remote: remote, command: 'docker pull images.mfhost.de/chat-be'
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