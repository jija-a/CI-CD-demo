pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('SonarScan') {
            steps {
                withSonarQubeEnv(installationName: 'sq1', credentialsId: 'sonarqube-secret') {
                    sh './gradlew sonarqube'
                }
            }
        }

        stage('QualityGate') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage ('Deploy') {
            steps {
                echo 'Deploing...'
                sh 'ls'
                dir('build/libs') {
                    script {
                        withEnv(['JENKINS_NODE_COOLIE=dontkill']) {

                        }
                        deploy adapters: [tomcat9(credentialsId: 'tomcat-deployer', path: '', url: 'http://localhost:8080')], contextPath: '/certificates/', onFailure: false, war: 'build/libs/rest-api-advanced-0.0.1-SNAPSHOT.war'
                    }
                }
            }
        }
    }
}