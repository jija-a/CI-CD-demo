pipeline {
    agent any

    stages {
        stage('Scan and Build') {
            steps {
                withSonarQubeEnv(installationName: 'sq1', credentialsId: 'sonarqube-secret') {
                    sh './gradlew clean build sonarqube'
                }
            }
        }

        stage('Quality gate') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Packaging') {
            steps {
                withSonarQubeEnv(installationName: 'sq1', credentialsId: 'sonarqube-secret') {
                    sh './gradlew war'
                }
            }
        }

        stage ('Deploy') {
            steps {
                script {
                    deploy adapters: [tomcat9(credentialsId: 'tomcat-deployer', path: '', url: 'http://localhost:8080')], contextPath: '/web-0.0.1-SNAPSHOT.war', onFailure: false, war: 'web/build/libs/web-0.0.1-SNAPSHOT.war'
                }
            }
        }
    }
}
