pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

 //       stage('Test') {
 //           steps {
 //               sh 'mvn test'
 //           }
 //           post {
 //               always {
 //                   junit '**/target/surefire-reports/*.xml'
 //               }
  //          }
//        }



        stage('Code Review') {
                    steps {
                        script {
                            try {
                                withSonarQubeEnv('Sonar-Serveur') {
                                    sh "mvn sonar:sonar -Dsonar.java.binaries=target/classes"
                                }
                            } catch (Exception e) {
                                error "L'analyse SonarQube a échoué : ${e.message}"
                            }
                        }
                    }
        }

    }


}