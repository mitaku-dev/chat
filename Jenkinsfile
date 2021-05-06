def remote = [:]
remote.name = "root"
remote.host = "95.111.255.170"
remote.allowAnyHosts = true

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

            sshagent(credentials : ['sshAuth']) {
                        sh 'ssh -o StrictHostKeyChecking=no root@95.111.255.170 uptime'
                        sh 'ssh -v root@95.111.255.170'
                        sh 'docker stop chat_be || true && docker rm chat_be || true'
                        sh 'docker run images.mfhost.de/chat-be -d -p 3333:8080 --name chat_be'
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

