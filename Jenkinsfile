pipeline {
    agent any

    triggers {
        pollSCM '* * * * *'
    }




    stages {
        stage('build') {
            steps {
                sh './gradlew build -x test'
                sh 'docker build --build-arg JAR_FILE=build/libs/*.jar -t images.mfhost.de/chat-be .'
                sh 'docker push images.mfhost.de/chat-be'
            }
        }
           stage("deploy") {
            steps {
            withCredentials([sshUserPrivateKey(credentialsId: 'sshAuth', keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'userName')]) {
                                script {
                                    def remote = [:]
                                                                remote.user = userName
                                                                remote.identityFile = identity
                                                                remote.name = "root"
                                                                remote.host = "95.111.255.170"
                                                                remote.allowAnyHosts = true
                                }

                                //TODO remove old
                                sshCommand remote: remote, command: 'docker pull images.mfhost.de/chat-be'
                                sshCommand remote: remote, command: 'docker kill chat_be || true'
                                sshCommand remote: remote, command: 'docker run images.mfhost.de/chat-be -d -p 3333:8080 --name chat_be'
                                sh 'echo deploy sucessfull'

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

