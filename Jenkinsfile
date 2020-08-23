pipeline {
  agent any
  stages {
    stage('test') {
      steps {
        sh 'mvn test'
      }
    }

    stage('results') {
      steps {
        junit 'target/surefire-reports/*.xml'
      }
    }

  }
}