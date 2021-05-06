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

                                    def res = sshCommand remote: remote, command: 'docker pull images.mfhost.de/chat-be'
                                    echo res
                                    def res = sshCommand remote: remote, command: 'docker stop chat_be || true && docker rm chat_be || true'
                                    echo res
                                    def res = sshCommand remote: remote, command: 'docker run images.mfhost.de/chat-be -d -p 3333:8080 --name chat_be'
                                    echo res
                                }



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

