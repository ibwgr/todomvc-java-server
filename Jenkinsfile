pipeline {
  agent any
  stages {
    stage('test') {
      steps {
        sh 'mvn test'
      }
    }
  }
  post {
    always {
        junit 'target/surefire-reports/*.xml'
    }
  }
}
